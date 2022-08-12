package com.sisekelo.carrentalapi.models;

import com.sisekelo.carrentalapi.models.dto.CarDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Currency;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private CarCategory carCategory;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private Boolean isReserved;

    @Column(nullable = false)
    private Boolean inUse;
	
    public static Car from(CarDto carDto){
        Car car = new Car();
		car.setRegistrationNumber(carDto.getRegistrationNumber());
		car.setPrice(carDto.getPrice());
		car.setIsReserved(carDto.getIsReserved());
		car.setInUse(carDto.getInUse());
        return car;
    }
}
