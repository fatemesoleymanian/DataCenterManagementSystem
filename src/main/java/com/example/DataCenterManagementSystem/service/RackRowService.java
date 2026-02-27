package com.example.DataCenterManagementSystem.service;


import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.dcim.CreateRackRequest;
import com.example.DataCenterManagementSystem.dto.dcim.CreateRackRowRequest;
import com.example.DataCenterManagementSystem.dto.dcim.RackResponse;
import com.example.DataCenterManagementSystem.dto.dcim.RackRowResponse;
import com.example.DataCenterManagementSystem.entity.dcim.DataCenter;
import com.example.DataCenterManagementSystem.entity.dcim.Rack;
import com.example.DataCenterManagementSystem.entity.dcim.RackRow;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.repository.DataCenterRepository;
import com.example.DataCenterManagementSystem.repository.RackRowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RackRowService {

    private final RackRowRepository rackRowRepository;
    private final DataCenterRepository dataCenterRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CREATE_RACKROW")
    public RackRowResponse createRackRow(CreateRackRowRequest request) {

        DataCenter dataCenter = dataCenterRepository.findById(request.getDataCenterId())
                .orElseThrow(() -> new NotFoundException(
                        "DataCenter not found with id: " + request.getDataCenterId()));

        RackRow rackRow = RackRow.builder()
                .rowName(request.getRowName())
                .dataCenter(dataCenter)
                .build();

        if (request.getRacks() != null) {
            for (CreateRackRequest rackReq : request.getRacks()) {
                Rack rack = Rack.builder()
                        .rackNumber(rackReq.getRackNumber())
                        .rackRow(rackRow)
                        .build();
                rackRow.getRacks().add(rack);
            }
        }

        RackRow saved = rackRowRepository.save(rackRow);
        return mapToResponse(saved);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public RackRowResponse getRackRowById(Long id) {

        RackRow rackRow = rackRowRepository.findWithDetailsById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RackRow not found with id: " + id));

        return mapToResponse(rackRow);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<RackRowResponse> getAllRackRows() {

        return rackRowRepository.findAllWithDetails()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DELETE_RACKROW")
    public void deleteRackRow(Long id) {

        RackRow rackRow = rackRowRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RackRow not found with id: " + id));

        rackRowRepository.delete(rackRow);
    }

    private RackRowResponse mapToResponse(RackRow entity) {

        List<RackResponse> rackResponses = entity.getRacks() == null
                ? List.of()
                : entity.getRacks()
                .stream()
                .map(rack -> RackResponse.builder()
                        .id(rack.getId())
                        .rackNumber(rack.getRackNumber())
                        .build())
                .toList();

        return RackRowResponse.builder()
                .id(entity.getId())
                .rowName(entity.getRowName())
                .dataCenterId(
                        entity.getDataCenter() != null
                                ? entity.getDataCenter().getId()
                                : null
                )
                .racks(rackResponses)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .build();
    }
}