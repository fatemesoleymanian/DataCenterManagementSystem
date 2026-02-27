package com.example.DataCenterManagementSystem.service;

import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.dcim.CreateLocationRequest;
import com.example.DataCenterManagementSystem.dto.dcim.LocationResponse;
import com.example.DataCenterManagementSystem.entity.dcim.Location;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CREATE_LOCATION")
    public LocationResponse createLocation(CreateLocationRequest request) {

        Location parent = null;
        int level;

        if (request.parentId() != null) {
            parent = locationRepository.findById(request.parentId())
                    .orElseThrow(() -> new NotFoundException("Parent location not found"));

            level = parent.getLevel() + 1;

            if (level > 4)
                throw new IllegalArgumentException("Location level cannot exceed 4");

        } else {
            if (request.level() == null || request.level() != 1)
                throw new IllegalArgumentException("Root location must be level 1");

            level = 1;
        }

        Location location = Location.builder()
                .name(request.name())
                .level(level)
                .parent(parent)
                .build();

        locationRepository.save(location);

        return mapToResponse(location);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public LocationResponse getLocationById(Long id) {

        Location location = locationRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NotFoundException("Location not found"));

        return mapToResponse(location);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<LocationResponse> getAllLocations() {

        return locationRepository.findAllWithRelations()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DELETE_LOCATION")
    public void deleteLocation(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location not found"));

        locationRepository.delete(location);
    }

    private LocationResponse mapToResponse(Location entity) {

        List<Long> childrenIds = Optional.ofNullable(entity.getChildren())
                .orElse(Collections.emptyList())
                .stream()
                .map(Location::getId)
                .toList();

        return new LocationResponse(
                entity.getId(),
                entity.getName(),
                entity.getLevel(),
                entity.getParent() != null ? entity.getParent().getId() : null,
                childrenIds,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getLastModifiedBy()
        );
    }
}