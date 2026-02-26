package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.RackUnit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RackUnitRepository extends JpaRepository<RackUnit, Long> {
    @EntityGraph(attributePaths = "rack")
    @Query("SELECT r from RackUnit r where r.id = :id")
    Optional<RackUnit> findByIdWithRack(Long id);

    @EntityGraph(attributePaths = {"rack", "occupiedBy"})
    List<RackUnit> findAll();
}
