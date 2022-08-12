package com.sisekelo.carrentalapi.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long reservationId;

    @Column(nullable = false)
    private Long carId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Date collectionDate;

    @Column(nullable = false)
    private Date returnDate;

}
