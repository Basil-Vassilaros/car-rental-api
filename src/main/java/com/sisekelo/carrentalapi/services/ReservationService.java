package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.models.Client;
import com.sisekelo.carrentalapi.models.RentalRecord;
import com.sisekelo.carrentalapi.models.Reservation;
import com.sisekelo.carrentalapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
        if (!carRepository.findById(reservation.getCarId()).isPresent()){
            missing = true;
            return ResponseEntity.unprocessableEntity().body("Failed: Car not found");
        }
        if (!clientRepository.findById(reservation.getClientId()).isPresent()){
            missing = true;
            return ResponseEntity.unprocessableEntity().body("Failed: Client not found");
        }
        if (!missing){
            newRecord.setCar(carRepository.getReferenceById(reservation.getCarId()));
            newRecord.setClient(clientRepository.getReferenceById(reservation.getClientId()));
            newRecord.setCollectionDate(reservation.getCollectionDate());
            newRecord.setReservationDate(LocalDateTime.now());
            newRecord.setReturnDate(reservation.getReturnDate());

            Car carReserved = carRepository.getReferenceById(reservation.getCarId());
            carReserved.setIsReserved(true);

            clientRepository.getReferenceById(reservation.getClientId()).setCarsReserved(carReserved);

            rentalRecordService.addRentalRecord(newRecord);

        }
        if(rentalRecordRepository.findById(newRecord.getRentalId()).isPresent()){
            return ResponseEntity.accepted().body("Success: recorded reservation");
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Failed to record reservation");
        }
    }

    public ResponseEntity<Object> addBooking(Long id){
        if (rentalRecordRepository.findById(id).isPresent()) {
            RentalRecord record = rentalRecordRepository.getReferenceById(id);
            record.getCar().setInUse(true);
            record.getCar().setIsReserved(false);
            record.setCollectionDate(LocalDateTime.now());
            return ResponseEntity.accepted().body("Success: Car is booked in use");
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Record not found");
        }
    }

    public ResponseEntity<Object> removeReservation(Long id) {
        if (rentalRecordRepository.findById(id).isPresent()){
            RentalRecord recordToDelete = rentalRecordRepository.getReferenceById(id);
            recordToDelete.getClient().setCarsReserved(new Car());
            recordToDelete.getCar().setIsReserved(false);
            recordToDelete.getCar().setInUse(false);
            rentalRecordService.deleteRecordById(id);
            if(!rentalRecordRepository.findById(id).isPresent()){
                return ResponseEntity.accepted().body("Success: Reservation checked out");
            }
            else {
                return ResponseEntity.unprocessableEntity().body("Failed to checkout reservation");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Record not found");
        }

    }
}
