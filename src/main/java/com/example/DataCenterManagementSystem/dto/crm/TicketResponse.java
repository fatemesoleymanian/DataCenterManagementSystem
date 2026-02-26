package com.example.DataCenterManagementSystem.dto.crm;

import com.example.DataCenterManagementSystem.entity.crm.TicketStatus;

import java.util.List;

public record TicketResponse(
        Long id,
        String subject,
        TicketStatus status,
        Long creatorId,
        List<TicketMessageResponse> messages
) {
}
