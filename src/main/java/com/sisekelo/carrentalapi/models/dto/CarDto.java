package com.sisekelo.carrentalapi.models.dto;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.CarCategory;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import lombok.Data;

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