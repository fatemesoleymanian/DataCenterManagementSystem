package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.Equipment;
import com.example.DataCenterManagementSystem.entity.dcim.Rack;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {

    @EntityGraph(attributePaths = {
            "rackRow",
            "units"
    })
    @Query("SELECT r from Rack r where r.id = :id")
    Optional<Rack> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {
            "rackRow",
            "units"
    })
    @Query("SELECT r from Rack r")
    List<Rack> findAllWithDetails();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       select r from Rack r
       left join fetch r.units
       where r.id = :id
       """)
    Optional<Rack> findByIdWithUnitsForUpdate(@Param("id") Long id);


    @EntityGraph(attributePaths = {"occupiedUnits", "ports"})
    @Query("SELECT DISTINCT e FROM Equipment e " +
            "JOIN e.occupiedUnits ru " +
            "WHERE ru.rack.id = :rackId")
    List<Long> findByRackId(@Param("rackId") Long rackId);
}
