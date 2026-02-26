package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.dcim.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("""
    select distinct l from Location l
    left join fetch l.children
    left join fetch l.parent
""")
    List<Location> findAllWithRelations();

    @Query("""
    select l from Location l
    left join fetch l.children
    left join fetch l.parent
    where l.id = :id
""")
    Optional<Location> findByIdWithRelations(@Param("id") Long id);
}
