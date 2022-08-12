package com.sisekelo.carrentalapi.models.dto;

import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.models.CarCategory;
import com.sisekelo.carrentalapi.models.CarModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CarDto {
    private Long carId;

    private String registrationNumber;

    private CarModel carModel;

    private CarCategory carCategory;

    private Float price;

    private Boolean isReserved;

    private Boolean inUse;

    public static CarDto from(Car car){
        CarDto carDto = new CarDto();
        carDto.setCarId(car.getCarId());
        carDto.setRegistrationNumber(car.getRegistrationNumber());
        carDto.setPrice(car.getPrice());
        carDto.setIsReserved(car.getIsReserved());
        carDto.setInUse(car.getInUse());
        return carDto;
    }
}