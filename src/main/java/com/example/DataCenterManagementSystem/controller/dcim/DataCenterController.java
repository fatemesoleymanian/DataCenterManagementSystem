package com.example.DataCenterManagementSystem.controller.dcim;

import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.dcim.CreateDataCenterRequest;
import com.example.DataCenterManagementSystem.dto.dcim.DataCenterResponse;
import com.example.DataCenterManagementSystem.service.DataCenterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/datacenters")
@RequiredArgsConstructor
@CrossOrigin
public class DataCenterController {

    private final DataCenterService dataCenterService;

    @Operation(summary = "Create a new data center", description = "Registers a new data center in the system.")
    @PostMapping
    public ResponseEntity<ApiResponse<DataCenterResponse>> createDataCenter(
            @Valid @RequestBody CreateDataCenterRequest request) {

        DataCenterResponse created = dataCenterService.createDataCenter(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created));
    }

    @Operation(summary = "Get data center by ID", description = "Retrieves detailed information about a specific data center using its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DataCenterResponse>> getDataCenterById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(dataCenterService.getDataCenterById(id)));
    }

    @Operation(summary = "Get all data centers", description = "Returns a complete list of all registered data centers.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DataCenterResponse>>> getAllDataCenters() {

        return ResponseEntity.ok(
                ApiResponse.success(dataCenterService.getAllDataCenters()));
    }

    @Operation(summary = "Delete data center", description = "Permanently removes a data center record from the system by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataCenter(@PathVariable Long id) {

        dataCenterService.deleteDataCenter(id);
        return ResponseEntity.noContent().build();
    }
}