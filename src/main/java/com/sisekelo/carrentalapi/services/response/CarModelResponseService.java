package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.response.CarModelResponse;
import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import com.sisekelo.carrentalapi.repository.CarManufacturerRepository;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import com.sisekelo.carrentalapi.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CarModelResponseService {
    private CarModelRepository carModelRepository;
    private CarManufacturerRepository carManufacturerRepository;
    private CarRepository carRepository;

    private CarCategoryRepository carCategoryRepository;
    private Boolean missing;

    @Autowired
    public CarModelResponseService(CarModelRepository carModelRepository, CarManufacturerRepository carManufacturerRepository, CarRepository carRepository, CarCategoryRepository carCategoryRepository) {
        this.carModelRepository = carModelRepository;
        this.carManufacturerRepository = carManufacturerRepository;
        this.carRepository = carRepository;
        this.carCategoryRepository = carCategoryRepository;
    }

    public ResponseEntity<Object> addModel(CarModelResponse model){
        CarModel newModel = new CarModel();
        missing = false;
        if (!carManufacturerRepository.findById(model.getManufacturerId()).isPresent()){
            missing = true;
            return ResponseEntity.unprocessableEntity().body("Error: Referenced Car Manufacturer not found");
        }
        if (!carCategoryRepository.findById(model.getCategoryId()).isPresent()){
            missing = true;
            return ResponseEntity.unprocessableEntity().body("Error: Referenced Car Category not found");
        }
        if (!missing){
            newModel.setCarManufacturer(carManufacturerRepository.getReferenceById(model.getManufacturerId()));
            newModel.setCarCategory(carCategoryRepository.getReferenceById(model.getCategoryId()));
            newModel.setCarModel(model.getCarModel());
            newModel.setYear(model.getYear());
            carModelRepository.save(newModel);
            return ResponseEntity.accepted().body("Success: Car Model saved");
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Car Model not saved");
        }
    }

    public ResponseEntity<Object> removeCarModel(Long id) {
        if (carModelRepository.findById(id).isPresent()){
            CarModel deleteModel = carModelRepository.getReferenceById(id);
            Boolean isReferenced = false;
            // checks if car is present in any records
            List<Car> carList = carRepository.findAll();
            for (Car car:carList) {
                if (car.getCarModel() == deleteModel){
                    isReferenced = true;
                }
            };
            // if there is no reference then delete
            if (!isReferenced){
                carModelRepository.deleteById(id);
                if(!carModelRepository.findById(id).isPresent()){
                    return ResponseEntity.accepted().body("Success: Car Model removed");
                }
                else {
                    return ResponseEntity.unprocessableEntity().body("Error: failed to delete Car Model");
                }
            }
            else {
                return ResponseEntity.unprocessableEntity().body("Error: The car model you wish to delete is being used.");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Error: Car Model not found");
        }
    }

    @Transactional
    public ResponseEntity<Object> updateCarModelById(Long id, CarModelResponse model) {
        if (carModelRepository.findById(id).isPresent()) {
            CarModel updateModel = carModelRepository.getReferenceById(id);
            missing = false;
            if (!carManufacturerRepository.findById(model.getManufacturerId()).isPresent()){
                missing = true;
                return ResponseEntity.unprocessableEntity().body("Error: Referenced Car Manufacturer not found");
            }
            if (!carCategoryRepository.findById(model.getCategoryId()).isPresent()){
                missing = true;
                return ResponseEntity.unprocessableEntity().body("Error: Referenced Car Category not found");
            }
            if (!missing){
                updateModel.setCarManufacturer(carManufacturerRepository.getReferenceById(model.getManufacturerId()));
                updateModel.setCarCategory(carCategoryRepository.getReferenceById(model.getCategoryId()));
                updateModel.setCarModel(model.getCarModel());
                updateModel.setYear(model.getYear());
                return ResponseEntity.accepted().body("Success: Car Model updated");
            }
            else{
                return ResponseEntity.unprocessableEntity().body("Error: Car Model not updated");
            }
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Error: Car Model not found");
        }
    }
}
