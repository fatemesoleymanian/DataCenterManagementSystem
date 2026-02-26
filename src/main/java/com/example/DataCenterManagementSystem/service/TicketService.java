package com.example.DataCenterManagementSystem.service;

import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.crm.CreateTicketMessageRequest;
import com.example.DataCenterManagementSystem.dto.crm.CreateTicketRequest;
import com.example.DataCenterManagementSystem.dto.crm.TicketMessageResponse;
import com.example.DataCenterManagementSystem.dto.crm.TicketResponse;
import com.example.DataCenterManagementSystem.entity.crm.Ticket;
import com.example.DataCenterManagementSystem.entity.crm.TicketMessage;
import com.example.DataCenterManagementSystem.entity.crm.TicketStatus;
import com.example.DataCenterManagementSystem.entity.security.User;
import com.example.DataCenterManagementSystem.entity.security.UserRole;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.exception.UnauthorizedException;
import com.example.DataCenterManagementSystem.repository.TicketRepository;
import com.example.DataCenterManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    @LogActivity(action = "CREATE_TICKET")
    public TicketResponse createTicket(CreateTicketRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User creator = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Ticket ticket = Ticket.builder()
                .subject(request.subject())
                .status(TicketStatus.OPEN)
                .creator(creator)
                .build();

        ticket = ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SUPPORT')")
    @LogActivity(action = "ADD_TICKET_MESSAGE")
    public TicketMessageResponse addMessageToTicket(Long ticketId, CreateTicketMessageRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User sender = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Ticket ticket = ticketRepository.findByIdWithMessages(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + ticketId));

        // Check if customer is creator or support
        if (sender.getRole() == UserRole.CUSTOMER && !ticket.getCreator().getId().equals(sender.getId())) {
            throw new UnauthorizedException("You can only add messages to your own tickets");
        }

        TicketMessage message = TicketMessage.builder()
                .message(request.message())
                .createdAt(LocalDateTime.now())
                .sender(sender)
                .ticket(ticket)
                .build();

        ticket.getMessages().add(message);
        message = ticketRepository.save(ticket).getMessages().get(ticket.getMessages().size() - 1); // Get the saved message
        return mapToMessageResponse(message);
    }

    @Transactional
    @PreAuthorize("hasRole('SUPPORT')")
    @LogActivity(action = "UPDATE_TICKET_STATUS")
    public TicketResponse updateTicketStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findByIdWithMessages(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + id));

        ticket.setStatus(newStatus);
        ticket = ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT', 'CUSTOMER')")
    public TicketResponse getTicketById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Ticket ticket = ticketRepository.findByIdWithMessages(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + id));

        // Access control
        if (user.getRole() == UserRole.CUSTOMER && !ticket.getCreator().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only view your own tickets");
        }

        return mapToResponse(ticket);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT', 'CUSTOMER')")
    public List<TicketResponse> getAllTickets() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Ticket> tickets;
        if (user.getRole() == UserRole.CUSTOMER) {
            tickets = ticketRepository.findByCreatorId(user.getId());
        } else {
            tickets = ticketRepository.findAll();
        }

        return tickets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Mapper methods
    private TicketResponse mapToResponse(Ticket entity) {
        return new TicketResponse(
                entity.getId(),
                entity.getSubject(),
                entity.getStatus(),
                entity.getCreator().getId(),
                entity.getMessages().stream()
                        .map(this::mapToMessageResponse)
                        .collect(Collectors.toList())
        );
    }

    private TicketMessageResponse mapToMessageResponse(TicketMessage entity) {
        return new TicketMessageResponse(
                entity.getId(),
                entity.getMessage(),
                entity.getCreatedAt(),
                entity.getSender().getId(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getLastModifiedBy()
        );
    }
}
