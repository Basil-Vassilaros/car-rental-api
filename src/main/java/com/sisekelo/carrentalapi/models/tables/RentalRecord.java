package com.sisekelo.carrentalapi.models.tables;

import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.Client;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime dateReservationMade;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dateToCollect;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dateToReturn;

    @Column(nullable = false)
    private Float totalPrice;
}
