package com.example.demo.service;

import com.example.demo.dto.SalesOrderCreateDTO;
import com.example.demo.dto.SalesOrderDetailCreateDTO;
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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            throw new DataConflictException("客戶 '" + customer.getName() + "' 為非啟用狀態，無法建立訂單。");
        }



        SalesOrder newOrder = new SalesOrder();
        newOrder.setCustomer(customer);
        newOrder.setOrderDate(dto.getOrderDate());
        newOrder.setShippingAddress(dto.getShippingAddress());
        newOrder.setShippingMethod(dto.getShippingMethod());
        newOrder.setPaymentMethod(dto.getPaymentMethod());
        newOrder.setRemarks(dto.getRemarks());


        newOrder.setOrderStatus(SalesOrderStatus.CONFIRMED);
        newOrder.setPaymentStatus(PaymentStatus.NONPAYMENT);


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

            if (!product.getIsActive() || !product.isSalable()) {
                throw new DataConflictException("產品 '" + product.getName() + "' 非啟用或不可銷售狀態。");
            }

            SalesOrderDetail detail = new SalesOrderDetail();
            detail.setProduct(product);
            detail.setProductId(detailDTO.getProductId());
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
        newOrder.setTotalAmount(totalNetAmount.add(totalTaxAmount));


        String orderNumber = "SO-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + (salesOrderRepository.count() + 1);
        newOrder.setOrderNumber(orderNumber);


        return salesOrderRepository.save(newOrder);
    }
}