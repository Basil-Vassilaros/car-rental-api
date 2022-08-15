package com.sisekelo.carrentalapi.models.response;

import com.sisekelo.carrentalapi.models.tables.Car;
import lombok.Data;

import javax.persistence.*;

@Data
public class ClientResponse {
        private Long clientId;

        private String firstName;

        private String lastName;

        private String mobileNumber;

        private String emailAddress;

        private String homeAddress;

}
