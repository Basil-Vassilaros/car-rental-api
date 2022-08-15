package com.sisekelo.carrentalapi.services.table;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import com.sisekelo.carrentalapi.repository.CarModelRepository;
import com.sisekelo.carrentalapi.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarModelService {
    private CarModelRepository carModelRepository;
    private CarRepository carRepository;

    @Autowired
    public CarModelService(CarModelRepository carModelRepository, CarRepository carRepository) {
        this.carModelRepository = carModelRepository;
        this.carRepository = carRepository;
    }

    public CarModel getModel(Long id){
        return carModelRepository.getReferenceById(id);
    }

    public List<CarModel> getAllModel(){
        return carModelRepository.findAll();
    }

}
