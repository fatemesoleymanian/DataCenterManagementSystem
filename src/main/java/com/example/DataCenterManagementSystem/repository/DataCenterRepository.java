package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.DataCenter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataCenterRepository extends JpaRepository<DataCenter, Long> {

    @EntityGraph(attributePaths = {"rows", "location"})
    @Query("select d from DataCenter d")
    List<DataCenter> findAllWithDetails();

    @EntityGraph(attributePaths = {"rows", "location"})
    @Query("select d from DataCenter d where d.id = :id")
    Optional<DataCenter> findWithDetailsById(@Param("id") Long id);
}
