package com.example.DataCenterManagementSystem.dto.dcim;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RackRowResponse {

    private Long id;
    private String rowName;
    private Long dataCenterId;
    private List<RackResponse> racks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String lastModifiedBy;
}