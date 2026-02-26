package com.example.DataCenterManagementSystem.dto.dcim;

import jakarta.validation.constraints.NotNull;

public record ConnectPortsRequest(
        @NotNull Long portAId,
        @NotNull Long portBId
) {}
