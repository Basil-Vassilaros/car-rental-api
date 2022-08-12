package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.RentalRecord;
import com.sisekelo.carrentalapi.services.RentalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalRecordController {
    private RentalRecordService rentalRecordService;

    @Autowired
    public RentalRecordController(RentalRecordService rentalRecordService) {
        this.rentalRecordService = rentalRecordService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addRentalRecord(@RequestBody final RentalRecord record) {
        return rentalRecordService.addRentalRecord(record);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<RentalRecord> getRentalRecord(@PathVariable Long id) {
        return new ResponseEntity<>(rentalRecordService.getRentalRecord(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RentalRecord>> getAllRentalRecord() {
        return new ResponseEntity<>(rentalRecordService.getAllRentalRecord(), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<RentalRecord> deleteUser(@PathVariable final Long id){
        RentalRecord rentalRecord = rentalRecordService.deleteRecordById(id);
        return new ResponseEntity<>(rentalRecord, HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<RentalRecord> updateUser(@PathVariable final Long id, @RequestBody final RentalRecord rentalRecord){
        RentalRecord updatedUser = rentalRecordService.updateRecordById(id, rentalRecord);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
