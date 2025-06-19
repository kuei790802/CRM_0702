package com.example.demo.service;

import com.example.demo.dto.SalesOrderCreateDTO;
import com.example.demo.dto.SalesOrderDetailCreateDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.SalesOrder;
import com.example.demo.entity.SalesOrderDetail;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.SalesOrderStatus;
import com.example.demo.exception.DataConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BCustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SalesOrderRepository;
import io.swagger.v3.oas.annotations.servers.Server;
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
    private final BCustomerRepository bCustomerRepository;

    @Transactional
    public SalesOrder createSalesOrder(SalesOrderCreateDTO dto, Long userId){



            BCustomerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + dto.getCustomerId() + " 的客戶"));


            SalesOrder newOrder = new SalesOrder();
            newOrder.setCustomerId(dto.getCustomerId());
            newOrder.setOrderDate(dto.getOrderDate());
            newOrder.setShippingAddress(dto.getShippingAddress());
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

                if (!product.getIsActive()) {
                    throw new DataConflictException("產品 '" + product.getName() + "' 為非啟用狀態，無法銷售。");
                }
                if (!product.isSalable()) {
                    throw new DataConflictException("產品 '" + product.getName() + "' 為不可銷售品項。");
                }

                SalesOrderDetail detail = new SalesOrderDetail();
                detail.setProductId(product.getProductId());
                detail.setQuantity(detailDTO.getQuantity());
                detail.setUnitPrice(detailDTO.getUnitPrice());
                detail.setItemSequence(sequence++);
                detail.setDiscountRate(BigDecimal.ZERO); // 折扣預設為0

                // 計算金額
                BigDecimal itemAmount = detail.getUnitPrice().multiply(detail.getQuantity());
                detail.setItemAmount(itemAmount);
                // 假設銷售價為含稅價，我們要反推未稅額與稅額
                BigDecimal taxRateDivisor = new BigDecimal("1.05"); // 假設稅率為 5%
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

            // 4. 計算主檔總金額
            newOrder.setTotalNetAmount(totalNetAmount);
            BigDecimal totalTaxAmount = totalNetAmount.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
            newOrder.setTotalTaxAmount(totalTaxAmount);
            newOrder.setTotalAmount(totalNetAmount.add(totalTaxAmount));

            // 5. 產生唯一的訂單號
            String orderNumber = "SO-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + (salesOrderRepository.count() + 1);
            newOrder.setOrderNumber(orderNumber);

            // 6. 儲存到資料庫
            return salesOrderRepository.save(newOrder);
        }
    }