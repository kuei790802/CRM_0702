package com.example.demo.config.util;


import com.example.demo.dto.erp.SalesOrderCreateDTO;
import com.example.demo.dto.erp.SalesOrderDetailCreateDTO;
import com.example.demo.entity.BCustomer;
import com.example.demo.entity.CCustomer;
import com.example.demo.entity.Product;
import com.example.demo.repository.CCustomerRepo;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.BCustomerService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class SalesOrderFaker {

    private final Faker faker;
    private final Random random = new Random();

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CCustomerRepo cCustomerRepo;
    //    @Autowired
//    private BCustomerRepository bCustomerRepository;
    @Autowired
    private BCustomerService bCustomerService;


    public SalesOrderFaker() {
        this.faker = new Faker(new Locale("zh-TW"));
    }

    public SalesOrderCreateDTO generateFakeSalesOrderCreateDTO(List<Long> allCustomerIds, List<Long> productIds, CCustomerRepo cCustomerRepo, BCustomerService bCustomerService) {
        if (allCustomerIds.isEmpty() || productIds.isEmpty()) {
            throw new IllegalArgumentException("Customer and Product IDs must be provided.");
        }

        SalesOrderCreateDTO dto = new SalesOrderCreateDTO();

        Long randomCustomerId = allCustomerIds.get(random.nextInt(allCustomerIds.size()));
        dto.setCustomerId(randomCustomerId);

        // Determine if customer is B2B or B2C to fetch appropriate address and name
        // This is a simplified approach. A more robust solution might involve a CustomerService
        // that can return a generic Customer DTO or handle this logic.
        // String customerName; // Not directly needed for DTO population
        String customerAddress;
        // String customerEmail; // Not directly needed for DTO population

        // Try fetching as CCustomer first
        CCustomer cCustomer = cCustomerRepo.findById(randomCustomerId).orElse(null);
        if (cCustomer != null) {
            // customerName = cCustomer.getCustomerName();
            customerAddress = cCustomer.getAddress();
            // customerEmail = cCustomer.getEmail();
            // dto.setCustomerType("B2C"); // Not a field in SalesOrderCreateDTO
        } else {
            // Try fetching as BCustomer if not found as CCustomer
            BCustomer bCustomer = bCustomerService.getBCustomerEntityById(randomCustomerId);
            if (bCustomer != null) {
                // customerName = bCustomer.getCustomerName();
                customerAddress = bCustomer.getAddress();
                // customerEmail = bCustomer.getEmail();
                // dto.setCustomerType("B2B"); // Not a field in SalesOrderCreateDTO
            } else {
                // Fallback if customer cannot be fetched - use a generic address
                customerAddress = faker.address().fullAddress();
                // customerName = faker.name().fullName();
                // customerEmail = faker.internet().emailAddress();
                // dto.setCustomerType("UNKNOWN"); // Not a field in SalesOrderCreateDTO
            }
        }

        dto.setOrderDate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        // dto.setCurrency("TWD"); // Not in DTO, service handles this
        dto.setPaymentMethod(faker.options().option("Credit Card", "Bank Transfer", "Cash on Delivery"));
        // dto.setPaymentStatus(faker.options().option(PaymentStatus.class)); // Not in DTO, service handles this
        // dto.setOrderStatus(faker.options().option(SalesOrderStatus.class)); // Not in DTO, service handles this
        dto.setShippingMethod(faker.options().option("Standard Shipping", "Express Shipping", "Local Pickup"));
        dto.setShippingAddress(customerAddress);
        // dto.setEstimatedDeliveryDate(dto.getOrderDate().plusDays(faker.number().numberBetween(3, 14))); // Not in DTO
        dto.setRemarks(faker.lorem().sentence(3));
        // dto.setCreatedBy(1L); // Not in DTO, service handles this
        // dto.setUpdatedBy(1L); // Not in DTO, service handles this

        List<SalesOrderDetailCreateDTO> details = new ArrayList<>();
        int numItems = random.nextInt(5) + 1; // 1 to 5 items per order
        // BigDecimal totalAmount = BigDecimal.ZERO; // Calculated by service
        // BigDecimal totalTax = BigDecimal.ZERO; // Calculated by service

        for (int i = 0; i < numItems; i++) {
            SalesOrderDetailCreateDTO detailDTO = new SalesOrderDetailCreateDTO();
            Long randomProductId = productIds.get(random.nextInt(productIds.size()));
            Product product = productRepository.findById(randomProductId)
                    .orElseThrow(() -> new IllegalStateException("Product not found for ID: " + randomProductId + ". Ensure products are loaded."));

            detailDTO.setProductId(randomProductId);
            detailDTO.setQuantity(BigDecimal.valueOf(random.nextInt(10) + 1)); // 1 to 10 units
            detailDTO.setUnitPrice(product.getBasePrice());

            // Fields like TaxAmount, Subtotal, DiscountAmount are not in SalesOrderDetailCreateDTO
            // They will be calculated by the service based on product, quantity, unitPrice.
            // BigDecimal itemSubtotal = detailDTO.getUnitPrice().multiply(detailDTO.getQuantity());
            // BigDecimal itemTax = itemSubtotal.multiply(BigDecimal.valueOf(0.05));
            // detailDTO.setTaxAmount(itemTax);
            // detailDTO.setSubtotal(itemSubtotal.add(itemTax));
            // detailDTO.setDiscountAmount(BigDecimal.ZERO);

            details.add(detailDTO);
            // totalAmount = totalAmount.add(itemSubtotal.add(itemTax)); // Calculated by service
            // totalTax = totalTax.add(itemTax); // Calculated by service
        }
        dto.setDetails(details);
        // dto.setTotalAmount(totalAmount); // Not in DTO
        // dto.setTotalTaxAmount(totalTax); // Not in DTO
        // dto.setTotalNetAmount(totalAmount.subtract(totalTax)); // Not in DTO

        return dto;
    }

    public List<SalesOrderCreateDTO> generateFakeSalesOrderCreateDTOs(int count, List<Long> allCustomerIds, List<Long> productIds, CCustomerRepo cCustomerRepo, BCustomerService bCustomerService) {
        List<SalesOrderCreateDTO> dtos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dtos.add(generateFakeSalesOrderCreateDTO(allCustomerIds, productIds, cCustomerRepo, bCustomerService));
        }
        return dtos;
    }
}
