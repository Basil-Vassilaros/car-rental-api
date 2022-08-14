package com.sisekelo.carrentalapi.repository;

import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRecordRepository extends JpaRepository<RentalRecord, Long> {
}
