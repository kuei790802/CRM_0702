package com.example.demo.service.erp;

import com.example.demo.dto.erp.*;
import com.example.demo.entity.CustomerBase;
import com.example.demo.entity.Product;
import com.example.demo.entity.SalesOrder;
import com.example.demo.entity.SalesOrderDetail;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.SalesOrderStatus;
import com.example.demo.exception.DataConflictException;
import com.example.demo.exception.ResourceNotFoundException;

import com.example.demo.repository.CustomerBaseRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SalesOrderRepository;

import com.example.demo.specification.SalesOrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SalesOrderService {
    private final SalesOrderRepository salesOrderRepository;
    private final ProductRepository productRepository;
    private final CustomerBaseRepository customerBaseRepository;

    @Transactional
    public SalesOrder createSalesOrder(SalesOrderCreateDTO dto, Long userId){

        CustomerBase customer = customerBaseRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + dto.getCustomerId() + " 的客戶"));

        if (!Boolean.TRUE.equals(customer.isActive())) {
            throw new DataConflictException("客戶 '" + customer.getCustomerName() + "' 為非啟用狀態，無法建立訂單。");
        }



        SalesOrder newOrder = new SalesOrder();
        newOrder.setCustomer(customer);
        newOrder.setOrderDate(dto.getOrderDate());
        newOrder.setShippingAddress(dto.getShippingAddress());
        newOrder.setShippingMethod(dto.getShippingMethod());
        newOrder.setPaymentMethod(dto.getPaymentMethod());
        newOrder.setRemarks(dto.getRemarks());


        newOrder.setOrderStatus(SalesOrderStatus.CONFIRMED);
        newOrder.setPaymentStatus(PaymentStatus.UNPAID);


        LocalDateTime now = LocalDateTime.now();
        newOrder.setCreatedBy(userId);
        newOrder.setUpdatedBy(userId);
        newOrder.setCreatedAt(now);
        newOrder.setUpdatedAt(now);


        BigDecimal totalNetAmount = BigDecimal.ZERO;
        int sequence = 1;
        for (SalesOrderDetailCreateDTO detailDTO : dto.getDetails()) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + detailDTO.getProductId() + " 的產品"));

            if (!product.getIsActive() || !product.getIsSalable()) {
                throw new DataConflictException("產品 '" + product.getName() + "' 非啟用或不可銷售狀態。");
            }

            SalesOrderDetail detail = new SalesOrderDetail();
            detail.setProduct(product);
            detail.setQuantity(detailDTO.getQuantity());
            detail.setUnitPrice(detailDTO.getUnitPrice());
            detail.setItemSequence(sequence++);
            detail.setDiscountRate(BigDecimal.ZERO);


            if (product.getUnit() != null) {
                detail.setUnitId(product.getUnit().getUnitId());
            }

            BigDecimal itemAmount = detail.getUnitPrice().multiply(detail.getQuantity());
            detail.setItemAmount(itemAmount);

            BigDecimal taxRateDivisor = new BigDecimal("1.05");
            BigDecimal itemNetAmount = itemAmount.divide(taxRateDivisor, 2, RoundingMode.HALF_UP);
            detail.setItemNetAmount(itemNetAmount);
            detail.setItemTaxAmount(itemAmount.subtract(itemNetAmount));

            detail.setCreatedBy(userId);
            detail.setUpdatedBy(userId);
            detail.setCreatedAt(now);
            detail.setUpdatedAt(now);

            newOrder.addDetail(detail);
            totalNetAmount = totalNetAmount.add(itemNetAmount);
        }


        newOrder.setTotalNetAmount(totalNetAmount);
        BigDecimal totalTaxAmount = totalNetAmount.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        newOrder.setTotalTaxAmount(totalTaxAmount);
        newOrder.setTotalAmount(newOrder.getTotalNetAmount().add(newOrder.getTotalTaxAmount()));


        String orderNumber = "SO-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + (salesOrderRepository.count() + 1);
        newOrder.setOrderNumber(orderNumber);


        return salesOrderRepository.save(newOrder);
    }

    @Transactional
    public SalesOrderViewDTO updateSalesOrder(Long orderId, SalesOrderUpdateDTO updateDTO, Long userId) {
        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + orderId + " 的銷售訂單"));

        // Validate order status before update
        if (order.getOrderStatus() == SalesOrderStatus.SHIPPED ||
                order.getOrderStatus() == SalesOrderStatus.DELIVERED ||
                order.getOrderStatus() == SalesOrderStatus.CANCELLED) {
            throw new DataConflictException("訂單狀態為 " + order.getOrderStatus() + "，無法修改。");
        }

        // Customer cannot be changed, so not updating customerId from DTO.
        // If it were allowed, fetch and set customer here.
        // CustomerBase customer = customerBaseRepository.findById(updateDTO.getCustomerId())
        // .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + updateDTO.getCustomerId() + " 的客戶"));
        // order.setCustomer(customer);


        order.setOrderDate(updateDTO.getOrderDate());
        order.setShippingAddress(updateDTO.getShippingAddress());
        order.setShippingMethod(updateDTO.getShippingMethod());
        order.setPaymentMethod(updateDTO.getPaymentMethod());
        order.setRemarks(updateDTO.getRemarks());

        // Potentially update orderStatus or paymentStatus if they are part of DTO and logic allows
        // if (updateDTO.getOrderStatus() != null) {
        //     order.setOrderStatus(updateDTO.getOrderStatus());
        // }
        // if (updateDTO.getPaymentStatus() != null) {
        //     order.setPaymentStatus(updateDTO.getPaymentStatus());
        // }

        order.setUpdatedBy(userId);
        order.setUpdatedAt(LocalDateTime.now());

        // Clear existing details and add new ones
        order.getDetails().clear(); // Requires CascadeType.ALL and orphanRemoval=true

        BigDecimal totalNetAmount = BigDecimal.ZERO;
        int sequence = 1;
        for (SalesOrderDetailUpdateDTO detailDTO : updateDTO.getDetails()) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + detailDTO.getProductId() + " 的產品"));

            if (!product.getIsActive() || !product.getIsSalable()) {
                throw new DataConflictException("產品 '" + product.getName() + "' 非啟用或不可銷售狀態。");
            }

            SalesOrderDetail detail = new SalesOrderDetail();
            detail.setProduct(product);
            detail.setQuantity(detailDTO.getQuantity());
            detail.setUnitPrice(detailDTO.getUnitPrice());
            detail.setItemSequence(sequence++);
            detail.setDiscountRate(BigDecimal.ZERO); // Assuming discount is not part of update DTO for now

            if (product.getUnit() != null) {
                detail.setUnitId(product.getUnit().getUnitId());
            }

            BigDecimal itemAmount = detail.getUnitPrice().multiply(detail.getQuantity());
            detail.setItemAmount(itemAmount);

            // Consistent tax calculation with createSalesOrder
            BigDecimal taxRateDivisor = new BigDecimal("1.05");
            BigDecimal itemNetAmountCal = itemAmount.divide(taxRateDivisor, 2, RoundingMode.HALF_UP);
            detail.setItemNetAmount(itemNetAmountCal);
            detail.setItemTaxAmount(itemAmount.subtract(itemNetAmountCal));

            detail.setCreatedBy(userId); // Should this be original creator or current user?
            detail.setUpdatedBy(userId); // Current user
            detail.setCreatedAt(LocalDateTime.now()); // Should this be original item creation time?
            detail.setUpdatedAt(LocalDateTime.now()); // Current time

            order.addDetail(detail);
            totalNetAmount = totalNetAmount.add(itemNetAmountCal);
        }

        order.setTotalNetAmount(totalNetAmount);
        BigDecimal totalTaxAmount = totalNetAmount.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        order.setTotalTaxAmount(totalTaxAmount);
        order.setTotalAmount(order.getTotalNetAmount().add(order.getTotalTaxAmount()));

        SalesOrder updatedOrder = salesOrderRepository.save(order);
        return SalesOrderViewDTO.fromEntity(updatedOrder);
    }
}


    public Page<SalesOrderSummaryDTO> searchSalesOrders(
            Long customerId, SalesOrderStatus status,
            LocalDate startDate, LocalDate endDate,
            String keyword, Pageable pageable) {

        Specification<SalesOrder> spec = SalesOrderSpecification.findByCriteria(
                customerId, status, startDate, endDate, keyword);

        Page<SalesOrder> orderPage = salesOrderRepository.findAll(spec, pageable);

        return orderPage.map(SalesOrderSummaryDTO::fromEntity);
    }

    public SalesOrderViewDTO getSalesOrderById(Long orderId){
        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("找不到 ID 為 " + orderId + " 的銷售訂單"));

        return SalesOrderViewDTO.fromEntity(order);
    }

    @Transactional
    public SalesOrderViewDTO deleteSalesOrder(Long orderId, Long userId) {
        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + orderId + " 的銷售訂單"));

        if (order.getOrderStatus() != SalesOrderStatus.CONFIRMED) {
            throw new DataConflictException("訂單狀態為 " + order.getOrderStatus() + "，無法取消。只有已確認的訂單才能被取消。");
        }

        order.setOrderStatus(SalesOrderStatus.CANCELLED);
        order.setUpdatedBy(userId);
        order.setUpdatedAt(LocalDateTime.now());

        SalesOrder cancelledOrder = salesOrderRepository.save(order);

        return SalesOrderViewDTO.fromEntity(cancelledOrder);
    }
}