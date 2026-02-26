package com.example.DataCenterManagementSystem.entity.security;


import com.example.DataCenterManagementSystem.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder @NoArgsConstructor
@Entity @Table(name = "activity_logs")
public class ActivityLog extends BaseEntity {

    @NotBlank(message = "Action is required")
    @Column(nullable = false, length = 200)
    private String action;


    @NotNull(message = "User is required")
    @Column(nullable = false)
    private String username;
}
