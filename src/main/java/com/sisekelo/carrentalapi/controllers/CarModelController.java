package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.response.CarModelResponse;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import com.sisekelo.carrentalapi.services.response.CarModelResponseService;
import com.sisekelo.carrentalapi.services.table.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/model")
public class CarModelController {
    private CarModelService carModelService;
    private CarModelResponseService carModelResponseService;
    private CarModelRepository carModelRepository;
    @Autowired
    public CarModelController(CarModelService carModelService, CarModelResponseService carModelResponseService, CarModelRepository carModelRepository) {
        this.carModelService = carModelService;
        this.carModelResponseService = carModelResponseService;
        this.carModelRepository = carModelRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addModel(@RequestBody final CarModelResponse model) {
        return carModelResponseService.addModel(model);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteModel(@PathVariable final Long id){
        return carModelResponseService.removeCarModel(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateModel(@PathVariable final Long id, @RequestBody final CarModelResponse model){
        return carModelResponseService.updateCarModelById(id, model);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<CarModel> getModel(@PathVariable Long id) {
        if (carModelRepository.findById(id).isPresent()){
            return new ResponseEntity<>(carModelRepository.getReferenceById(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarModel>> getAllModels() {
        return new ResponseEntity<>(carModelRepository.findAll(), HttpStatus.OK);
    }
}
