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
import com.example.demo.entity.Warehouse; // 導入 Lombok 註解
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

/**
 * InventoryService 提供了管理庫存的核心業務邏輯。
 * 包含了查詢庫存、調整庫存等功能。
 */
@Service
@RequiredArgsConstructor
public class InventoryService {

    // 注入所有需要的 Repository
    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final PurchaseOrderRepository purchaseOrderRepository; // 新增依賴
    private final UserRepository userRepository; // 新增依賴

    /**
     * 處理採購單的入庫流程。
     * 這是一個高層級的業務方法。
     *
     * @param orderId         要入庫的採購單ID
     * @param receivingUserId 執行入庫操作的使用者ID
     */
    @Transactional
    public void receivePurchaseOrder(Long orderId, Long receivingUserId) {
        // 步驟 1: 查找採購單和執行操作的使用者
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + orderId + " 的採購單"));

        Users receivingUser = userRepository.findById(receivingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + receivingUserId + " 的使用者"));

        // 步驟 2: 驗證採購單狀態，防止重複入庫
        if (purchaseOrder.getStatus() == PurchaseOrderStatus.RECEIVED
                || purchaseOrder.getStatus() == PurchaseOrderStatus.CANCELLED) {
            throw new IllegalStateException("採購單 " + purchaseOrder.getOrderNumber() + " 已完成或已取消，無法執行入庫。");
        }

        // 步驟 3: 遍歷採購單中的每一個品項，並呼叫底層的 adjustInventory 方法來更新庫存
        for (PurchaseOrderDetail detail : purchaseOrder.getDetails()) {
            this.adjustInventory(
                    detail.getProductId(),
                    detail.getWarehouseId(),
                    detail.getQuantity().intValue(),
                    MovementType.PURCHASE_IN,
                    "PURCHASE_ORDER", // documentType
                    purchaseOrder.getPurchaseOrderId(), // documentId
                    detail.getItemId());
        }

        // 步驟 4: 更新採購單的狀態
        purchaseOrder.setStatus(PurchaseOrderStatus.RECEIVED);
        purchaseOrder.setUpdatedAt(LocalDateTime.now());
        purchaseOrder.setUpdatedBy(receivingUser.getUserId());

        purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * 根據產品ID查詢所有相關的庫存紀錄。
     *
     * @param productId 產品ID
     * @return 該產品的庫存列表
     */
    public List<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    /**
     * 調整指定產品在指定倉庫的庫存數量，並記錄異動歷史。
     * 這是一個底層、可重用的工具方法。
     *
     * @param productId      要調整庫存的產品ID
     * @param warehouseId    要調整庫存的倉庫ID
     * @param quantity       異動數量（正數代表增加，負數代表減少）
     * @param movementType   異動類型
     * @param documentNumber 相關單據號碼
     * @return 更新後的庫存物件
     */
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
