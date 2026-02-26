package com.example.DataCenterManagementSystem.service;

import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.dcim.CreateEquipmentRequest;
import com.example.DataCenterManagementSystem.dto.dcim.EquipmentResponse;
import com.example.DataCenterManagementSystem.entity.dcim.Equipment;
import com.example.DataCenterManagementSystem.entity.dcim.Port;
import com.example.DataCenterManagementSystem.entity.dcim.Rack;
import com.example.DataCenterManagementSystem.entity.dcim.RackUnit;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.exception.UnitOccupiedException;
import com.example.DataCenterManagementSystem.repository.EquipmentRepository;
import com.example.DataCenterManagementSystem.repository.RackRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final RackRepository rackRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CREATE_EQUIPMENT")
    public EquipmentResponse createEquipment(CreateEquipmentRequest request) {
        Rack rack = rackRepository.findByIdWithUnitsForUpdate(request.rackId())
                .orElseThrow(() -> new NotFoundException("Rack not found with id: " + request.rackId()));

        int start = request.startUnit();
        int size = request.unitSize();
        int maxUnits = rack.getMaxUnits();

        if (start < 1 || start + size - 1 > maxUnits) {
            throw new IllegalArgumentException("Invalid start unit or size for rack");
        }

        List<RackUnit> targetUnits = rack.allocateUnits(request.startUnit(), request.unitSize());

        if (targetUnits.size() != size) {
            throw new IllegalStateException("Units not initialized properly");
        }

        for (RackUnit unit : targetUnits) {
            if (unit.isOccupied()) {
                throw new UnitOccupiedException("Unit " + unit.getUnitNumber() + " is occupied");
            }
        }

        Equipment equipment = Equipment.builder()
                .modelName(request.modelName())
                .type(request.type())
                .unitSize(size)
                .build();

        // Create ports if portCount > 0
        if (request.portCount() > 0) {
            for (int i = 1; i <= request.portCount(); i++) {
                Port port = Port.builder()
                        .portNumber(String.valueOf(i))
                        .equipment(equipment)
                        .build();
                equipment.getPorts().add(port);
            }
        }

        // Assign units
        for (RackUnit unit : targetUnits) {
            unit.setOccupiedBy(equipment);
            equipment.getOccupiedUnits().add(unit);
        }

        // No need for rack.getEquipments().add(equipment); - relationship is through units

        equipment = equipmentRepository.save(equipment);
        return mapToResponse(equipment);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public EquipmentResponse getEquipmentById(Long id) {
        // Use repository with JOIN FETCH to avoid lazy issues
        return equipmentRepository.findByIdWithDetails(id) // Implement in repo: JOIN FETCH occupiedUnits, ports, rack
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Equipment not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentRepository.findAllWithDetails()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DELETE_EQUIPMENT")
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipment not found with id: " + id));
        // Clear occupied units before delete
        equipment.getOccupiedUnits().forEach(unit -> unit.setOccupiedBy(null));
        equipmentRepository.delete(equipment);
    }

    // Mapper method (safe inside transaction)
    private EquipmentResponse mapToResponse(Equipment entity) {

        Long rackId = null;

        if (!entity.getOccupiedUnits().isEmpty()) {
            rackId = entity.getOccupiedUnits()
                    .get(0)
                    .getRack()
                    .getId();
        }

        return new EquipmentResponse(
                entity.getId(),
                entity.getModelName(),
                entity.getType(),
                entity.getUnitSize(),
                entity.getOccupiedUnits()
                        .stream()
                        .map(RackUnit::getId)
                        .toList(),
                rackId,
                entity.getPorts()
                        .stream()
                        .map(Port::getId)
                        .toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getLastModifiedBy()

        );
    }
}