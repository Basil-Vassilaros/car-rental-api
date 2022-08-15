package com.sisekelo.carrentalapi.repository;

import com.sisekelo.carrentalapi.models.tables.CarManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarManufacturerRepository extends JpaRepository<CarManufacturer, Long> {
}
