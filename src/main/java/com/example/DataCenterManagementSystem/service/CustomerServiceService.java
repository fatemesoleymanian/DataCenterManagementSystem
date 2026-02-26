package com.example.DataCenterManagementSystem.service;


import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.crm.CreateCustomerServiceRequest;
import com.example.DataCenterManagementSystem.dto.crm.CustomerServiceResponse;
import com.example.DataCenterManagementSystem.dto.crm.RenewCustomerServiceRequest;
import com.example.DataCenterManagementSystem.entity.crm.CustomerService;
import com.example.DataCenterManagementSystem.entity.crm.Invoice;
import com.example.DataCenterManagementSystem.entity.crm.InvoiceType;
import com.example.DataCenterManagementSystem.entity.dcim.Equipment;
import com.example.DataCenterManagementSystem.entity.security.User;
import com.example.DataCenterManagementSystem.entity.security.UserRole;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.exception.UnauthorizedException;
import com.example.DataCenterManagementSystem.repository.CustomerServiceRepository;
import com.example.DataCenterManagementSystem.repository.EquipmentRepository;
import com.example.DataCenterManagementSystem.repository.InvoiceRepository;
import com.example.DataCenterManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CustomerServiceService {

    private final CustomerServiceRepository customerServiceRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CREATE_CUSTOMERSERVICE")
    public CustomerServiceResponse createCustomerService(CreateCustomerServiceRequest request) {
        User customer = userRepository.findById(request.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + request.customerId()));

        Equipment server = equipmentRepository.findById(request.serverId())
                .orElseThrow(() -> new NotFoundException("Server not found with id: " + request.serverId()));

        CustomerService customerService = CustomerService.builder()
                .customer(customer)
                .server(server)
                .expiryDate(request.expiryDate())
                .build();

        customerService = customerServiceRepository.save(customerService);

        // ایجاد فاکتور خرید (BUY) اتوماتیک
        // amount از request یا محاسبه (مثل fixed یا بر اساس expiry)
        BigDecimal buyAmount = request.amount();  // فرضاً به request اضافه کردیم
        // اگر محاسبه: buyAmount = calculateAmount(customerService);

        Invoice buyInvoice = Invoice.builder()
                .amount(buyAmount)
                .type(InvoiceType.BUY)
                .customer(customer)
                .build();
        invoiceRepository.save(buyInvoice);

        return mapToResponse(customerService);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public CustomerServiceResponse getCustomerServiceById(Long id) {
        return customerServiceRepository.findByIdWithDetails(id) // Implement in repo with @EntityGraph for customer, server
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("CustomerService not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<CustomerServiceResponse> getAllCustomerServices() {
        return customerServiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DELETE_CUSTOMERSERVICE")
    public void deleteCustomerService(Long id) {
        CustomerService customerService = customerServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CustomerService not found with id: " + id));
        customerServiceRepository.delete(customerService);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @LogActivity(action = "RENEW_SERVICE", target = "#serviceId")
    public CustomerServiceResponse renewService(RenewCustomerServiceRequest request) {
        CustomerService service = customerServiceRepository.findById(request.serviceId())
                .orElseThrow(() -> new NotFoundException("Service not found"));

        // چک اگر customer باشه، فقط سرویس خودش
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        if (user.getRole() == UserRole.CUSTOMER && !service.getCustomer().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only renew your own services");
        }

        service.setExpiryDate(request.newExpiryDate());
        service = customerServiceRepository.save(service);

        // ایجاد فاکتور تمدید
        Invoice invoice = Invoice.builder()
                .amount(request.amount())
                .type(InvoiceType.RENEW)
                .customer(service.getCustomer())
                .build();
        invoiceRepository.save(invoice);

        return mapToResponse(service);
    }

    // Helper برای محاسبه amount اگر نیاز باشه (اختیاری)
//    private BigDecimal calculateAmount(CustomerService service) {
//
//        // مثلاً بر اساس مدت یا نوع سرور
//        long months = ChronoUnit.MONTHS.between(LocalDateTime.now(), service.getExpiryDate());
//        return BigDecimal.valueOf(1000).multiply(BigDecimal.valueOf(months));  // مثال
//    }
    private BigDecimal calculateAmount(CustomerService service) {

        LocalDate now = LocalDate.now();
        LocalDate expiry = service.getExpiryDate().toLocalDate();

        Period period = Period.between(now, expiry);
        int months = period.getYears() * 12 + period.getMonths();

        return BigDecimal.valueOf(1000)
                .multiply(BigDecimal.valueOf(months));
    }

    // Mapper method
    private CustomerServiceResponse mapToResponse(CustomerService entity) {
        return new CustomerServiceResponse(
                entity.getId(),
                entity.getCustomer().getId(),
                entity.getServer().getId(),
                entity.getExpiryDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getLastModifiedBy()
        );
    }
}
