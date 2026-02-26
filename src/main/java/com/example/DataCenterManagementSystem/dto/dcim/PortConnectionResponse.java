package com.example.DataCenterManagementSystem.dto.dcim;


public record PortConnectionResponse(
        Long connectionId,
        Long portAId,
        Long portBId
) {}