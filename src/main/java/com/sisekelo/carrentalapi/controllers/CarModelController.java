package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.CarModel;
import com.sisekelo.carrentalapi.services.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/model")
public class CarModelController {
    private CarModelService carModelService;
    @Autowired
    public CarModelController(CarModelService carModelService) {
        this.carModelService = carModelService;
    }



    @PostMapping("/add")
    public ResponseEntity<CarModel> addModel(@RequestBody final CarModel carModel) {
        return new ResponseEntity<>(carModelService.addModel(carModel), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Object> deleteModel(@PathVariable final Long id){
        return carModelService.deleteModel(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<CarModel> updateModel(@PathVariable final Long id, @RequestBody final CarModel Model){
        CarModel updatedModel = carModelService.updateModelById(id, Model);
        return new ResponseEntity<>(updatedModel, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<CarModel> getModel(@PathVariable Long id) {
        return new ResponseEntity<>(carModelService.getModel(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarModel>> getAllModel() {
        return new ResponseEntity<>(carModelService.getAllModel(), HttpStatus.OK);
    }
}
