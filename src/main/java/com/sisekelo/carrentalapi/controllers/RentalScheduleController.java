package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.RentalSchedule;
import com.sisekelo.carrentalapi.repository.RentalScheduleRepository;
import com.sisekelo.carrentalapi.services.RentalScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalScheduleController {
    private RentalScheduleService rentalScheduleService;

    @Autowired
    public RentalScheduleController(RentalScheduleService rentalScheduleService) {
        this.rentalScheduleService = rentalScheduleService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addRentalRecord(@RequestBody final RentalSchedule record) {
        return rentalScheduleService.addRentalRecord(record);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<RentalSchedule> getRentalRecord(@PathVariable Long id) {
        return new ResponseEntity<>(rentalScheduleService.getRentalRecord(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RentalSchedule>> getAllRentalRecord() {
        return new ResponseEntity<>(rentalScheduleService.getAllRentalRecord(), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<RentalSchedule> deleteUser(@PathVariable final Long id){
        RentalSchedule rentalSchedule = rentalScheduleService.deleteRecordById(id);
        return new ResponseEntity<>(rentalSchedule, HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<RentalSchedule> updateUser(@PathVariable final Long id, @RequestBody final RentalSchedule rentalSchedule){
        RentalSchedule updatedUser = rentalScheduleService.updateRecordById(id, rentalSchedule);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
