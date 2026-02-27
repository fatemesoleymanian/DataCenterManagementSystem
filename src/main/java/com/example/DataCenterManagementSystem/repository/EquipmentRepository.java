package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.Equipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    // EquipmentRepository.java

    @EntityGraph(attributePaths = {"ports", "occupiedUnits"})
    @Query("SELECT e FROM Equipment e WHERE e.id = :id")
    Optional<Equipment> findByIdWithDetails(@Param("id") Long id);


    @EntityGraph(attributePaths = {"ports"})
    @Query("select e from Equipment e")
    List<Equipment> findAllWithDetails();

    @Query("""
    SELECT DISTINCT e 
    FROM Equipment e 
    JOIN e.occupiedUnits ru 
    WHERE ru.rack.id = :rackId
    """)
    List<Equipment> findByRackId(@Param("rackId") Long rackId);

    @EntityGraph(attributePaths = {"ports"})
    @Query("SELECT e FROM Equipment e WHERE e.id = :id")
    Optional<Equipment> findByIdWithPorts(@Param("id") Long id);
}
