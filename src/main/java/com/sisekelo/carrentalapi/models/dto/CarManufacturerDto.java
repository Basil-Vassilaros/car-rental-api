package com.sisekelo.carrentalapi.models.dto;

import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import lombok.Data;

@Data
public class CarManufacturerDto {
    private Long manufacturerId;

    private String manufacturer;

    public static CarManufacturerDto from(CarManufacturer manufacturer){
        CarManufacturerDto manufacturerDto = new CarManufacturerDto();
        manufacturerDto.setManufacturer(manufacturer.getManufacturer());
        return manufacturerDto;
    }
}
