package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Inventory;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.Product;
import com.example.demo.entity.PurchaseOrder;
import com.example.demo.entity.PurchaseOrderDetail;
import com.example.demo.entity.Users;

import com.example.demo.entity.Warehouse;
import com.example.demo.enums.PurchaseOrderStatus;
import com.example.demo.exception.DataConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.PurchaseOrderRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final UserRepository userRepository;

    @Transactional
    public void receivePurchaseOrder(Long purchaseOrderId, Long operatorUserId) {
        
        
        purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + purchaseOrderId + " 的進貨單"));
        userRepository.findById(UserId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + UserId + " 的使用者"));

        
        if (order.getStatus() != PurchaseOrderStatus.CONFIRMED && order.getStatus() != PurchaseOrderStatus.PARTIALLY_RECEIVED) {
            throw new DataConflictException("此進貨單狀態為 " + order.getStatus() + "，不可執行入庫作業。");
        }

        
        for (PurchaseOrderDetail detail : order.getDetails()) {
            
            
            Inventory inventory = inventoryRepository.
            findByProductIdAndWarehouseId(detail.getProductId(), 
            detail.getWarehouseId())
                    .orElseGet(() -> {
                        
                        Inventory newInventory = new Inventory();
                        newInventory.setProduct(new Product());
                        newInventory.getProduct().setProductId(detail.getProductId());
                        newInventory.setWarehouse(new Warehouse());
                        newInventory.getWarehouse().setWarehouseId(detail.getWarehouseId());
                        newInventory.setCurrentStock(BigDecimal.ZERO);
                        newInventory.setAverageCost(BigDecimal.ZERO);
                        
                        newInventory.setCreatedBy(operatorUserId);
                        newInventory.setCreatedAt(LocalDateTime.now());
                        return newInventory;
                    });
            
            BigDecimal incomingQuantity = detail.getQuantity();
            BigDecimal incomingPrice = detail.getUnitPrice();

            
            BigDecimal oldStock = inventory.getCurrentStock();
            BigDecimal oldAvgCost = inventory.getAverageCost();
            BigDecimal oldTotalCost = oldStock.multiply(oldAvgCost);
            BigDecimal incomingTotalCost = incomingQuantity.multiply(incomingPrice);
            
            BigDecimal newStock = oldStock.add(incomingQuantity);
            BigDecimal newTotalCost = oldTotalCost.add(incomingTotalCost);
            
            
            BigDecimal newAvgCost = newStock.compareTo(BigDecimal.ZERO) == 0 ? 
                                    BigDecimal.ZERO : 
                                    newTotalCost.divide(newStock, 4, RoundingMode.HALF_UP);
            
            inventory.setCurrentStock(newStock);
            inventory.setAverageCost(newAvgCost);
            inventory.setUpdatedBy(operatorUserId);
            inventory.setUpdatedAt(LocalDateTime.now());

         
            inventoryRepository.save(inventory);

            
            InventoryMovement movement = new InventoryMovement();
            movement.setProduct(inventory.getProduct());
            movement.setWarehouse(inventory.getWarehouse());
            movement.setMovementType("IN_PURCHASE");
            movement.setQuantityChange(incomingQuantity);
            movement.setCurrentStockAfterMovement(newStock);
            movement.setDocumentType("PurchaseOrder");
            movement.setDocumentId(order.getPurchaseOrderId());
            movement.setDocumentItemId(detail.getItemId());
            movement.setRecordedBy(user);

            movement.setMovementDate(LocalDateTime.now());
            
            inventoryMovementRepository.save(movement);
        }
        
        // 6. 更新進貨單狀態為「已到貨」
        order.setStatus(PurchaseOrderStatus.RECEIVED);
        order.setUpdatedBy(operatorUserId);
        order.setUpdatedAt(LocalDateTime.now());
        purchaseOrderRepository.save(order);
    }
}
