package com.sisekelo.carrentalapi.models.tables;

import com.sisekelo.carrentalapi.models.dto.CarDto;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "Car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long carId;
    private String registrationNumber;//Number Plate Code

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelId", nullable = false)
    private CarModel carModel;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private Boolean isReserved;

    @Column(nullable = false)
    private Boolean inUse;

    @Column(nullable = false)
    private String color;

    private List<String> reservedDates;
    public static Car from(CarDto carDto){
        Car car = new Car();
		car.setRegistrationNumber(carDto.getRegistrationNumber());
		car.setPrice(carDto.getPrice());
		car.setIsReserved(carDto.getIsReserved());
		car.setInUse(carDto.getInUse());
        return car;
    }
}
