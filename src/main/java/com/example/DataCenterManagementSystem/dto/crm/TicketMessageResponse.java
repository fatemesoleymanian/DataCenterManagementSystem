package com.example.DataCenterManagementSystem.dto.crm;

import java.time.LocalDateTime;

public record TicketMessageResponse(
        Long id,
        String message,
        LocalDateTime createdAt,
        Long senderId,

        LocalDateTime updatedAt,
        String createdBy,
        String lastModifiedBy
) {
}
