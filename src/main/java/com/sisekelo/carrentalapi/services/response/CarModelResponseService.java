package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.exceptions.NotFoundException;
import com.sisekelo.carrentalapi.models.exceptions.ReferenceExistException;
import com.sisekelo.carrentalapi.models.response.CarModelResponse;
import com.sisekelo.carrentalapi.models.tables.Car;
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
import java.util.ArrayList;
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
        // Check if referenced manufacturer exists
        if (carManufacturerRepository.findById(model.getManufacturerId()).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Manufacturer not found");
        }
        CarManufacturer refMan = carManufacturerRepository.findById(model.getManufacturerId()).get();

        // Check if referenced category exists
        if (carCategoryRepository.findById(model.getCategoryId()).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Category not found");
        }
        CarCategory refCat = carCategoryRepository.findById(model.getCategoryId()).get();

        // Check if new model is a duplicate
        List<CarModel> modelList = carModelRepository.findAll();
        for (CarModel carModel: modelList) {
            if (carModel.getCarModel().equals(model.getCarModel()) &&
                    carModel.getCarCategory().getCategoryId().equals(model.getCategoryId()) &&
                    carModel.getCarManufacturer().getManufacturerId().equals(model.getManufacturerId()) &&
                    carModel.getYear().equals(model.getYear())){
                return ResponseEntity.unprocessableEntity().body("Error: Specified Car Model already exists");
            }
        }

        // Set values for new model
        CarModel newModel = new CarModel();
        newModel.setCarManufacturer(refMan);
        newModel.setCarCategory(refCat);
        newModel.setCarModel(model.getCarModel());
        newModel.setYear(model.getYear());

        // Save new model
        carModelRepository.save(newModel);

        // Success
        return ResponseEntity.accepted().body("Success: Car Model saved");
    }

    @Transactional
    public ResponseEntity<Object> updateCarModelById(Long id, CarModelResponse model) {
        //Check if referenced Car Model exists
        if (carModelRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Model not found");
        }
        CarModel updateModel = carModelRepository.findById(id).get();

        // Check if referenced manufacturer exists
        if (carManufacturerRepository.findById(model.getManufacturerId()).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Manufacturer not found");
        }
        CarManufacturer refMan = carManufacturerRepository.findById(model.getManufacturerId()).get();

        // Check if referenced category exists
        if (carCategoryRepository.findById(model.getCategoryId()).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Category not found");
        }
        CarCategory refCat = carCategoryRepository.findById(model.getCategoryId()).get();

        // Check if updated Model is a duplicate
        List<CarModel> modelList = carModelRepository.findAll();
        for (CarModel carModel: modelList) {
            if (carModel.getCarModel().equals(model.getCarModel()) &&
                    carModel.getCarCategory().getCategoryId().equals(model.getCategoryId()) &&
                    carModel.getCarManufacturer().getManufacturerId().equals(model.getManufacturerId()) &&
                    carModel.getYear().equals(model.getYear())){
                return ResponseEntity.unprocessableEntity().body("Error: Specified Car Model already exists");
            }
        }

        // Set values for updated Model
        updateModel.setCarManufacturer(refMan);
        updateModel.setCarCategory(refCat);
        updateModel.setCarModel(model.getCarModel());
        updateModel.setYear(model.getYear());

        // Success
        return ResponseEntity.accepted().body("Success: Car Model updated");
    }

    public ResponseEntity<Object> removeCarModel(Long id) {
        //Check if referenced Car Model exists
        if (carRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Model not found");
        }
        CarModel modelDelete = carModelRepository.findById(id).get();

        // Check if Car Model is being used
        List<Car> carList = carRepository.findAll();
        for (Car car : carList){
            if (car.getCarModel().equals(modelDelete)){
                return ResponseEntity.unprocessableEntity().body("Error: Model is used by Car "+car.getCarId());
            }
        }

        // Delete Car Model
        carModelRepository.deleteById(id);

        // Check if it did delete
        if (carModelRepository.findById(id).isPresent()){
            return ResponseEntity.unprocessableEntity().body("Error: failed to delete Car Model");
        }

        // Success
        return ResponseEntity.accepted().body("Success: Car Model removed");
    }

    public List<CarModel> searchModel(String search){
        // Initialize our lists
        List<CarModel> models = carModelRepository.findAll();
        List<CarModel> found = new ArrayList<>();

        // Loop through to find matches
        for (CarModel model:models){
            // Make an index to search by
            List<String> index = new ArrayList<>();
            index.add(model.getCarModel());
            index.add(model.getCarManufacturer().getManufacturer());
            index.add(model.getCarCategory().getCarCategory());
            index.add(model.getYear());

            // find matches
            if (index.contains(search)){
                found.add(model);
            }
        }

        // Success
        return found;
    }
}
