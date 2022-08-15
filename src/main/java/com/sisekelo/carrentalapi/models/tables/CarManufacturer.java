package com.sisekelo.carrentalapi.models.tables;

import com.sisekelo.carrentalapi.models.dto.CarManufacturerDto;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "CarManufacturer")
public class CarManufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long manufacturerId;

    @Column(nullable = false)
    private String manufacturer;
}