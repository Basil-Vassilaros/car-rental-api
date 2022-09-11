package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.CarCategory;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import com.sisekelo.carrentalapi.services.table.CarCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")

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
    public ResponseEntity<Object> addCategory(@RequestBody final CarCategory carCategory) {
        return carCategoryService.addCategory(carCategory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable final Long id){
        return carCategoryService.deleteCategory(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable final Long id, @RequestBody final CarCategory category){
        return carCategoryService.updateCategoryById(id, category);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        if (carCategoryRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Category not found");
        }
        return new ResponseEntity<>(carCategoryRepository.getReferenceById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarCategory>> getAllCategory() {
        return new ResponseEntity<>(carCategoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<CarCategory>> search(@PathVariable String search) {
        return new ResponseEntity<>(carCategoryService.searchCategory(search), HttpStatus.OK);
    }
}
