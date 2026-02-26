package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.Port;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PortRepository extends JpaRepository<Port, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Port p where p.id = :id")
    Optional<Port> findByIdForUpdate(@Param("id") Long id);
}
