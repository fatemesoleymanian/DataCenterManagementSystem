package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.RackRow;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RackRowRepository extends JpaRepository<RackRow, Long> {

    @EntityGraph(attributePaths = {"racks", "dataCenter"})
    @Query("SELECT r from RackRow r where r.id = :id")
    Optional<RackRow> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"racks", "dataCenter"})
    @Query("SELECT r from RackRow r")
    List<RackRow> findAllWithDetails();
}
