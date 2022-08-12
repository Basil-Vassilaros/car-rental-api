package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.models.Client;
import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car addCar(Car car){
        car.setIsReserved(false);
        car.setInUse(false);
        return carRepository.save(car);
    }

    public ResponseEntity<Object> deleteCar (Long id) {
        if(carRepository.findById(id).isPresent()){
            carRepository.deleteById(id);
            if(carRepository.findById(id).isPresent()){
                return ResponseEntity.unprocessableEntity().body("Failed to delete the specific car");
            }
            else {
                return ResponseEntity.ok().body("Successfully deleted specific car");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("No Car Found");
        }
    }

    @Transactional
    public Car updateCarById(Long id, Car car){
        Car updatedCar = carRepository.getReferenceById(id);
        updatedCar.setCarCategory(car.getCarCategory());
        updatedCar.setCarModel(car.getCarModel());
        updatedCar.setRegistrationNumber(car.getRegistrationNumber());
        updatedCar.setInUse(car.getInUse());
        updatedCar.setIsReserved(car.getIsReserved());
        updatedCar.setPrice(car.getPrice());
        return updatedCar;
    }

    //NORMAL GETS
    public Car getCar(Long id){
        if(carRepository.findById(id).isPresent()){
            Car car = carRepository.findById(id).get();
            return car;
        }
        else{
            return null;
        }
    }

    public List<Car> getAllCars(){
        List<Car> cars = carRepository.findAll();
        return cars;
    }

    public List<Car> getAllAvailableCars(){
        List<Car> cars = carRepository.findAll();
        List<Car> availableCars = new ArrayList<Car>();
        for (Car car: cars) {
            if (!car.getIsReserved() || !car.getInUse()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public List<Car> getAllReservedCars(){
        List<Car> cars = carRepository.findAll();
        List<Car> reservedCars = new ArrayList<Car>();
        for (Car car: cars) {
            if (car.getIsReserved()) {
                reservedCars.add(car);
            }
        }
        return reservedCars;
    }

    public List<Car> getAllBookedCars(){
        List<Car> cars = carRepository.findAll();
        List<Car> bookedCars = new ArrayList<Car>();
        for (Car car: cars) {
            if (car.getInUse()) {
                bookedCars.add(car);
            }
        }
        return bookedCars;
    }

    //GET BY MODELS
    public List<Car> getAllAvailableCarsByModel(Long modelId){
        List<Car> cars = carRepository.findAll();
        List<Car> availableCars = new ArrayList<Car>();
        for (Car car: cars) {
            if ((!car.getIsReserved() || !car.getInUse()) && car.getCarModel().getModelId() == modelId ) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public List<Car> getAllReservedCarsByModel(Long modelId){
        List<Car> cars = carRepository.findAll();
        List<Car> reservedCars = new ArrayList<Car>();
        for (Car car: cars) {
            if (car.getIsReserved() && car.getCarModel().getModelId() == modelId) {
                reservedCars.add(car);
            }
        }
        return reservedCars;
    }

    public List<Car> getAllBookedCarsByModel(Long modelId){
        List<Car> cars = carRepository.findAll();
        List<Car> bookedCars = new ArrayList<Car>();
        for (Car car: cars) {
            if (car.getInUse() && car.getCarModel().getModelId() == modelId ) {
                bookedCars.add(car);
            }
        }
        return bookedCars;
    }
}