package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.CarManufacturer;
import com.sisekelo.carrentalapi.services.CarManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manufacturer")
public class CarManufacturerController {
    private CarManufacturerService carManufacturerService;

    @Autowired
    public CarManufacturerController(CarManufacturerService carManufacturerService) {
        this.carManufacturerService = carManufacturerService;
    }

    @PostMapping("/add")
    public ResponseEntity<CarManufacturer> addManufacturer(@RequestBody final CarManufacturer carManufacturer) {
        return new ResponseEntity<>(carManufacturerService.addManufacturer(carManufacturer), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Object> deleteManufacturer(@PathVariable final Long id){
        return carManufacturerService.deleteManufacturer(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<CarManufacturer> updateManufacturer(@PathVariable final Long id, @RequestBody final CarManufacturer Manufacturer){
        CarManufacturer updatedManufacturer = carManufacturerService.updateManufacturerById(id, Manufacturer);
        return new ResponseEntity<>(updatedManufacturer, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<CarManufacturer> getManufacturer(@PathVariable Long id) {
        return new ResponseEntity<>(carManufacturerService.getManufacturer(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarManufacturer>> getAllManufacturer() {
        return new ResponseEntity<>(carManufacturerService.getAllManufacturer(), HttpStatus.OK);
    }
}
