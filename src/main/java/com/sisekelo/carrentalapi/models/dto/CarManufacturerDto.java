package com.sisekelo.carrentalapi.models.dto;

import com.sisekelo.carrentalapi.models.CarManufacturer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
