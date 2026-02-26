package com.example.DataCenterManagementSystem.entity.crm;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import com.example.DataCenterManagementSystem.entity.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder @NoArgsConstructor
@Entity @Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Min(value = 0, message = "Amount must be non-negative")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceType type;

    @Column(nullable = false)
    private boolean isPaid = false;

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
}
