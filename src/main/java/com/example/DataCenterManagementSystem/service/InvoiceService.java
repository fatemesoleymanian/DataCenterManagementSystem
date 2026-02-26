package com.example.DataCenterManagementSystem.service;


import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.crm.CreateInvoiceRequest;
import com.example.DataCenterManagementSystem.dto.crm.InvoiceResponse;
import com.example.DataCenterManagementSystem.entity.crm.Invoice;
import com.example.DataCenterManagementSystem.entity.security.User;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.repository.InvoiceRepository;
import com.example.DataCenterManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CREATE_INVOICE")
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        User customer = userRepository.findById(request.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + request.customerId()));

        Invoice invoice = Invoice.builder()
                .amount(request.amount())
                .type(request.type())
                .customer(customer)
                .build();

        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    @LogActivity(action = "PAY_INVOICE")
    public InvoiceResponse payInvoice(Long id) {
        Invoice invoice = invoiceRepository.findByIdWithCustomer(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found with id: " + id));

        if (invoice.isPaid()) {
            throw new IllegalStateException("Invoice already paid");
        }

        invoice.setPaid(true);
        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public InvoiceResponse getInvoiceById(Long id) {
        return invoiceRepository.findByIdWithCustomer(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Invoice not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DELETE_INVOICE")
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found with id: " + id));
        invoiceRepository.delete(invoice);
    }

    // Mapper method
    private InvoiceResponse mapToResponse(Invoice entity) {
        return new InvoiceResponse(
                entity.getId(),
                entity.getAmount(),
                entity.getType(),
                entity.isPaid(),
                entity.getCustomer().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getLastModifiedBy()
        );
    }
}
