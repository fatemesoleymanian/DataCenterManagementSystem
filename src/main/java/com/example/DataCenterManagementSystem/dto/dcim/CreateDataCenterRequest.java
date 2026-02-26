package com.example.DataCenterManagementSystem.dto.dcim;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class CreateDataCenterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    private List<CreateRackRowRequest> rows;
}
