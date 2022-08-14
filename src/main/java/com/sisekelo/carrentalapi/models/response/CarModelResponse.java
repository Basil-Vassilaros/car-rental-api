package com.sisekelo.carrentalapi.models.response;

import com.sisekelo.carrentalapi.models.dto.CarModelDto;
import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import lombok.Data;

import javax.persistence.*;

@Data
public class CarModelResponse {
    private Long manufacturerId;

    private String carModel;

    private String year;
}
