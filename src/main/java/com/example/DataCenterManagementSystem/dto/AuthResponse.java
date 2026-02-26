package com.example.DataCenterManagementSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "پاسخ احراز هویت")

public record AuthResponse (
        @Schema(description = "توکن کاربر")
        String token,
        @Schema(description = "نقش کاربر")
        String role
){ }
