package com.example.DataCenterManagementSystem.service;

import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.dcim.CreateRackRequest;
import com.example.DataCenterManagementSystem.dto.dcim.RackResponse;
import com.example.DataCenterManagementSystem.entity.dcim.Rack;
import com.example.DataCenterManagementSystem.entity.dcim.RackRow;
import com.example.DataCenterManagementSystem.entity.dcim.RackUnit;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.repository.RackRepository;
import com.example.DataCenterManagementSystem.repository.RackRowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RackService {

    private final RackRepository rackRepository;
    private final RackRowRepository rackRowRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CREATE_RACK")
    public RackResponse createRack(CreateRackRequest request) {

        RackRow rackRow = rackRowRepository.findById(request.getRackRowId())
                .orElseThrow(() -> new NotFoundException("RackRow not found with id: " + request.getRackRowId()));

        Rack rack = Rack.builder()
                .rackNumber(request.getRackNumber())
                .rackRow(rackRow)
                .build();

        rack = rackRepository.save(rack);

        return mapToResponse(rack);
    }


    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public RackResponse getRackById(Long id) {
        Rack rack = rackRepository.findWithDetailsById(id)
                .orElseThrow(() -> new NotFoundException("Rack not found with id: " + id));

        return mapToResponse(rack);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<RackResponse> getAllRacks() {
        return rackRepository.findAllWithDetails()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DELETE_RACK")
    public void deleteRack(Long id) {
        Rack rack = rackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rack not found with id: " + id));

        rackRepository.delete(rack);
    }


    private RackResponse mapToResponse(Rack entity) {


        return RackResponse.builder()
                .id(entity.getId())
                .rackNumber(entity.getRackNumber())
                .rackRowId(entity.getRackRow().getId())
                .maxUnits(entity.getMaxUnits())
                .unitIds(entity.getUnits().stream()
                        .map(RackUnit::getId)
                        .toList())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .build();
    }
}