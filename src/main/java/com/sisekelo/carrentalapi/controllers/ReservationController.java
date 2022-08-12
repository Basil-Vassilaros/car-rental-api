package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.RentalRecord;
import com.sisekelo.carrentalapi.models.Reservation;
import com.sisekelo.carrentalapi.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ReservationController {
    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping("/reserve")
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
}
