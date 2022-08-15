package com.sisekelo.carrentalapi.models.dto;

import com.sisekelo.carrentalapi.models.tables.Car;
import lombok.Data;

@Data
public class ClientDto {
    private Long clientId;

    private String firstName;

    private String lastName;

    private String mobileNumber;

    private String emailAddress;

    private String homeAddress;

    private Car carsReserved;
}
