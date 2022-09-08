package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.response.CarResponse;
import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import com.sisekelo.carrentalapi.repository.CarRepository;
import com.sisekelo.carrentalapi.repository.RentalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarResponseService {
    private CarRepository carRepository;
    private CarCategoryRepository carCategoryRepository;
    private CarModelRepository carModelRepository;

    private RentalRecordRepository rentalRecordRepository;

    private Boolean missing = false;

    @Autowired
    public CarResponseService(CarRepository carRepository, CarCategoryRepository carCategoryRepository, CarModelRepository carModelRepository, RentalRecordRepository rentalRecordRepository) {
        this.carRepository = carRepository;
        this.carCategoryRepository = carCategoryRepository;
        this.carModelRepository = carModelRepository;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    public List<Car> addCar(CarResponse car){
        Car newCar = new Car();
        newCar.setCarModel(carModelRepository.getReferenceById(car.getModelId()));
        newCar.setPrice(car.getPrice());
        newCar.setRegistrationNumber(car.getRegistrationNumber());
        newCar.setInUse(false);
        newCar.setIsReserved(false);
        newCar.setColor(car.getColor());
        newCar.setBookedDates("");
        carRepository.save(newCar);
        return carRepository.findAll();
    }

    public List<Car> deleteCar(Long id) {
        carRepository.deleteById(id);
        return carRepository.findAll();
    }


    @Transactional
    public List<Car> updateCar(Long id, CarResponse car) {
        Car updateCar = carRepository.getReferenceById(id);
        updateCar.setCarModel(carModelRepository.getReferenceById(car.getModelId()));
        updateCar.setPrice(car.getPrice());
        updateCar.setRegistrationNumber(car.getRegistrationNumber());
        updateCar.setColor(car.getColor());
        return carRepository.findAll();
    }
}
