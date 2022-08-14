package com.sisekelo.carrentalapi.services.table;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.CarCategory;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import com.sisekelo.carrentalapi.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarCategoryService {
    private CarCategoryRepository carCategoryRepository;
    private CarRepository carRepository;

    @Autowired
    public CarCategoryService(CarCategoryRepository carCategoryRepository, CarRepository carRepository) {
        this.carCategoryRepository = carCategoryRepository;
        this.carRepository = carRepository;
    }

    public CarCategory addCategory(CarCategory carCategory){
        return carCategoryRepository.save(carCategory);
    }

    public CarCategory getCategory(Long id){
        return carCategoryRepository.getReferenceById(id);
    }

    public List<CarCategory> getAllCategory(){
        return carCategoryRepository.findAll();
    }

    @Transactional
    public CarCategory updateCategoryById(Long id, CarCategory carCategory){
        CarCategory updatedCategory = carCategoryRepository.getReferenceById(id);

        updatedCategory.setCarCategory(carCategory.getCarCategory());

        return updatedCategory;
    }

    public ResponseEntity<Object> deleteCategory(Long id){
        if(carCategoryRepository.findById(id).isPresent()){
             /*
                If the Car Category that I wish to delete is referenced to a Car Model
                then I cannot delete the Car Category until it no longer is referenced
            */
            Boolean isReferenced = false;
            List<Car> listToReference = carRepository.findAll();// the list to see if there are any references
            List<Long> referenceExistList = new ArrayList<Long>();// a list to save the IDs of the entities referencing
            CarCategory reference = carCategoryRepository.findById(id).get();// the entity I want to delete
            for (Car toReference : listToReference) {
                if (toReference.getCarCategory() == reference) {
                    isReferenced = true;
                    referenceExistList.add(toReference.getCarId());
                }
            }
            if (!isReferenced) {
                carCategoryRepository.deleteById(id);
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed to delete category: Reference to Car (" + referenceExistList + ")");
            }
            if(carCategoryRepository.findById(id).isPresent()){
                return ResponseEntity.unprocessableEntity().body("Failed to delete category: Unknown");
            }
            else {
                return ResponseEntity.ok().body("Success: deleted category");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Category not found");
        }
    }
}
