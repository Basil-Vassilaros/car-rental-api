package com.sisekelo.carrentalapi.models;

import com.sisekelo.carrentalapi.models.dto.CarDto;
import com.sisekelo.carrentalapi.models.dto.CarModelDto;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "CarModel")
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long modelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manufacturerId",nullable = false)
    private CarManufacturer carManufacturer;

    @Column(nullable = false)
    private String carModel;

    public static CarModel from(CarModelDto carModelDto){
        CarModel model = new CarModel();
        model.setCarModel(carModelDto.getCarModel());
        return model;
    }
}