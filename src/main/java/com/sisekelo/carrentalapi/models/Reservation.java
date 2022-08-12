package com.sisekelo.carrentalapi.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime reservationDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime collectionDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime returnDate;

}
