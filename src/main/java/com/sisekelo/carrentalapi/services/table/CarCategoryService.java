package com.sisekelo.carrentalapi.services.table;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.CarCategory;
import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarCategoryService {
    private CarCategoryRepository carCategoryRepository;
    private CarModelRepository carModelRepository;

    @Autowired
    public CarCategoryService(CarCategoryRepository carCategoryRepository, CarModelRepository carModelRepository) {
        this.carCategoryRepository = carCategoryRepository;
        this.carModelRepository = carModelRepository;
    }

    public ResponseEntity<Object>  addCategory(CarCategory carCategory){
        // Checks for if new Category is a duplicate
        List<CarCategory> carCategoryList = carCategoryRepository.findAll();
        for (CarCategory category: carCategoryList) {
            if (category.getCarCategory().equals(carCategory.getCarCategory())){
                return ResponseEntity.unprocessableEntity().body("Error: Category already exists");
            }
        }

        // Save new Category
        carCategoryRepository.save(carCategory);

        // Success
        return ResponseEntity.ok().body("Success: added Category");
    }

    @Transactional
    public ResponseEntity<Object>  updateCategoryById(Long id, CarCategory carCategory){
        // Check if referenced Category exists
        if (carCategoryRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Category does not exist");
        }
        CarCategory updatedCategory = carCategoryRepository.getReferenceById(id);

        // Checks for if updated Category is a duplicate
        List<CarCategory> carCategoryList = carCategoryRepository.findAll();
        for (CarCategory category: carCategoryList) {
            if (category.getCarCategory().equals(carCategory.getCarCategory()) &&
                    !(category.getCategoryId().equals(id))){
                return ResponseEntity.unprocessableEntity().body("Error: Category already exists");
            }
        }

        // Update Category
        updatedCategory.setCarCategory(carCategory.getCarCategory());

        // Success
        return ResponseEntity.ok().body("Success: updated Category");
    }

    public ResponseEntity<Object> deleteCategory(Long id){
        // Check if referenced Category exists
        if (carCategoryRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Category does not exist");
        }
        CarCategory category = carCategoryRepository.getReferenceById(id);

        // Check if Category is used in another entity
        List<CarModel> carModelList = carModelRepository.findAll();
        for (CarModel model : carModelList){
            if (model.getCarCategory().equals(category)){
                return ResponseEntity.unprocessableEntity().body("Error: Category in use by Car Model: "+model.getModelId());
            }
        }

        // Delete Category
        carCategoryRepository.deleteById(id);

        // Check if Category deleted
        if(carCategoryRepository.findById(id).isEmpty()) {
            return ResponseEntity.unprocessableEntity().body("Error: Failed to delete Category");
        }

        // Success
        return ResponseEntity.ok().body("Success: deleted category");
    }

    public List<CarCategory> searchCategory(String search){
        // Initialize our lists
        List<CarCategory> categories = carCategoryRepository.findAll();
        List<CarCategory> found = new ArrayList<>();

        // Loop through to find matches
        for (CarCategory category:categories){
            if (category.getCarCategory().contains(search)){
                found.add(category);
            }
        }

        // Success
        return found;
    }
}
