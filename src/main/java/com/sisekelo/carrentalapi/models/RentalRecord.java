package com.sisekelo.carrentalapi.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "RentalSchedule")
public class RentalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long rentalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="carId", nullable = false, updatable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="clientId", nullable = false, updatable = false)
    private Client client;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime reservationDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime collectionDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime returnDate;

    @Column(nullable = false)
    private Float totalPrice;
}
