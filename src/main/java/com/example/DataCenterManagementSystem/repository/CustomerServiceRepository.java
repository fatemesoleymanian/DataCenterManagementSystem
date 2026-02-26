package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.crm.CustomerService;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long> {
    @EntityGraph(attributePaths = {"customer", "server"})
    @Query("SELECT cs FROM CustomerService cs WHERE cs.id = :id")
    Optional<CustomerService> findByIdWithDetails(@Param("id") Long id);
}
