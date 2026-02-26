package com.example.DataCenterManagementSystem.dto.dcim;


import java.time.LocalDateTime;
import java.util.List;

public record LocationResponse(
        Long id,
        String name,
        int level,
        Long parentId,
        List<Long> childrenIds,


        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String lastModifiedBy
) {}
