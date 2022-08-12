package com.sisekelo.carrentalapi.models.dto;

import com.sisekelo.carrentalapi.models.Car;
import com.sisekelo.carrentalapi.models.CarCategory;
import com.sisekelo.carrentalapi.models.CarManufacturer;
import com.sisekelo.carrentalapi.models.CarModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Currency;
@Data
public class CarModelDto {
    private Long modelId;

    private CarManufacturer carManufacturer;

    private String carModel;

    public static CarModelDto from(CarModel model){
        CarModelDto modelDto = new CarModelDto();
        modelDto.setCarModel(model.getCarModel());
        return modelDto;
    }
}
