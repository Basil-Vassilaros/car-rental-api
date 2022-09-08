package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.exceptions.NotFoundException;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");



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

    // Method to calculate the total price of the booking
    // Total price = price * (returnDate - collectDate)
    public Float calcTotalPrice(Float price, LocalDate dateToCollect, LocalDate dateToReturn){
        return price * (dateToReturn.toEpochDay() - dateToCollect.toEpochDay());
    }

    // To Find booking conflicts
    public String getPreBookedDates(Car car, LocalDate dateToCollect, LocalDate dateToReturn){
        String[] bookedDates = car.getBookedDates().split(", ");
        String prebookedDates = "";
        for (String bookedDate: bookedDates) {
            if (bookedDate != ""){
                String[] dates = bookedDate.split("-");
                if (dates.length > 0 ){
                    LocalDate bookedDateCollect = LocalDate.parse(dates[0], formatter);
                    LocalDate bookedDateReturn = LocalDate.parse(dates[1], formatter);
                    if ((dateToCollect.isAfter(bookedDateCollect) && dateToCollect.isBefore(bookedDateReturn)) ||
                            (dateToReturn.isBefore(bookedDateReturn) && dateToReturn.isAfter(bookedDateCollect))){
                        prebookedDates = prebookedDates + dates[0] + "-" + dates[1]+", ";
                    }
                }
            }
        }
        return prebookedDates;
    }

    public ResponseEntity<Object> addNewRecord(Reservation reservation){

        // Checks if Referenced Client exists
        if(!(clientRepository.findById(reservation.getClientId()).isPresent())){
            return ResponseEntity.unprocessableEntity().body(
                    "Error: Referenced Client not found");
        }
        Client client = clientRepository.getReferenceById(reservation.getClientId());

        // Checks if Referenced Car exists
        if(!(carRepository.findById(reservation.getCarId()).isPresent())){
            return ResponseEntity.unprocessableEntity().body(
                    "Error: Referenced Car not found");
        }
        Car car = carRepository.getReferenceById(reservation.getCarId());

        // Checks if Collect Data is before Return Date
        LocalDate dateToCollect = LocalDate.parse(reservation.getDateToCollect(), formatter);
        LocalDate dateToReturn = LocalDate.parse(reservation.getDateToReturn(), formatter);
        if (dateToCollect.isAfter(dateToReturn)){
            return ResponseEntity.unprocessableEntity().body(
                    "Error: Collect Date is must be before Return Date");
        }

       // Checks if Referenced Car is already booked
        if (!(car.getBookedDates().isEmpty())){
            String prebookedDates = getPreBookedDates(car, dateToCollect, dateToReturn);
            if (prebookedDates != "") {
                return ResponseEntity.unprocessableEntity().body(
                        "Error: Car is unavailable - Booking conflicts: "+prebookedDates);
            }
        }

        // Get Total price of booking
        Float totalPrice = calcTotalPrice(car.getPrice(),dateToCollect, dateToReturn);

        // Set the Car to reserved, save the dates it has been reserved & for the client
        car.setIsReserved(true);

        String carBookedDates = car.getBookedDates() + ", "+reservation.getDateToCollect()+"-"+reservation.getDateToReturn();
        car.setBookedDates(carBookedDates);

        // Save the new Record
        RentalRecord newRecord = new RentalRecord();
        newRecord.setCar(car);
        newRecord.setClient(client);
        newRecord.setDateReservationMade(LocalDate.now());
        newRecord.setDateToCollect(dateToCollect);
        newRecord.setDateToReturn(dateToReturn);
        newRecord.setTotalPrice(totalPrice);
        rentalRecordRepository.save(newRecord);

        return ResponseEntity.accepted().body("Success: Record saved");
    }

    public ResponseEntity<Object> removeBooking(Long id){
        RentalRecord record = rentalRecordRepository.findById(id).orElseThrow( ()
                -> new NotFoundException(id, "Record"));
        Car car = record.getCar();
        Client client = record.getClient();

        // Find record Dates in Car Dates and remove them
        String carBookingDates = car.getBookedDates();
        String datesToRemove = ", "+record.getDateToCollect().toString() +"-"+record.getDateToReturn();
        carBookingDates.replace(datesToRemove, "");
        car.setBookedDates(carBookingDates);

        // Check if things where removed
        if (car.getBookedDates().contains(datesToRemove)){
            return ResponseEntity.unprocessableEntity().body(
                    "Error: Failed to remove booking dates from Car "+car.getCarId());
        }

        car.setIsReserved(false);
        car.setInUse(false);

        rentalRecordRepository.deleteById(id);

        if (rentalRecordRepository.findById(id).isPresent()){
            return ResponseEntity.unprocessableEntity().body(
                    "Error: Failed to remove record "+record.getRentalId());
        }else{
            return ResponseEntity.accepted().body("Success: Record Removed");
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
                recordToDelete.getClient().setReservedCars("");
                recordToDelete.getCar().setIsReserved(false);
                recordToDelete.getCar().setInUse(false);

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
                //convert String to LocalDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                String dateCollect = reservation.getDateToCollect();
                String dateReturn = reservation.getDateToReturn();

                LocalDate localDateCollect = LocalDate.parse(dateCollect, formatter);
                LocalDate localDateReturn = LocalDate.parse(dateReturn, formatter);

                for (RentalRecord record : recordList) {
                    // cant be booked before return and cant be returned after booked
                    if (localDateCollect.isBefore(record.getDateToReturn()) ||
                            localDateReturn.isAfter(record.getDateToCollect())) {
                        problem = true;
                        return ResponseEntity.unprocessableEntity().body("Error: Car is already booked between"
                                + record.getDateToCollect().toString() + " and " + record.getDateToReturn().toString());
                    }
                    if (record.getClient() == client) {
                        recordId = record.getRentalId();
                    }
                }
                if (localDateReturn.isBefore(localDateCollect)) {
                    problem = true;
                    return ResponseEntity.unprocessableEntity().body("Error: Return date must be after collection date");
                }
                if (recordId.toString() != ""){
                    RentalRecord updatedRecord = rentalRecordRepository.getReferenceById(recordId);
                    updatedRecord.setCar(carRepository.getReferenceById(reservation.getCarId()));
                    updatedRecord.setClient(clientRepository.getReferenceById(reservation.getClientId()));

                    updatedRecord.setDateReservationMade(LocalDate.now());
                    updatedRecord.setTotalPrice(rentalRecordService.getTotalPrice(updatedRecord.getCar().getPrice(),
                            updatedRecord.getDateToReturn(),
                            updatedRecord.getDateToCollect()));
                    Car carReserved = carRepository.getReferenceById(reservation.getCarId());
                    carReserved.setIsReserved(true);



                    updatedRecord.setDateToReturn(localDateCollect);
                    updatedRecord.setDateToCollect(localDateReturn);

                   // clientRepository.getReferenceById(reservation.getClientId()).setCarsReserved(reservation.getCarId());
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


}
