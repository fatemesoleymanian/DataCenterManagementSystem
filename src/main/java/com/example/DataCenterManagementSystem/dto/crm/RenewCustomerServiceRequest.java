package com.example.DataCenterManagementSystem.dto.crm;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RenewCustomerServiceRequest(
        @NotNull(message = "Service id is required.")
        Long serviceId,
        @NotNull(message = "New expiryDate is required.")
        LocalDateTime newExpiryDate,
        @NotNull(message = "Amount id is required.")
        BigDecimal amount
) {
}
