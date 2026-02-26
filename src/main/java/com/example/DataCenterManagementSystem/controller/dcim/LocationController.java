package com.example.DataCenterManagementSystem.controller.dcim;


import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.dcim.CreateLocationRequest;
import com.example.DataCenterManagementSystem.dto.dcim.LocationResponse;
import com.example.DataCenterManagementSystem.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@CrossOrigin
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "Create a new location")
    @PostMapping
    public ResponseEntity<ApiResponse<LocationResponse>> createLocation(
            @Valid @RequestBody CreateLocationRequest request) {

        LocationResponse response = locationService.createLocation(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "Get location by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponse>> getLocationById(@PathVariable Long id) {
        LocationResponse dto = locationService.getLocationById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @Operation(summary = "Get all locations")
    @GetMapping
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getAllLocations() {
        List<LocationResponse> dtos = locationService.getAllLocations();
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @Operation(summary = "Delete location by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
