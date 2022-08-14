package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.response.Reservation;
import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import com.sisekelo.carrentalapi.repository.RentalRecordRepository;
import com.sisekelo.carrentalapi.services.response.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private ReservationService reservationService;
    private RentalRecordRepository rentalRecordRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, RentalRecordRepository rentalRecordRepository) {
        this.reservationService = reservationService;
        this.rentalRecordRepository = rentalRecordRepository;
    }
    @PostMapping("/add")
    public ResponseEntity<Object> addReservation(@RequestBody final Reservation record) {
        return reservationService.addReservation(record);
    }

    @PostMapping("/book/{id}")
    public ResponseEntity<Object> addBooking(@RequestBody final Long id) {
        return reservationService.addBooking(id);
    }

    @DeleteMapping("/checkout/{id}")
    public ResponseEntity<Object> checkoutReservation(@PathVariable final Long id){
        return reservationService.removeReservation(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateRecord(@PathVariable final Long id, @RequestBody final Reservation record){
        return reservationService.updateRecordByClient(id, record);
    }
    @GetMapping("/details/{id}")
    public ResponseEntity<RentalRecord> getRentalRecord(@PathVariable Long id) {
        return new ResponseEntity<>(rentalRecordRepository.getReferenceById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RentalRecord>> getAllRentalRecord() {
        return new ResponseEntity<>(rentalRecordRepository.findAll(), HttpStatus.OK);
    }
}
