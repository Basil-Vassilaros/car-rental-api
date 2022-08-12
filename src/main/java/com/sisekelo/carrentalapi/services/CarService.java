package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.models.RentalRecord;
import com.sisekelo.carrentalapi.repository.CarRepository;
import com.sisekelo.carrentalapi.repository.RentalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;
    private RentalRecordRepository rentalRecordRepository;

    @Autowired
    public CarService(CarRepository carRepository, RentalRecordRepository rentalRecordRepository) {
        this.carRepository = carRepository;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    public Car addCar(Car car){
        car.setIsReserved(false);
        car.setInUse(false);
        return carRepository.save(car);
    }

    public ResponseEntity<Object> deleteCar (Long id) {
        if (carRepository.findById(id).isPresent()) {
            /*
                If the Car that I wish to delete is referenced to a Rental Record
                then I cannot delete the Car until it no longer is referenced
            */
            Boolean isReferenced = false;
            List<RentalRecord> listToReference = rentalRecordRepository.findAll();// the list to see if there are any references
            List<Long> referenceExistList = new ArrayList<Long>();// a list to save the IDs of the entities referencing
            Car reference = carRepository.findById(id).get();// the entity I want to delete
            for (RentalRecord toReference : listToReference) {
                if (toReference.getCar() == reference) {
                    isReferenced = true;
                    referenceExistList.add(toReference.getRentalId());
                }
            }
            if (!isReferenced) {
                carRepository.deleteById(id);
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed to delete car: Reference to Rental Record (" + referenceExistList + ")");
            }
            if (carRepository.findById(id).isPresent()) {
                return ResponseEntity.unprocessableEntity().body("Failed to delete car: Unknown");
            } else {
                return ResponseEntity.ok().body("Success: deleted car");
            }
        } else {
            return ResponseEntity.unprocessableEntity().body("Car not found");
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