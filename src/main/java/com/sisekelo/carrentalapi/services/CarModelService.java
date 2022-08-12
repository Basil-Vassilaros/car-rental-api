package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.models.CarModel;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import com.sisekelo.carrentalapi.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarModelService {
    private CarModelRepository carModelRepository;
    private CarRepository carRepository;

    @Autowired
    public CarModelService(CarModelRepository carModelRepository, CarRepository carRepository) {
        this.carModelRepository = carModelRepository;
        this.carRepository = carRepository;
    }

    public CarModel addModel(CarModel carModel){
        return carModelRepository.save(carModel);
    }

    public CarModel getModel(Long id){
        return carModelRepository.getReferenceById(id);
    }

    public List<CarModel> getAllModel(){
        return carModelRepository.findAll();
    }

    @Transactional
    public CarModel updateModelById(Long id, CarModel carModel){
        CarModel updatedModel = carModelRepository.getReferenceById(id);

        updatedModel.setCarModel(carModel.getCarModel());
        updatedModel.setCarManufacturer(carModel.getCarManufacturer());

        return updatedModel;
    }

    public ResponseEntity<Object> deleteModel(Long id) {
        if (carModelRepository.findById(id).isPresent()) {
            /*
                If the Car Model that I wish to delete is referenced to a Car
                then I cannot delete the Car Model until it no longer is referenced
            */
            Boolean isReferenced = false;
            List<Car> listToReference = carRepository.findAll();// the list to see if there are any references
            List<Long> referenceExistList = new ArrayList<Long>();// a list to save the IDs of the entities referencing
            CarModel reference = carModelRepository.findById(id).get();// the entity I want to delete
            for (Car toReference : listToReference) {
                if (toReference.getCarModel() == reference) {
                    isReferenced = true;
                    referenceExistList.add(toReference.getCarId());
                }
            }
            if (!isReferenced) {
                carRepository.deleteById(id);
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed to delete car: Reference to Rental Record (" + referenceExistList + ")");
            }
            if (carModelRepository.findById(id).isPresent()) {
                return ResponseEntity.unprocessableEntity().body("Failed to delete model: Unknown");
            } else {
                return ResponseEntity.ok().body("Success: deleted model");
            }
        } else {
            return ResponseEntity.unprocessableEntity().body("Model not found");
        }
    }
}
