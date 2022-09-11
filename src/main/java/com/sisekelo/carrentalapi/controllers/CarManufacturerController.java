package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.tables.CarCategory;
import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import com.sisekelo.carrentalapi.repository.CarManufacturerRepository;
import com.sisekelo.carrentalapi.services.table.CarManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")

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
    public ResponseEntity<Object> addManufacturer(@RequestBody final CarManufacturer carManufacturer) {
        return carManufacturerService.addManufacturer(carManufacturer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteManufacturer(@PathVariable final Long id){
        return carManufacturerService.deleteManufacturer(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateManufacturer(@PathVariable final Long id, @RequestBody final CarManufacturer Manufacturer){
        return carManufacturerService.updateManufacturerById(id, Manufacturer);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getManufacturer(@PathVariable Long id) {
        if (carManufacturerRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Manufacturer not found");
        }
        return new ResponseEntity<>(carManufacturerRepository.getReferenceById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarManufacturer>> getAllManufacturer() {
        return new ResponseEntity<>(carManufacturerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<CarManufacturer>> searchManufacterer(@PathVariable String search) {
        return new ResponseEntity<>(carManufacturerService.searchManufacterer(search), HttpStatus.OK);
    }
}
