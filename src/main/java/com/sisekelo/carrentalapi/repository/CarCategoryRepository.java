package com.sisekelo.carrentalapi.repository;

import com.sisekelo.carrentalapi.models.tables.CarCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCategoryRepository extends JpaRepository<CarCategory, Long> {
}
