package com.example.DataCenterManagementSystem.controller.crm;

import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.crm.CreateTicketMessageRequest;
import com.example.DataCenterManagementSystem.dto.crm.CreateTicketRequest;
import com.example.DataCenterManagementSystem.dto.crm.TicketMessageResponse;
import com.example.DataCenterManagementSystem.dto.crm.TicketResponse;
import com.example.DataCenterManagementSystem.entity.crm.TicketStatus;
import com.example.DataCenterManagementSystem.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@CrossOrigin
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Create a new ticket")
    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket
            (@Valid @RequestBody CreateTicketRequest request) {
        TicketResponse created = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    @Operation(summary = "Add message to ticket")
    @PostMapping("/{ticketId}/messages")
    public ResponseEntity<ApiResponse<TicketMessageResponse>> addMessageToTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody CreateTicketMessageRequest request) {
        TicketMessageResponse created = ticketService.addMessageToTicket(ticketId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    @Operation(summary = "Update ticket status")
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicketStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus newStatus) {
        TicketResponse updated = ticketService.updateTicketStatus(id, newStatus);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @Operation(summary = "Get ticket by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(@PathVariable Long id) {
        TicketResponse response = ticketService.getTicketById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all tickets (filtered by role)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllTickets() {
        List<TicketResponse> responses = ticketService.getAllTickets();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
