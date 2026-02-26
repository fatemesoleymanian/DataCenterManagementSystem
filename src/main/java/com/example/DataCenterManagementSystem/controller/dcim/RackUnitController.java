package com.example.DataCenterManagementSystem.controller.dcim;

import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.dcim.CreateRackUnitRequest;
import com.example.DataCenterManagementSystem.dto.dcim.RackUnitResponse;
import com.example.DataCenterManagementSystem.service.RackUnitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rack-units")
@RequiredArgsConstructor
@CrossOrigin
public class RackUnitController {

    private final RackUnitService rackUnitService;

    @Operation(summary = "Create a new rack unit")
    @PostMapping
    public ResponseEntity<ApiResponse<RackUnitResponse>> createRackUnit(@Valid @RequestBody CreateRackUnitRequest request) {
        RackUnitResponse created = rackUnitService.createRackUnit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    @Operation(summary = "Get rack unit by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RackUnitResponse>> getRackUnitById(@PathVariable Long id) {
        RackUnitResponse response = rackUnitService.getRackUnitById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all rack units")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RackUnitResponse>>> getAllRackUnits() {
        List<RackUnitResponse> responses = rackUnitService.getAllRackUnits();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "Delete rack unit by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRackUnit(@PathVariable Long id) {
        rackUnitService.deleteRackUnit(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
