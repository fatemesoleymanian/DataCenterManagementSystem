package com.example.DataCenterManagementSystem.controller.crm;

import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.crm.CreateCustomerServiceRequest;
import com.example.DataCenterManagementSystem.dto.crm.CustomerServiceResponse;
import com.example.DataCenterManagementSystem.dto.crm.RenewCustomerServiceRequest;
import com.example.DataCenterManagementSystem.service.CustomerServiceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-services")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceService customerServiceService;

    @Operation(summary = "Create a new customer service")
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerServiceResponse>> createCustomerService
            (@Valid @RequestBody
             CreateCustomerServiceRequest request) {
        CustomerServiceResponse created = customerServiceService.createCustomerService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    @Operation(summary = "Renew customer service")
    @PutMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<CustomerServiceResponse>> renewCustomerService
            (@Valid @RequestBody
             RenewCustomerServiceRequest request) {
        CustomerServiceResponse created = customerServiceService.renewService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    @Operation(summary = "Get customer service by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerServiceResponse>> getCustomerServiceById(@PathVariable Long id) {
        CustomerServiceResponse response = customerServiceService.getCustomerServiceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all customer services")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerServiceResponse>>> getAllCustomerServices() {
        List<CustomerServiceResponse> responses = customerServiceService.getAllCustomerServices();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "Delete customer service by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerService(@PathVariable Long id) {
        customerServiceService.deleteCustomerService(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
