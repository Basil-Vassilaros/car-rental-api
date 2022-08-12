package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.RentalRecord;
import com.sisekelo.carrentalapi.models.Reservation;
import com.sisekelo.carrentalapi.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping("/add")
    public ResponseEntity<Object> addReservation(@RequestBody final Reservation record) {
        return reservationService.addReservation(record);
    }

    @DeleteMapping("/checkout/{id}")
    public ResponseEntity<Object> checkoutReservation(@PathVariable final Long id){
        return reservationService.removeReservation(id);
    }
}
