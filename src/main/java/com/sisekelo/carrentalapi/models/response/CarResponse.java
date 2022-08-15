package com.sisekelo.carrentalapi.models.response;

import com.sisekelo.carrentalapi.models.dto.CarDto;
import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.CarCategory;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import lombok.Data;

import javax.persistence.*;

@Data
public class CarResponse {
        private String registrationNumber;//Number Plate Code

        private Long modelId;

        private Long categoryId;

        private Float price;

}
