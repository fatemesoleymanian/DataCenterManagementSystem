package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.crm.Ticket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @EntityGraph(attributePaths = {"creator", "messages", "messages.sender"})
    @Query("SELECT t FROM Ticket t WHERE t.id = :id")
    Optional<Ticket> findByIdWithMessages(@Param("id") Long id);

    List<Ticket> findByCreatorId(Long creatorId);
}
