package com.sisekelo.carrentalapi.services.table;

import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import com.sisekelo.carrentalapi.repository.CarManufacturerRepository;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarManufacturerService {
    private CarManufacturerRepository carManufacturerRepository;
    private CarModelRepository carModelRepository;

    @Autowired
    public CarManufacturerService(CarManufacturerRepository carManufacturerRepository, CarModelRepository carModelRepository) {
        this.carManufacturerRepository = carManufacturerRepository;
        this.carModelRepository = carModelRepository;
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
            /*
                If the Car Manufacturer that I wish to delete is referenced to a Car Model
                then I cannot delete the Car Manufacturer until it no longer is referenced
            */
            Boolean isReferenced = false;
            List<CarModel> listToReference = carModelRepository.findAll();// the list to see if there are any references
            List<Long> referenceExistList = new ArrayList<Long>();// a list to save the IDs of the entities referencing
            CarManufacturer reference = carManufacturerRepository.findById(id).get();// the entity I want to delete
            for (CarModel toReference : listToReference) {
                if (toReference.getCarManufacturer() == reference) {
                    isReferenced = true;
                    referenceExistList.add(toReference.getModelId());
                }
            }
            if (!isReferenced) {
                carModelRepository.deleteById(id);
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed to delete manufacturer: Reference to Car Model (" + referenceExistList + ")");
            }
            if(carManufacturerRepository.findById(id).isPresent()){
                return ResponseEntity.unprocessableEntity().body("Failed to delete manufacturer: Unknown");
            }
            else {
                return ResponseEntity.ok().body("Success: deleted manufacturer");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Manufacturer not found");
        }
    }
}
