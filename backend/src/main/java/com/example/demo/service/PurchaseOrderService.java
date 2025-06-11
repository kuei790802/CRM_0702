package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PurchaseOrderCreateDTO;
import com.example.demo.dto.PurchaseOrderDetailCreateDTO;
import com.example.demo.entity.PurchaseOrder;
import com.example.demo.entity.PurchaseOrderDetail;
<<<<<<< Updated upstream
import com.example.demo.entity.Warehouse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;

=======
import com.example.demo.enums.PurchaseOrderStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;
>>>>>>> Stashed changes
import com.example.demo.repository.PurchaseOrderRepository;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.repository.WarehouseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrderCreateDTO podto, Long userId) {
        supplierRepository.findById(podto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為" + podto.getSupplierId() + "的廠商"));

        // It will looks like this if without the "->"
        // .orElseThrow(new Supplier<RuntimeException>() {
        // @Override
        // public RuntimeException get() {
        // return new
        // ResourceNotFoundException("找不到ID為"+dto.getSupplierId()+"的供應商");}});

        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setSupplierId(podto.getSupplierId());
        newOrder.setOrderDate(podto.getOrderDate());
        newOrder.setCurrency(podto.getCurrency());
        newOrder.setRemarks(podto.getRemarks());

<<<<<<< Updated upstream
        newOrder.setStatus("DRAFT");
=======
        newOrder.setStatus(PurchaseOrderStatus.DRAFT);
>>>>>>> Stashed changes

        newOrder.setCreatedBy(userId);
        newOrder.setUpdatedBy(userId);
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setUpdatedAt(LocalDateTime.now());

        BigDecimal totalNetAmount = BigDecimal.ZERO;
        for (PurchaseOrderDetailCreateDTO detailDTO : podto.getDetails()) {
            productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("找不到ID為" + detailDTO.getProductId() + "的商品"));

            warehouseRepository.findById(detailDTO.getWarehouseId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("找不到 ID 為 " + detailDTO.getWarehouseId() + " 的倉庫"));

            PurchaseOrderDetail detail = new PurchaseOrderDetail();
            detail.setProductId(detailDTO.getProductId());
            detail.setQuantity(detailDTO.getQuantity());
            detail.setUnitPrice(detailDTO.getUnitPrice());
            detail.setWarehouseId(detailDTO.getWarehouseId());
            detail.setGift(false);

            BigDecimal itemNetAmount = detail.getUnitPrice().multiply(detailDTO.getQuantity());
            detail.setItemNetAmount(itemNetAmount);

            BigDecimal taxRate = new BigDecimal("0.05");
            BigDecimal itemTaxAmount = itemNetAmount.multiply(taxRate);
            detail.setItemTaxAmount(itemTaxAmount);

            detail.setItemAmount(itemNetAmount.add(itemTaxAmount));

            detail.setCreatedBy(userId);
            detail.setUpdatedBy(userId);
            detail.setCreatedAt(LocalDateTime.now());
            detail.setUpdatedAt(LocalDateTime.now());

            newOrder.addDetail(detail);

            totalNetAmount = totalNetAmount.add(itemNetAmount);
        }
        BigDecimal taxRate = new BigDecimal("0.05");
        newOrder.setTotalNetAmount(totalNetAmount);
        BigDecimal totalTaxAmount = totalNetAmount.multiply(taxRate);
        newOrder.setTotalTaxAmount(totalTaxAmount);
        newOrder.setTotalAmount(totalNetAmount.add(totalTaxAmount));

        String orderNumber = "PO-" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "-" + (purchaseOrderRepository.count() + 1);

        newOrder.setOrderNumber(orderNumber);

        return purchaseOrderRepository.save(newOrder);
    }

}
