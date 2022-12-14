package com.sisekelo.carrentalapi.repository;

import com.sisekelo.carrentalapi.models.tables.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
