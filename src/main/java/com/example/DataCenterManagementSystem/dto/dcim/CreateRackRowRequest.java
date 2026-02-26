package com.example.DataCenterManagementSystem.dto.dcim;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class CreateRackRowRequest {

    @NotBlank(message = "Row name is required")
    private String rowName;

    @NotNull(message = "DataCenter ID is required")
    private Long dataCenterId;

    private List<CreateRackRequest> racks;
}