package com.example.DataCenterManagementSystem.controller.dcim;

import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.dcim.CreateEquipmentRequest;
import com.example.DataCenterManagementSystem.dto.dcim.EquipmentResponse;
import com.example.DataCenterManagementSystem.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Operation(summary = "Create a new equipment and place in rack")
    @PostMapping
    public ResponseEntity<ApiResponse<EquipmentResponse>> createEquipment(@Valid @RequestBody CreateEquipmentRequest request) {
        EquipmentResponse created = equipmentService.createEquipment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    @Operation(summary = "Get equipment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EquipmentResponse>> getEquipmentById(@PathVariable Long id) {
        EquipmentResponse response = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all equipments")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EquipmentResponse>>> getAllEquipments() {
        List<EquipmentResponse> responses = equipmentService.getAllEquipments();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "Delete equipment by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
