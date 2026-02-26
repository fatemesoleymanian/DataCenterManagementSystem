package com.example.DataCenterManagementSystem.controller.crm;

import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.crm.CreateInvoiceRequest;
import com.example.DataCenterManagementSystem.dto.crm.InvoiceResponse;
import com.example.DataCenterManagementSystem.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Operation(summary = "Create a new invoice")
    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceResponse>> createInvoice
            (@Valid @RequestBody CreateInvoiceRequest request) {
        InvoiceResponse created = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    @Operation(summary = "Pay an invoice by ID")
    @PutMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<InvoiceResponse>> payInvoice(@PathVariable Long id) {
        InvoiceResponse paid = invoiceService.payInvoice(id);
        return ResponseEntity.ok(ApiResponse.success(paid));
    }

    @Operation(summary = "Get invoice by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceById(@PathVariable Long id) {
        InvoiceResponse response = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all invoices")
    @GetMapping
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getAllInvoices() {
        List<InvoiceResponse> responses = invoiceService.getAllInvoices();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "Delete invoice by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
