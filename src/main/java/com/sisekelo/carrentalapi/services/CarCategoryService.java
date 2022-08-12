package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.CarCategory;
import com.sisekelo.carrentalapi.repository.CarCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
