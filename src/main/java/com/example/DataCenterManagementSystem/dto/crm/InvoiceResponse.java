package com.example.DataCenterManagementSystem.dto.crm;


import com.example.DataCenterManagementSystem.entity.crm.InvoiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceResponse(
        Long id,
        BigDecimal amount,
        InvoiceType type,
        boolean isPaid,
        Long customerId,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String lastModifiedBy
) {}
