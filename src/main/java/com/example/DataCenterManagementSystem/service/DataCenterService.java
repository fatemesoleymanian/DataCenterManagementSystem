package com.example.DataCenterManagementSystem.service;


import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.dcim.CreateDataCenterRequest;
import com.example.DataCenterManagementSystem.dto.dcim.CreateRackRowRequest;
import com.example.DataCenterManagementSystem.dto.dcim.DataCenterResponse;
import com.example.DataCenterManagementSystem.dto.dcim.RackRowResponse;
import com.example.DataCenterManagementSystem.entity.dcim.DataCenter;
import com.example.DataCenterManagementSystem.entity.dcim.Location;
import com.example.DataCenterManagementSystem.entity.dcim.RackRow;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.repository.DataCenterRepository;
import com.example.DataCenterManagementSystem.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DataCenterService {

    private final DataCenterRepository dataCenterRepository;
    private final LocationRepository locationRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CREATE_DATACENTER")
    public DataCenterResponse createDataCenter(CreateDataCenterRequest request) {

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new NotFoundException(
                        "Location not found with id: " + request.getLocationId()));

        DataCenter dataCenter = DataCenter.builder()
                .name(request.getName())
                .location(location)
                .build();

        if (request.getRows() != null) {
            for (CreateRackRowRequest rowReq : request.getRows()) {
                RackRow row = RackRow.builder()
                        .rowName(rowReq.getRowName())
                        .dataCenter(dataCenter)
                        .build();
                dataCenter.getRows().add(row);
            }
        }

        DataCenter saved = dataCenterRepository.save(dataCenter);
        return mapToResponse(saved);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public DataCenterResponse getDataCenterById(Long id) {

        DataCenter dataCenter = dataCenterRepository.findWithDetailsById(id)
                .orElseThrow(() -> new NotFoundException(
                        "DataCenter not found with id: " + id));

        return mapToResponse(dataCenter);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<DataCenterResponse> getAllDataCenters() {

        return dataCenterRepository.findAllWithDetails()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DELETE_DATACENTER")
    public void deleteDataCenter(Long id) {

        DataCenter dataCenter = dataCenterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "DataCenter not found with id: " + id));

        dataCenterRepository.delete(dataCenter);
    }

    private DataCenterResponse mapToResponse(DataCenter entity) {

        List<RackRowResponse> rows = entity.getRows() == null
                ? List.of()
                : entity.getRows()
                .stream()
                .map(row -> RackRowResponse.builder()
                        .id(row.getId())
                        .rowName(row.getRowName())
                        .build())
                .toList();

        return DataCenterResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .locationId(
                        entity.getLocation() != null
                                ? entity.getLocation().getId()
                                : null
                )
                .rows(rows)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .build();
    }
}