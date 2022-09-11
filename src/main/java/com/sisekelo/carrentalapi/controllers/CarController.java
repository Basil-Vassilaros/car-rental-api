package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.response.CarResponse;
import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import com.sisekelo.carrentalapi.repository.CarRepository;
import com.sisekelo.carrentalapi.services.response.CarResponseService;
import com.sisekelo.carrentalapi.services.table.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")

@RestController
@RequestMapping("/cars")
public class CarController {
    private CarService carService;

    private CarResponseService carResponseService;

    private CarRepository carRepository;

    private CarModelRepository carModelRepository;

    @Autowired
    public CarController(CarService carService, CarResponseService carResponseService, CarRepository carRepository, CarModelRepository carModelRepository) {
        this.carService = carService;
        this.carResponseService = carResponseService;
        this.carRepository = carRepository;
        this.carModelRepository = carModelRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addCar(@RequestBody final CarResponse car){
        return carResponseService.addCar(car);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable final Long id){
        return carResponseService.deleteCar(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable final Long id, @RequestBody final CarResponse car){
        return carResponseService.updateCar(id, car);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getCar(@PathVariable Long id) {
        if (carRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Car not found");
        }
        return new ResponseEntity<>(carRepository.getReferenceById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(carRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<Car>> search(@PathVariable String search) {
        return new ResponseEntity<>(carResponseService.searchCar(search), HttpStatus.OK);
    }




/*

    // Find Car by Search Parameter


    //Get all available car by ModelId
    @GetMapping("/all/booked/")
    public ResponseEntity<List<Car>> getAllBookedCars() {
        return new ResponseEntity<>(carService.getAllBookedCars(), HttpStatus.OK);
    }

    @GetMapping("/all/booked/{id}")
    public ResponseEntity<List<Car>> getAllBookedCarsByModel(@PathVariable final Long id) {
        return new ResponseEntity<>(carService.getAllBookedCarsByModel(id), HttpStatus.OK);
    }

    //get all reserved cars by ModelId
    @GetMapping("/all/available/{id}")
    public ResponseEntity<List<Car>> getAllAvailableCarsByModel(@PathVariable final Long id) {
        return new ResponseEntity<>(carService.getAllAvailableCarsByModel(id), HttpStatus.OK);
    }

    @GetMapping("/all/available/")
    public ResponseEntity<List<Car>> getAllAvailableCars() {
        return new ResponseEntity<>(carService.getAllAvailableCars(), HttpStatus.OK);
    }

    @GetMapping("/all/reserved/{id}")
    public ResponseEntity<List<Car>> getAllReservedCarsByModel(@PathVariable final Long id) {
        return new ResponseEntity<>(carService.getAllReservedCarsByModel(id), HttpStatus.OK);
    }

    @GetMapping("/all/reserved/")
    public ResponseEntity<List<Car>> getAllReservedCars() {
        return new ResponseEntity<>(carService.getAllReservedCars(), HttpStatus.OK);
    } */
}