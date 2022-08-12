package com.sisekelo.carrentalapi.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Currency;
import java.util.Date;

@Entity
@Data
@Table(name = "RentalSchedule")
public class RentalSchedule {
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

    @Column(nullable = false)
    private Date reservationDate;

    @Column(nullable = false)
    private Date collectionDate;

    @Column(nullable = false)
    private Date returnDate;

    @Column(nullable = false)
    private Float totalPrice;
}
