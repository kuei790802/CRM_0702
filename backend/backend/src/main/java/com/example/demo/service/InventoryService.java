package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.demo.entity.*;
import com.example.demo.enums.SalesOrderStatus;
import com.example.demo.exception.DataConflictException;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.enums.MovementType;
import com.example.demo.enums.PurchaseOrderStatus;
import com.example.demo.exception.ResourceNotFoundException;

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

    private final SalesOrderRepository salesOrderRepository;
    private final SalesShipmentRepository salesShipmentRepository;


    
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

    @Transactional
    public SalesShipment shipSalesOrder(Long salesOrderId, Long warehouseId, Long operatorId) {


        SalesOrder order = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + salesOrderId + " 的銷售訂單"));

        if (order.getOrderStatus() != SalesOrderStatus.CONFIRMED) {
            throw new DataConflictException("此銷售訂單狀態為 " + order.getOrderStatus() + "，不可執行出貨。");
        }

        Users operator = userRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + operatorId + " 的操作員"));


        SalesShipment shipment = new SalesShipment();
        shipment.setSalesOrder(order);
        shipment.setCustomer(order.getCustomer());
        shipment.setWarehouse(warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + warehouseId + " 的出貨倉庫")));
        shipment.setShipmentDate(LocalDate.now());
        shipment.setShippingMethod(order.getShippingMethod());
        shipment.setShippingStatus("SHIPPED");
        // ... set created_by 等欄位

        // 產生唯一的出貨單號
        String shipmentNumber = "SHIP-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        shipment.setShipmentNumber(shipmentNumber);

        // 3. 遍歷訂單明細，執行扣庫存、建立出貨明細與庫存異動紀錄
        for (SalesOrderDetail detail : order.getDetails()) {

            // 3.1 使用我們在 Repository 定義的原子性方法扣減庫存
            int updatedRows = inventoryRepository.deductStock(
                    detail.getProduct().getProductId(),
                    warehouseId, // 從指定的倉庫扣庫存
                    detail.getQuantity()
            );

            // 如果更新的筆數為 0，代表庫存不足，拋出例外並觸發交易復原
            if (updatedRows == 0) {
                throw new DataConflictException("庫存不足：產品 '" + detail.getProduct().getName() + "' 在倉庫 ID " + warehouseId + " 中數量不足，無法出貨。");
            }

            // 3.2 建立出貨單明細 (SalesShipmentDetail)
            SalesShipmentDetail shipmentDetail = new SalesShipmentDetail();
            shipmentDetail.setProduct(detail.getProduct());
            shipmentDetail.setShippedQuantity(detail.getQuantity());
            shipmentDetail.setSalesOrderItemId(detail.getItemId());
            shipment.addDetail(shipmentDetail);


            Product product = detail.getProduct();
            Warehouse warehouse = shipment.getWarehouse();
            Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                    .orElseThrow(() -> new IllegalStateException("庫存紀錄消失，資料異常！"));

            InventoryMovement movement = new InventoryMovement();
            movement.setProduct(detail.getProduct());
            movement.setWarehouse(shipment.getWarehouse());
            movement.setMovementType("OUT_SALE");
            movement.setQuantityChange(detail.getQuantity().negate());
            movement.setCurrentStockAfterMovement(inventory.getCurrentStock());
            movement.setDocumentType("SalesShipment");
            movement.setDocumentId(shipment.getShipmentId());
            movement.setDocumentItemId(shipmentDetail.getItemId());
            movement.setRecordedBy(operator);
            movement.setMovementDate(LocalDateTime.now());
            inventoryMovementRepository.save(movement);
        }


        order.setOrderStatus(SalesOrderStatus.SHIPPED);
        salesOrderRepository.save(order);


        return salesShipmentRepository.save(shipment);
    }
}

