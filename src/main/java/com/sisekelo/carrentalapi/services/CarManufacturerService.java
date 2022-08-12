package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.CarManufacturer;
import com.sisekelo.carrentalapi.repository.CarManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CarManufacturerService {
    private CarManufacturerRepository carManufacturerRepository;

    @Autowired
    public CarManufacturerService(CarManufacturerRepository carManufacturerRepository) {
        this.carManufacturerRepository = carManufacturerRepository;
    }

    public CarManufacturer addManufacturer(CarManufacturer carManufacturer){
        return carManufacturerRepository.save(carManufacturer);
    }

    public CarManufacturer getManufacturer(Long id){
        return carManufacturerRepository.getReferenceById(id);
    }

    public List<CarManufacturer> getAllManufacturer(){
        return carManufacturerRepository.findAll();
    }

    @Transactional
    public CarManufacturer updateManufacturerById(Long id, CarManufacturer carManufacturer){
        CarManufacturer updatedManufacturer = carManufacturerRepository.getReferenceById(id);

        updatedManufacturer.setManufacturer(carManufacturer.getManufacturer());

        return updatedManufacturer;
    }

    public ResponseEntity<Object> deleteManufacturer(Long id){
        if(carManufacturerRepository.findById(id).isPresent()){
            carManufacturerRepository.deleteById(id);
            if(carManufacturerRepository.findById(id).isPresent()){
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
