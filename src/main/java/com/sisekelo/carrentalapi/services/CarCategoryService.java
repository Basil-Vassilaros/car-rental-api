package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.models.CarCategory;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CarCategoryService {
    private CarCategoryRepository carCategoryRepository;

    @Autowired
    public CarCategoryService(CarCategoryRepository carCategoryRepository) {
        this.carCategoryRepository = carCategoryRepository;
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
            carCategoryRepository.deleteById(id);
            if(carCategoryRepository.findById(id).isPresent()){
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
