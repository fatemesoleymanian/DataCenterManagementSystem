package com.example.DataCenterManagementSystem.dto.dcim;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DataCenterResponse {

    private Long id;
    private String name;
    private Long locationId;
    private List<RackRowResponse> rows;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String lastModifiedBy;
}
