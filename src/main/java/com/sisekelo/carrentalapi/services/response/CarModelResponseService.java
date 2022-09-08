package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.exceptions.NotFoundException;
import com.sisekelo.carrentalapi.models.exceptions.ReferenceExistException;
import com.sisekelo.carrentalapi.models.response.CarModelResponse;
import com.sisekelo.carrentalapi.models.tables.CarCategory;
import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import com.sisekelo.carrentalapi.repository.CarManufacturerRepository;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import com.sisekelo.carrentalapi.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    public CarModel findModelById(Long id){
        return carModelRepository.findById(id).orElseThrow( ()
                -> new NotFoundException(id, "model"));
    }

    public CarModel addModel(CarModelResponse model){
        CarModel newModel = new CarModel();

        CarManufacturer refMan = carManufacturerRepository.findById(model.getManufacturerId()).orElseThrow(() ->
                new NotFoundException(model.getManufacturerId(), "manufacturer"));
        CarCategory refCat = carCategoryRepository.findById(model.getCategoryId()).orElseThrow(() ->
                new NotFoundException(model.getCategoryId(), "category"));

        newModel.setCarManufacturer(refMan);
        newModel.setCarCategory(refCat);
        newModel.setCarModel(model.getCarModel());
        newModel.setYear(model.getYear());

        return carModelRepository.save(newModel);
    }

    public ResponseEntity<Object> removeCarModel(Long id) {
        CarModel modelDelete = carModelRepository.findById(id).orElseThrow( ()
                -> new NotFoundException(id, "model"));
        if (carRepository.findAll().contains(modelDelete)){
            throw new ReferenceExistException(id, "model", "car");
        }
        else{
            carModelRepository.deleteById(id);
            if(!carModelRepository.findById(id).isPresent()){
                return ResponseEntity.accepted().body("Success: Car Model removed");
            }
            else {
                return ResponseEntity.unprocessableEntity().body("Error: failed to delete Car Model");
            }
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
