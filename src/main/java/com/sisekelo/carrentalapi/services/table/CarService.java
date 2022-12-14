package com.sisekelo.carrentalapi.services.table;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.RentalRecord;
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