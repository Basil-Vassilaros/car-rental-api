package com.sisekelo.carrentalapi.repository;

import com.sisekelo.carrentalapi.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
