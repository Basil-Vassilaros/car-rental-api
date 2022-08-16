package com.sisekelo.carrentalapi.models.response;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Reservation {

    private Long carId;

    private Long clientId;

    private String dateToCollect;

    private String dateToReturn;
}