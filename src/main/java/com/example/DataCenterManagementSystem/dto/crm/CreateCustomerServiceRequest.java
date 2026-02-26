package com.example.DataCenterManagementSystem.dto.crm;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCustomerServiceRequest(
        @NotNull(message = "Customer ID is required")
        Long customerId,

        @NotNull(message = "Server ID is required")
        Long serverId,

        @NotNull(message = "Expiry date is required")
        @FutureOrPresent(message = "Expiry date must be in the present or future")
        LocalDateTime expiryDate,
        @NotNull(message = "Amount date is required")
        BigDecimal amount
) {}
