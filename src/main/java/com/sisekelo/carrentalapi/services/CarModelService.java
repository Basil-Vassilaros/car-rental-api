package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.CarModel;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarModelService {
    private CarModelRepository carModelRepository;

    @Autowired
    public CarModelService(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public CarModel addModel(CarModel carModel){
        return carModelRepository.save(carModel);
    }
}
