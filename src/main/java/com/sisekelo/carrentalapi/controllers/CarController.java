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

    // Response Mapping
    // Add new Car only If Car exists & If Car Model exists
    @PostMapping("/add")
    public ResponseEntity<?> addCar(@RequestBody final CarResponse car){
        if (carModelRepository.findById(car.getModelId()).isPresent()){
            return new ResponseEntity<>(carResponseService.addCar(car), HttpStatus.OK);
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Car Model not found");
        }
    }

    // Delete Car only If Car exists & Is Available
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable final Long id){
        if (carRepository.findById(id).isPresent()){
            Car carToDel = carRepository.findById(id).get();
            if (!carToDel.getInUse() || !carToDel.getIsReserved()){
                return new ResponseEntity<>(carResponseService.deleteCar(id), HttpStatus.OK);
            }
            else{
                return ResponseEntity.unprocessableEntity().body("Error: Car is in use");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Car not found");
        }
    }

    // Update Car only If Car exists & If Car Model exists
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCar(@PathVariable final Long id, @RequestBody final CarResponse car){
        if (carRepository.findById(id).isPresent()){
            if (carModelRepository.findById(car.getModelId()).isPresent()){
                return new ResponseEntity<>(carResponseService.updateCar(id, car), HttpStatus.OK);
            }
            else{
                return ResponseEntity.unprocessableEntity().body("Error: Car Model not found");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Car not found");
        }
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

    // Find Car by Search Parameter
    @GetMapping("/all/{search}")
    public ResponseEntity<List<Car>> getAllCars(@PathVariable String search) {
        return new ResponseEntity<>(carService.findCarByPhrase(search), HttpStatus.OK);
    }

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
    }
}