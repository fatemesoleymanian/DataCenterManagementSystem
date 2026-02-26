package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.Equipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    @EntityGraph(attributePaths = {
            "occupiedUnits",
            "occupiedUnits.rack",
            "ports"
    })
    @Query("select e from Equipment e where e.id = :id")
    Optional<Equipment> findByIdWithDetails(@Param("id") Long id);


    @EntityGraph(attributePaths = {
            "occupiedUnits",
            "occupiedUnits.rack",
            "ports"
    })
    @Query("select e from Equipment e")
    List<Equipment> findAllWithDetails();

    @EntityGraph(attributePaths = {"occupiedUnits", "occupiedUnits.rack", "ports"})
    @Query("SELECT DISTINCT e FROM Equipment e JOIN e.occupiedUnits u WHERE u.rack.id = :rackId")
    List<Equipment> findByRackId(@Param("rackId") Long rackId);
}
