package com.example.DataCenterManagementSystem.controller.dcim;

import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.dcim.CreateRackRequest;
import com.example.DataCenterManagementSystem.dto.dcim.RackResponse;
import com.example.DataCenterManagementSystem.service.RackService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/racks")
@RequiredArgsConstructor
@CrossOrigin
public class RackController {

    private final RackService rackService;

    @Operation(summary = "Create a new rack", description = "Creates a new rack entry and assigns it to a data center.")
    @PostMapping
    public ResponseEntity<ApiResponse<RackResponse>> createRack(
            @Valid @RequestBody CreateRackRequest request) {

        RackResponse created = rackService.createRack(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created));
    }

    @Operation(summary = "Get rack by ID", description = "Retrieves details for a specific rack using its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RackResponse>> getRackById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(rackService.getRackById(id)));
    }

    @Operation(summary = "Get all racks", description = "Returns a list of all available racks.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RackResponse>>> getAllRacks() {

        return ResponseEntity.ok(
                ApiResponse.success(rackService.getAllRacks()));
    }

    @Operation(summary = "Delete rack", description = "Permanently removes a rack record from the system.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRack(@PathVariable Long id) {

        rackService.deleteRack(id);
        return ResponseEntity.noContent().build();
    }
}