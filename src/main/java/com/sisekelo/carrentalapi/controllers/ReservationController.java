package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.response.Reservation;
import com.sisekelo.carrentalapi.models.tables.Client;
import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import com.sisekelo.carrentalapi.repository.RentalRecordRepository;
import com.sisekelo.carrentalapi.services.response.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")
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
        return reservationService.addNewRecord(record);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Object> removeRecord(@PathVariable final Long id){
        return reservationService.removeBooking(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateRecord(@PathVariable final Long id, @RequestBody final Reservation record){
        return reservationService.updateBooking(id, record);
    }
    @GetMapping("/details/{id}")
    public ResponseEntity<?> getRentalRecord(@PathVariable Long id) {
        if (rentalRecordRepository.findById(id).isEmpty()){
            return ResponseEntity.badRequest().body("Error: Record not found");
        }
        return new ResponseEntity<>(rentalRecordRepository.getReferenceById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RentalRecord>> getAllRentalRecord() {
        return new ResponseEntity<>(rentalRecordRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<RentalRecord>> search(@PathVariable String search) {
        return new ResponseEntity<>(reservationService.searchRecord(search), HttpStatus.OK);
    }
}
