package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import com.sisekelo.carrentalapi.repository.CarManufacturerRepository;
import com.sisekelo.carrentalapi.services.table.CarManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manufacturer")
public class CarManufacturerController {
    private CarManufacturerService carManufacturerService;
    private CarManufacturerRepository carManufacturerRepository;

    @Autowired
    public CarManufacturerController(CarManufacturerService carManufacturerService, CarManufacturerRepository carManufacturerRepository) {
        this.carManufacturerService = carManufacturerService;
        this.carManufacturerRepository = carManufacturerRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<CarManufacturer> addManufacturer(@RequestBody final CarManufacturer carManufacturer) {
        return new ResponseEntity<>(carManufacturerService.addManufacturer(carManufacturer), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteManufacturer(@PathVariable final Long id){
        return carManufacturerService.deleteManufacturer(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CarManufacturer> updateManufacturer(@PathVariable final Long id, @RequestBody final CarManufacturer Manufacturer){
        CarManufacturer updatedManufacturer = carManufacturerService.updateManufacturerById(id, Manufacturer);
        return new ResponseEntity<>(updatedManufacturer, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getManufacturer(@PathVariable Long id) {
        if (carManufacturerRepository.findById(id).isPresent()){
            return new ResponseEntity<>(carManufacturerRepository.getReferenceById(id), HttpStatus.OK);
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Manufacturer not found");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarManufacturer>> getAllManufacturer() {
        return new ResponseEntity<>(carManufacturerRepository.findAll(), HttpStatus.OK);
    }
}
