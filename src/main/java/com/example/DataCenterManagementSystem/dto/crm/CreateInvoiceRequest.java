package com.example.DataCenterManagementSystem.dto.crm;

import com.example.DataCenterManagementSystem.entity.crm.InvoiceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateInvoiceRequest(
        @Min(value = 0, message = "Amount must be non-negative")
        BigDecimal amount,

        @NotNull(message = "Type is required")
        InvoiceType type,

        @NotNull(message = "Customer ID is required")
        Long customerId
) {}
