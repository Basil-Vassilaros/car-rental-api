package com.sisekelo.carrentalapi.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "Client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long clientId;

    private String firstName;

    private String lastName;

    private String mobileNumber;

    private String emailAddress;

    private String homeAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carId", nullable = false)
    private Car carsReserved;
}
