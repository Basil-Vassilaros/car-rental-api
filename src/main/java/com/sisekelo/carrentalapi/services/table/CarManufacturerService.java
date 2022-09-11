package com.sisekelo.carrentalapi.services.table;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.CarCategory;
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

    public ResponseEntity<Object> addManufacturer(CarManufacturer carManufacturer){
        // Check if new Manufacturer is a duplicate
        List<CarManufacturer> carManufacturerList = carManufacturerRepository.findAll();
        for (CarManufacturer manufacturer: carManufacturerList) {
            if (manufacturer.getManufacturer().equals(carManufacturer.getManufacturer())){
                return ResponseEntity.unprocessableEntity().body("Error: Manufacturer already exists");
            }
        }
        carManufacturerRepository.save(carManufacturer);
        return ResponseEntity.ok().body("Success: added manufacturer");
    }

    @Transactional
    public ResponseEntity<Object> updateManufacturerById(Long id, CarManufacturer carManufacturer){
        // Check if Ref Manufacturer exists
        if (carManufacturerRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Manufacturer not found");
        }
        CarManufacturer updatedManufacturer = carManufacturerRepository.getReferenceById(id);

        // Check if updated Manufacturer is a duplicate
        List<CarManufacturer> carManufacturerList = carManufacturerRepository.findAll();
        for (CarManufacturer manufacturer: carManufacturerList) {
            if (manufacturer.getManufacturer().equals(carManufacturer.getManufacturer()) && manufacturer.getManufacturerId() != id){
                return ResponseEntity.unprocessableEntity().body("Error: Manufacturer already exists");
            }
        }

        // Update the manufacturer
        updatedManufacturer.setManufacturer(carManufacturer.getManufacturer());
        return ResponseEntity.ok().body("Success: updated manufacturer");

    }

    public ResponseEntity<Object> deleteManufacturer(Long id){
        // Check if Ref Manufacturer exists
        if (carManufacturerRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Manufacturer not found");
        }
        CarManufacturer manufacturerToDelete = carManufacturerRepository.getReferenceById(id);

        // Check if Manufacturer is being used
        List<CarModel> carModelList = carModelRepository.findAll();
        for (CarModel model : carModelList) {
            if (model.getCarManufacturer() == manufacturerToDelete) {
                return ResponseEntity.unprocessableEntity().body("Error: Manufacturer in use by Car Model "+model.getCarModel());
            }
        }

        // Delete the manufacturer
        carManufacturerRepository.deleteById(id);

        // Check if the Manufacturer is deleted
        if (carManufacturerRepository.findById(id).isPresent()){
            return ResponseEntity.unprocessableEntity().body("Error: failed to delete Manufacturer");
        }

        return ResponseEntity.ok().body("Success: deleted manufacturer");
    }

    public List<CarManufacturer> searchManufacterer(String search){
        // Initialize our lists
        List<CarManufacturer> manufacturers = carManufacturerRepository.findAll();
        List<CarManufacturer> found = new ArrayList<>();

        // Loop through to find matches
        for (CarManufacturer manufacturer:manufacturers){
            if (manufacturer.getManufacturer().contains(search)){
                found.add(manufacturer);
            }
        }

        // Success
        return found;
    }
}
