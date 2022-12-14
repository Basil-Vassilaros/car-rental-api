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

    //Response Mapping
    @PostMapping("/add")
    public ResponseEntity<Object> addCar(@RequestBody final CarResponse car){
        return carResponseService.addCar(car);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable final Long id){
        return carResponseService.removeCar(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable final Long id, @RequestBody final CarResponse car){
       return carResponseService.updateCarById(id, car);
    }

    //Get Mapping
    @GetMapping("/details/{id}")
    public ResponseEntity<?> getCar(@PathVariable Long id) {
        if (carRepository.findById(id).isPresent()){
            return new ResponseEntity<>(carRepository.getReferenceById(id), HttpStatus.OK);
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Car not found");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(carRepository.findAll(), HttpStatus.OK);
    }

    //Get all available car by ModelId
    @GetMapping("/all/available/{id}")
    public ResponseEntity<List<Car>> getAllAvailableCars(@PathVariable final Long id) {
        if(id.toString() != ""){
            if (carModelRepository.findById(id).isPresent()){
                return new ResponseEntity<>(carService.getAllAvailableCarsByModel(id), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        else{
            return new ResponseEntity<>(carService.getAllAvailableCars(), HttpStatus.OK);
        }
    }

    //get all reserved cars by ModelId
    @GetMapping("/all/reserved/{id}")
    public ResponseEntity<List<Car>> getAllReservedCars(@PathVariable final Long id) {
        if(id.toString() != ""){
            if (carModelRepository.findById(id).isPresent()){
                return new ResponseEntity<>(carService.getAllReservedCarsByModel(id), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        else{
            return new ResponseEntity<>(carService.getAllReservedCars(), HttpStatus.OK);
        }
    }

    //get all booked cars by ModelId
    @GetMapping("/all/booked/{id}")
    public ResponseEntity<List<Car>> getAllBookedCars(@PathVariable final Long id) {
        if(id.toString() != ""){
            if (carModelRepository.findById(id).isPresent()){
                return new ResponseEntity<>(carService.getAllBookedCarsByModel(id), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        else{
            return new ResponseEntity<>(carService.getAllBookedCars(), HttpStatus.OK);
        }
    }

}