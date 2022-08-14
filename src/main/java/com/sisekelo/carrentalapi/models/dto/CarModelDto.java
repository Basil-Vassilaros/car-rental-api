package com.sisekelo.carrentalapi.models.dto;

import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import lombok.Data;

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
