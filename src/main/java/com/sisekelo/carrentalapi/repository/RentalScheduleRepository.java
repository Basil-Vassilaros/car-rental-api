package com.sisekelo.carrentalapi.repository;

import com.sisekelo.carrentalapi.models.RentalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalScheduleRepository extends JpaRepository<RentalSchedule, Long> {
}
