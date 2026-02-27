package com.example.DataCenterManagementSystem.dto.dcim;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RackResponse {

    private Long id;
    private Integer rackNumber;
    private Long rackRowId;
    private int maxUnits;
    private List<Long> unitIds;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String lastModifiedBy;
}