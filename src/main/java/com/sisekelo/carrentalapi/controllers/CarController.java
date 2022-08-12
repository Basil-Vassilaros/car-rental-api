package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/add")
    public ResponseEntity<Car> addCar(@RequestBody final Car car) {
        return new ResponseEntity<>(carService.addCar(car), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable final Long id){
        return carService.deleteCar(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Car> updateUser(@PathVariable final Long id, @RequestBody final Car car){
        Car updatedUser = carService.updateCarById(id, car);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        return new ResponseEntity<>(carService.getCar(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
    }

    @GetMapping("/all/available/{id}")
    public ResponseEntity<List<Car>> getAllAvailableCars(@PathVariable final Long id) {
        if(id > -1){
            return new ResponseEntity<>(carService.getAllAvailableCarsByModel(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(carService.getAllAvailableCars(), HttpStatus.OK);
        }
    }
    @GetMapping("/all/reserved/{id}")
    public ResponseEntity<List<Car>> getAllReservedCars(@PathVariable final Long id) {
        if(id > -1){
            return new ResponseEntity<>(carService.getAllReservedCarsByModel(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(carService.getAllReservedCars(), HttpStatus.OK);
        }
    }
    @GetMapping("/all/booked/{id}")
    public ResponseEntity<List<Car>> getAllBookedCars(@PathVariable final Long id) {
        if(id > -1){
            return new ResponseEntity<>(carService.getAllBookedCarsByModel(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(carService.getAllBookedCars(), HttpStatus.OK);
        }
    }

}