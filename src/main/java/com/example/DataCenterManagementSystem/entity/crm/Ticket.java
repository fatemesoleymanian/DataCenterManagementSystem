package com.example.DataCenterManagementSystem.entity.crm;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import com.example.DataCenterManagementSystem.entity.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder @NoArgsConstructor
@Entity @Table(name = "tickets")
public class Ticket extends BaseEntity {

    @NotBlank(message = "Subject is required")
    @Column(nullable = false, length = 200)
    private String subject;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @NotNull(message = "Creator is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TicketMessage> messages = new ArrayList<>();
}
