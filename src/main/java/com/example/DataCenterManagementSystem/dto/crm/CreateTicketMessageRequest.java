package com.example.DataCenterManagementSystem.dto.crm;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketMessageRequest(
        @NotBlank(message = "Message is required")
        String message
) {
}
