package com.example.DataCenterManagementSystem.controller.dcim;

import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.dcim.CreateRackRowRequest;
import com.example.DataCenterManagementSystem.dto.dcim.RackRowResponse;
import com.example.DataCenterManagementSystem.service.RackRowService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rack-rows")
@RequiredArgsConstructor
@CrossOrigin
public class RackRowController {

    private final RackRowService rackRowService;

    @Operation(summary = "Create a new rack row", description = "Defines a new rack row within the data center layout.")
    @PostMapping
    public ResponseEntity<ApiResponse<RackRowResponse>> createRackRow(
            @Valid @RequestBody CreateRackRowRequest request) {

        RackRowResponse created = rackRowService.createRackRow(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created));
    }

    @Operation(summary = "Get rack row by ID", description = "Fetches the details of a specific rack row using its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RackRowResponse>> getRackRowById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(rackRowService.getRackRowById(id)));
    }

    @Operation(summary = "Get all rack rows", description = "Retrieves a list of all rack rows configured in the system.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RackRowResponse>>> getAllRackRows() {

        return ResponseEntity.ok(
                ApiResponse.success(rackRowService.getAllRackRows()));
    }

    @Operation(summary = "Delete rack row", description = "Removes a specific rack row from the system by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRackRow(@PathVariable Long id) {

        rackRowService.deleteRackRow(id);
        return ResponseEntity.noContent().build();
    }
}