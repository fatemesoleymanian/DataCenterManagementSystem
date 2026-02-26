package com.example.DataCenterManagementSystem.service;

import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.dcim.CreateRackUnitRequest;
import com.example.DataCenterManagementSystem.dto.dcim.RackUnitResponse;
import com.example.DataCenterManagementSystem.entity.dcim.Rack;
import com.example.DataCenterManagementSystem.entity.dcim.RackUnit;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.repository.RackRepository;
import com.example.DataCenterManagementSystem.repository.RackUnitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RackUnitService {

    private final RackUnitRepository rackUnitRepository;
    private final RackRepository rackRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CREATE_RACKUNIT")
    public RackUnitResponse createRackUnit(CreateRackUnitRequest request) {
        Rack rack = rackRepository.findById(request.rackId())
                .orElseThrow(() -> new NotFoundException("Rack not found with id: " + request.rackId()));

        if (request.unitNumber() > rack.getMaxUnits()) {
            throw new IllegalArgumentException("Unit number exceeds rack max units");
        }

        RackUnit rackUnit = RackUnit.builder()
                .unitNumber(request.unitNumber())
                .rack(rack)
                .build();

        rackUnit = rackUnitRepository.save(rackUnit);
        return mapToResponse(rackUnit);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public RackUnitResponse getRackUnitById(Long id) {
        // Use repository with JOIN FETCH to avoid lazy issues
        return rackUnitRepository.findByIdWithRack(id) // Implement this in repo with @Query + JOIN FETCH
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("RackUnit not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<RackUnitResponse> getAllRackUnits() {
        return rackUnitRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DELETE_RACKUNIT")
    public void deleteRackUnit(Long id) {
        RackUnit rackUnit = rackUnitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RackUnit not found with id: " + id));
        rackUnitRepository.delete(rackUnit);
    }

    // Mapper method (safe inside transaction)
    private RackUnitResponse mapToResponse(RackUnit entity) {
        return new RackUnitResponse(
                entity.getId(),
                entity.getUnitNumber(),
                entity.getOccupiedBy() != null ? entity.getOccupiedBy().getId() : null,
                entity.getRack().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getLastModifiedBy()
        );
    }
}
