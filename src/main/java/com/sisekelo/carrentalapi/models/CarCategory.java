package com.sisekelo.carrentalapi.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "CarCategory")
/*
What type of vehicle is it? Car, Bus, Combi, Truck etc.
 */
public class CarCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String carCategory;
}
