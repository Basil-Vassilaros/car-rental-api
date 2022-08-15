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
        Car newCar = new Car();
        missing = false;
        if (!carModelRepository.findById(car.getModelId()).isPresent()){
            missing = true;
            return ResponseEntity.unprocessableEntity().body("Error: Referenced Car Model not found");
        }
        if (!missing){
            newCar.setCarModel(carModelRepository.getReferenceById(car.getModelId()));
            newCar.setPrice(car.getPrice());
            newCar.setRegistrationNumber(car.getRegistrationNumber());
            newCar.setInUse(false);
            newCar.setIsReserved(false);
            newCar.setColor(car.getColor());
            carRepository.save(newCar);
            return ResponseEntity.accepted().body("Success: Car saved");
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Car not saved");
        }
    }

    public ResponseEntity<Object> removeCar(Long id) {
        if (carRepository.findById(id).isPresent()){
            Car deleteCar = carRepository.getReferenceById(id);
            Boolean isReferenced = false;
            // checks if car is present in any records
            List<RentalRecord> recordList = rentalRecordRepository.findAll();
            for (RentalRecord record:recordList) {
                if (record.getCar() == deleteCar){
                    isReferenced = true;
                }
            };
            // if there is no reference then delete
            if (!isReferenced){
                carRepository.deleteById(id);
                if(!carRepository.findById(id).isPresent()){
                    return ResponseEntity.accepted().body("Success: Car removed");
                }
                else {
                    return ResponseEntity.unprocessableEntity().body("Error: failed to delete Car");
                }
            }
            else {
                return ResponseEntity.unprocessableEntity().body("Error: The car you wish to delete is being used.");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Car not found");
        }
    }

    @Transactional
    public ResponseEntity<Object> updateCarById(Long id, CarResponse car) {
        if (carRepository.findById(id).isPresent()) {
            Car updateCar = carRepository.getReferenceById(id);
            missing = false;
            if (!carModelRepository.findById(car.getModelId()).isPresent()) {
                missing = true;
                return ResponseEntity.unprocessableEntity().body("Error: Referenced Car Model not found");
            }
            if (!missing) {
                updateCar.setCarModel(carModelRepository.getReferenceById(car.getModelId()));
                updateCar.setPrice(car.getPrice());
                updateCar.setRegistrationNumber(car.getRegistrationNumber());
                updateCar.setColor(car.getColor());

                return ResponseEntity.accepted().body("Success: Car updated");
            }
            else{
                return ResponseEntity.unprocessableEntity().body("Error: Car not updated");
            }
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Error: Car not found");
        }
    }
}
