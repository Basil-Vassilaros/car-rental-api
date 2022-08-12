package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.CarCategory;
import com.sisekelo.carrentalapi.services.CarCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CarCategoryController {
    private CarCategoryService carCategoryService;

    @Autowired
    public CarCategoryController(CarCategoryService carCategoryService) {
        this.carCategoryService = carCategoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<CarCategory> addCategory(@RequestBody final CarCategory carCategory) {
        return new ResponseEntity<>(carCategoryService.addCategory(carCategory), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable final Long id){
        return carCategoryService.deleteCategory(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<CarCategory> updateCategory(@PathVariable final Long id, @RequestBody final CarCategory category){
        CarCategory updatedCategory = carCategoryService.updateCategoryById(id, category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<CarCategory> getCategory(@PathVariable Long id) {
        return new ResponseEntity<>(carCategoryService.getCategory(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarCategory>> getAllCategory() {
        return new ResponseEntity<>(carCategoryService.getAllCategory(), HttpStatus.OK);
    }
}
