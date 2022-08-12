package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.CarModel;
import com.sisekelo.carrentalapi.models.CarModel;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CarModelService {
    private CarModelRepository carModelRepository;

    @Autowired
    public CarModelService(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
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

    public ResponseEntity<Object> deleteModel(Long id){
        if(carModelRepository.findById(id).isPresent()){
            carModelRepository.deleteById(id);
            if(carModelRepository.findById(id).isPresent()){
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
}
