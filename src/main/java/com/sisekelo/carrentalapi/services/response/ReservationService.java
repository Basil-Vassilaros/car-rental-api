package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.Client;
import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import com.sisekelo.carrentalapi.models.response.Reservation;
import com.sisekelo.carrentalapi.repository.*;
import com.sisekelo.carrentalapi.services.table.RentalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private CarManufacturerRepository carManufacturerRepository;
    private CarModelRepository carModelRepository;
    private CarCategoryRepository carCategoryRepository;
    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private RentalRecordRepository rentalRecordRepository;

    private RentalRecordService rentalRecordService;

    private Boolean missing = false;

    private Boolean problem = false;

    @Autowired
    public ReservationService(CarManufacturerRepository carManufacturerRepository, CarModelRepository carModelRepository, CarCategoryRepository carCategoryRepository, CarRepository carRepository, ClientRepository clientRepository, RentalRecordRepository rentalRecordRepository, RentalRecordService rentalRecordService) {
        this.carManufacturerRepository = carManufacturerRepository;
        this.carModelRepository = carModelRepository;
        this.carCategoryRepository = carCategoryRepository;
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.rentalRecordRepository = rentalRecordRepository;
        this.rentalRecordService = rentalRecordService;
    }

    public ResponseEntity<Object> addReservation(Reservation reservation) {
        RentalRecord newRecord = new RentalRecord();
        problem = false;
        missing = false;
        if (!carRepository.findById(reservation.getCarId()).isPresent()){
            missing = true;
            return ResponseEntity.unprocessableEntity().body("Error: Referenced Car not found");
        }
        if (!clientRepository.findById(reservation.getClientId()).isPresent()){
            missing = true;
            return ResponseEntity.unprocessableEntity().body("Error: Referenced Client not found");
        }
        if (!missing){
            // Check if car is booked for dates reserved
            List<RentalRecord> recordList = rentalRecordRepository.findAll();
            for (RentalRecord record: recordList) {
                // cant be booked before return and cant be returned after booked
                if (reservation.getDateToCollect().isBefore(record.getDateToReturn()) ||
                reservation.getDateToReturn().isAfter(record.getDateToCollect())){
                    problem = true;
                    return ResponseEntity.unprocessableEntity().body("Error: Car is already booked between"
                            +record.getDateToCollect().toString()+" and "+record.getDateToReturn().toString());
                }
                if (record.getClient().getClientId() == reservation.getClientId()){
                    problem = true;
                    return ResponseEntity.unprocessableEntity().body("Error: Client has already made a reservation ("+record.getRentalId()+")");
                }
            }
            if (reservation.getDateToReturn().isBefore(reservation.getDateToCollect())){
                problem = true;
                return ResponseEntity.unprocessableEntity().body("Error: Return date must be after collection date");
            }
            if (!problem){
                newRecord.setCar(carRepository.getReferenceById(reservation.getCarId()));

                newRecord.setClient(clientRepository.getReferenceById(reservation.getClientId()));

                newRecord.setDateToCollect(reservation.getDateToCollect());

                newRecord.setDateReservationMade(LocalDateTime.now());

                newRecord.setDateToReturn(reservation.getDateToReturn());

                Car carReserved = carRepository.getReferenceById(reservation.getCarId());

                carReserved.setIsReserved(true);

                List<String> reservedDates = carReserved.getReservedDates();
                reservedDates.add(reservation.getDateToCollect().toString()+" - "
                        +reservation.getDateToReturn().toString());
                carReserved.setReservedDates(reservedDates);

                clientRepository.getReferenceById(reservation.getClientId()).setCarsReserved(carReserved);

                newRecord.setTotalPrice(rentalRecordService.getTotalPrice(newRecord.getCar().getPrice(), newRecord.getDateToReturn(),
                        newRecord.getDateToCollect()));

                rentalRecordRepository.save(newRecord);
            }
        }
        if(rentalRecordRepository.findById(newRecord.getRentalId()).isPresent()){
            return ResponseEntity.accepted().body("Success: recorded reservation");
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Error: Failed to record reservation");
        }
    }

    public ResponseEntity<Object> addBooking(Long id){
        // find which client
        if (clientRepository.findById(id).isPresent()) {
            Client client = clientRepository.getReferenceById(id);
            List<RentalRecord> recordList = rentalRecordRepository.findAll();
            Long recordId = null;
            for (RentalRecord record: recordList){
                if (record.getClient() == client){
                    //we found our client
                    recordId = record.getRentalId();
                }
            }
            if (recordId.toString() != ""){
                RentalRecord recordFound = rentalRecordRepository.getReferenceById(recordId);
                recordFound.getCar().setInUse(true);
                recordFound.getCar().setIsReserved(false);
                return ResponseEntity.accepted().body("Success: Car is booked in use");
            }
            else{
                return ResponseEntity.unprocessableEntity().body("Error: Client has not made a reservation");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Client not found");
        }
    }

    @Transactional
    public ResponseEntity<Object> updateRecordByClient(Long id, Reservation reservation){
        if (clientRepository.findById(id).isPresent()){
            missing = false;
            if (!carRepository.findById(reservation.getCarId()).isPresent()){
                missing = true;
                return ResponseEntity.unprocessableEntity().body("Error: Referenced Car not found");
            }
            if (!clientRepository.findById(reservation.getClientId()).isPresent()){
                missing = true;
                return ResponseEntity.unprocessableEntity().body("Error: Referenced Client not found");
            }
            if (!missing){
                Client client = clientRepository.getReferenceById(id);
                List<RentalRecord> recordList = rentalRecordRepository.findAll();
                Long recordId = null;
                for (RentalRecord record: recordList){
                    if (record.getClient() == client){
                        //we found our client
                        recordId = record.getRentalId();
                    }
                }
                if (recordId.toString() != ""){
                    RentalRecord updatedRecord = rentalRecordRepository.getReferenceById(recordId);
                    updatedRecord.setCar(carRepository.getReferenceById(reservation.getCarId()));
                    updatedRecord.setClient(clientRepository.getReferenceById(reservation.getClientId()));

                    updatedRecord.setDateReservationMade(LocalDateTime.now());
                    updatedRecord.setTotalPrice(rentalRecordService.getTotalPrice(updatedRecord.getCar().getPrice(),
                            updatedRecord.getDateToReturn(),
                            updatedRecord.getDateToCollect()));
                    Car carReserved = carRepository.getReferenceById(reservation.getCarId());
                    carReserved.setIsReserved(true);
                    List<String> reservedDates = carReserved.getReservedDates();
                    String dateToRemove = updatedRecord.getDateToCollect().toString()+" - "
                            +updatedRecord.getDateToReturn().toString();
                    if (reservedDates.contains(dateToRemove)) {
                        int removeId = reservedDates.indexOf(dateToRemove);
                        reservedDates.remove(removeId);
                    }

                    updatedRecord.setDateToReturn(reservation.getDateToReturn());
                    updatedRecord.setDateToCollect(reservation.getDateToCollect());

                    reservedDates.add(reservation.getDateToCollect().toString()+" - "
                            +reservation.getDateToReturn().toString());
                    carReserved.setReservedDates(reservedDates);

                    clientRepository.getReferenceById(reservation.getClientId()).setCarsReserved(carReserved);
                    return ResponseEntity.accepted().body("Success: record updated");
                }
                else{
                    return ResponseEntity.unprocessableEntity().body("Error: Client has not made a reservation");

                }
            }
            else{
                return ResponseEntity.unprocessableEntity().body("Error: Failed to update record");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Client not found");
        }
    }

    public ResponseEntity<Object> removeReservation(Long id) {
        if (clientRepository.findById(id).isPresent()){
            Client client = clientRepository.getReferenceById(id);
            List<RentalRecord> recordList = rentalRecordRepository.findAll();
            Long recordId = null;
            for (RentalRecord record: recordList){
                if (record.getClient() == client){
                    //we found our client
                    recordId = record.getRentalId();
                }
            }
            if (recordId.toString() != ""){
                RentalRecord recordToDelete = rentalRecordRepository.getReferenceById(recordId);
                recordToDelete.getClient().setCarsReserved(new Car());
                recordToDelete.getCar().setIsReserved(false);
                recordToDelete.getCar().setInUse(false);
                List<String> reservedDates = recordToDelete.getCar().getReservedDates();
                String dateToRemove = recordToDelete.getDateToCollect().toString()+" - "
                        +recordToDelete.getDateToReturn().toString();
                if (reservedDates.contains(dateToRemove)) {
                    int removeId = reservedDates.indexOf(dateToRemove);
                    reservedDates.remove(removeId);
                }
                rentalRecordRepository.deleteById(id);
                if(!rentalRecordRepository.findById(id).isPresent()){
                    return ResponseEntity.accepted().body("Success: Reservation checked out");
                }
                else {
                    return ResponseEntity.unprocessableEntity().body("Error: Failed to checkout reservation");
                }
            }
            else{
                return ResponseEntity.unprocessableEntity().body("Error: Client has not made a reservation");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Client not found");
        }
    }
}
