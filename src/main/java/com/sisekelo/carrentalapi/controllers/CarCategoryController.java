package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.tables.CarCategory;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import com.sisekelo.carrentalapi.services.table.CarCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CarCategoryController {
    private CarCategoryService carCategoryService;
    private CarCategoryRepository carCategoryRepository;

    @Autowired
    public CarCategoryController(CarCategoryService carCategoryService, CarCategoryRepository carCategoryRepository) {
        this.carCategoryService = carCategoryService;
        this.carCategoryRepository = carCategoryRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<CarCategory> addCategory(@RequestBody final CarCategory carCategory) {
        return new ResponseEntity<>(carCategoryService.addCategory(carCategory), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable final Long id){
        return carCategoryService.deleteCategory(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CarCategory> updateCategory(@PathVariable final Long id, @RequestBody final CarCategory category){
        CarCategory updatedCategory = carCategoryService.updateCategoryById(id, category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        if (carCategoryRepository.findById(id).isPresent()){
            return new ResponseEntity<>(carCategoryRepository.getReferenceById(id), HttpStatus.OK);
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Car Category not found");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarCategory>> getAllCategory() {
        return new ResponseEntity<>(carCategoryRepository.findAll(), HttpStatus.OK);
    }
}
