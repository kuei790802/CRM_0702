package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Inventory;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.Product;
import com.example.demo.entity.PurchaseOrder;
import com.example.demo.entity.PurchaseOrderDetail;
import com.example.demo.entity.Users;
import com.example.demo.entity.Warehouse;
import com.example.demo.enums.MovementType;
import com.example.demo.enums.PurchaseOrderStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.PurchaseOrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class InventoryService {

    
    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    
    @Transactional
    public void receivePurchaseOrder(Long orderId, Long receivingUserId) {
        
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + orderId + " 的採購單"));

        Users receivingUser = userRepository.findById(receivingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + receivingUserId + " 的使用者"));

        
        if (purchaseOrder.getStatus() == PurchaseOrderStatus.RECEIVED
                || purchaseOrder.getStatus() == PurchaseOrderStatus.CANCELLED) {
            throw new IllegalStateException("採購單 " + purchaseOrder.getOrderNumber() + " 已完成或已取消，無法執行入庫。");
        }

        
        for (PurchaseOrderDetail detail : purchaseOrder.getDetails()) {
            this.adjustInventory(
                    detail.getProduct().getProductId(),
                    detail.getWarehouseId(),
                    detail.getQuantity().intValue(),
                    MovementType.PURCHASE_IN,
                    "PURCHASE_ORDER",
                    purchaseOrder.getPurchaseOrderId(),
                    detail.getItemId());
        }

        
        purchaseOrder.setStatus(PurchaseOrderStatus.RECEIVED);
        purchaseOrder.setUpdatedAt(LocalDateTime.now());
        purchaseOrder.setUpdatedBy(receivingUser.getUserId());

        purchaseOrderRepository.save(purchaseOrder);
    }

    
    public List<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProduct_ProductId(productId);
    }

   
    @Transactional
    public Inventory adjustInventory(Long productId, Long warehouseId,
            int quantity, MovementType movementType, String documentType,
            Long documentId, Long documentItemId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + productId + " 的產品"));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + warehouseId + " 的倉庫"));

        Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setProduct(product);
                    newInventory.setWarehouse(warehouse);
                    newInventory.setCurrentStock(BigDecimal.ZERO);
                    return newInventory;
                });

        int currentQuantity = inventory.getCurrentStock().intValue();
        int newQuantity = currentQuantity + quantity;

        if (newQuantity < 0) {
            throw new IllegalArgumentException("庫存不足。產品 '" + product.getName() + "' 在倉庫 '" + warehouse.getName()
                    + "' 的目前庫存為 " + currentQuantity + ", 無法扣減 " + (-quantity));
        }
        inventory.setCurrentStock(BigDecimal.valueOf(newQuantity));
        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setMovementType(movementType.name());
        movement.setQuantityChange(BigDecimal.valueOf(quantity));
        movement.setCurrentStockAfterMovement(BigDecimal.valueOf(newQuantity));
        movement.setMovementDate(LocalDateTime.now());
        movement.setDocumentType("PO");
        movement.setDocumentId(documentId);
        movement.setDocumentItemId(documentItemId);

        inventoryMovementRepository.save(movement);

        return updatedInventory;
    }
}
