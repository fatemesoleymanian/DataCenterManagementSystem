package com.example.DataCenterManagementSystem.dto.crm;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketRequest(
        @NotBlank(message = "Subject is required")
        String subject
) {
}
