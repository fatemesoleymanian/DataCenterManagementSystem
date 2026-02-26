package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.Port;
import com.example.DataCenterManagementSystem.entity.dcim.PortConnection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PortConnectionRepository extends JpaRepository<PortConnection, Long> {

    @EntityGraph(attributePaths = {
            "portA",
            "portB",
            "portA.equipment",
            "portB.equipment"
    })
    @Query("select pc from PortConnection pc where pc.id = :id")
    Optional<PortConnection> findWithDetails(@Param("id") Long id);

    boolean existsByPortAAndPortB(Port portA, Port portB);
}
