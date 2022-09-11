package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.response.CarModelResponse;
import com.sisekelo.carrentalapi.models.response.CarResponse;
import com.sisekelo.carrentalapi.models.tables.*;
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

    public ResponseEntity<Object> addCar(CarResponse car){
        // Check if referenced Model exists
        if (carModelRepository.findById(car.getModelId()).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Model not found");
        }
        CarModel refModel = carModelRepository.findById(car.getModelId()).get();

        // Check if new Car is a duplicate
        List<Car> carList = carRepository.findAll();
        for (Car existingCar: carList) {
            if (existingCar.getCarModel().getModelId().equals(car.getModelId()) &&
                    existingCar.getRegistrationNumber().equals(car.getRegistrationNumber()) &&
                    existingCar.getColor().equals(car.getColor()) &&
                    existingCar.getPrice().equals(car.getPrice())){
                return ResponseEntity.unprocessableEntity().body("Error: Specified Car Model already exists");
            }
        }

        // Set values for new Car
        Car newCar = new Car();
        newCar.setCarModel(refModel);
        newCar.setPrice(car.getPrice());
        newCar.setColor(car.getColor());
        newCar.setRegistrationNumber(car.getRegistrationNumber());
        newCar.setInUse(false);
        newCar.setIsReserved(false);
        newCar.setBookedDates("");

        // Save new Car
        carRepository.save(newCar);

        // Success
        return ResponseEntity.accepted().body("Success: Car saved");
    }

    public ResponseEntity<Object> deleteCar(Long id) {
        // Check if referenced Car exists
        if (carRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Car not found");
        }
        Car car = carRepository.findById(id).get();

        // Check if Car is being used
        List<RentalRecord> records = rentalRecordRepository.findAll();
        for (RentalRecord record: records){
            if (record.getCar().equals(car)){
                return ResponseEntity.unprocessableEntity().body("Error: Car is used by Record "+record.getRentalId());
            }
        }

        // Delete Car
        carRepository.deleteById(id);

        // Check if Car was deleted
        if (carRepository.findById(id).isPresent()){
            return ResponseEntity.unprocessableEntity().body("Error: failed to delete Car");
        }

        // Success
        return ResponseEntity.accepted().body("Success: Car deleted");
    }

    @Transactional
    public ResponseEntity<Object> updateCar(Long id, CarResponse car) {
        // Check if referenced Car exists
        if (carRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Car not found");
        }
        Car updateCar = carRepository.getReferenceById(id);

        // Check if referenced Model exists
        if (carModelRepository.findById(car.getModelId()).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Model not found");
        }
        CarModel refModel = carModelRepository.findById(car.getModelId()).get();

        // Check if updated Car is a duplicate
        List<Car> carList = carRepository.findAll();
        for (Car existingCar: carList) {
            if (existingCar.getCarModel().getModelId().equals(car.getModelId()) &&
                    existingCar.getRegistrationNumber().equals(car.getRegistrationNumber()) &&
                    existingCar.getColor().equals(car.getColor()) &&
                    existingCar.getPrice().equals(car.getPrice())){
                return ResponseEntity.unprocessableEntity().body("Error: Specified Car Model already exists");
            }
        }

        // Set values of updated Car
        updateCar.setCarModel(refModel);
        updateCar.setPrice(car.getPrice());
        updateCar.setRegistrationNumber(car.getRegistrationNumber());
        updateCar.setColor(car.getColor());

        // Success
        return ResponseEntity.accepted().body("Success: Car updated");
    }

    public List<Car> searchCar(String search){
        // Initialize our lists
        List<Car> cars = carRepository.findAll();
        List<Car> found = new ArrayList<>();

        // Loop through to find matches
        for (Car car:cars){
            // Make an index to search by
            List<String> index = new ArrayList<>();
            index.add(car.getRegistrationNumber());
            index.add(car.getCarModel().getCarModel());
            index.add(car.getCarModel().getCarManufacturer().getManufacturer());
            index.add(car.getCarModel().getCarCategory().getCarCategory());
            index.add(car.getCarModel().getYear());
            index.add(car.getColor());

            // find matches
            if (index.contains(search)){
                found.add(car);
            }
        }

        // Success
        return found;
    }
}
