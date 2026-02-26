package com.example.DataCenterManagementSystem.entity.crm;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import com.example.DataCenterManagementSystem.entity.dcim.Equipment;
import com.example.DataCenterManagementSystem.entity.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder
@NoArgsConstructor
@Entity @Table(name = "customer_services")
public class CustomerService extends BaseEntity {

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @NotNull(message = "Server is required")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false, unique = true)
    private Equipment server;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date must be in the present or future")
    @Column(nullable = false)
    private LocalDateTime expiryDate;
}
