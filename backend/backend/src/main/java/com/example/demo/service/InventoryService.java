package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

//        Users receivingUser = userRepository.findById(receivingUserId)
//                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + receivingUserId + " 的使用者"));


        if (purchaseOrder.getStatus() == PurchaseOrderStatus.RECEIVED
                || purchaseOrder.getStatus() == PurchaseOrderStatus.CANCELLED) {
            throw new IllegalStateException("採購單 " + purchaseOrder.getOrderNumber() + " 已完成或已取消，無法執行入庫。");
        }


        for (PurchaseOrderDetail detail : purchaseOrder.getDetails()) {
            this.adjustInventory(
                    detail.getProduct().getProductId(),
                    detail.getWarehouseId(),
                    detail.getQuantity(),
                    detail.getUnitPrice(),
                    MovementType.PURCHASE_IN,
                    "PURCHASE_ORDER",
                    purchaseOrder.getPurchaseOrderId(),
                    detail.getItemId(),
                    receivingUserId);

        }


        purchaseOrder.setStatus(PurchaseOrderStatus.RECEIVED);
        purchaseOrder.setUpdatedAt(LocalDateTime.now());
        purchaseOrder.setUpdatedBy(receivingUserId);
        purchaseOrderRepository.save(purchaseOrder);
    }


    public List<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProduct_ProductId(productId);
    }

    /**
     * 根據產品ID獲取當前庫存數量。
     * @param productId 產品ID
     * @return 如果找到庫存，返回庫存數量；如果沒有該產品的庫存紀錄，則返回 0。
     */
    public BigDecimal getProductStock(Long productId) {
        // 使用 inventoryRepository 根據 productId 查找庫存紀錄
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
        // 如果存在庫存紀錄，則返回其 stock 數量；否則返回 0
        return inventoryOpt.map(Inventory::getCurrentStock).orElse(BigDecimal.valueOf(0));
    }

    @Transactional
    public Inventory adjustInventory(Long productId, Long warehouseId,
                                     BigDecimal quantityChange, BigDecimal unitCost,
                                     MovementType movementType, String documentType,
                                     Long documentId, Long documentItemId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + productId + " 的產品"));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID為 " + warehouseId + " 的倉庫"));
        Users user = userRepository.getReferenceById(userId);

        Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setProduct(product);
                    newInventory.setWarehouse(warehouse);
                    newInventory.setCurrentStock(BigDecimal.ZERO);
                    newInventory.setAverageCost(BigDecimal.ZERO);
                    newInventory.setCreatedAt(LocalDateTime.now());
                    newInventory.setCreatedBy(userId);
                    newInventory.setUpdatedAt(LocalDateTime.now());
                    newInventory.setUpdatedBy(userId);

                    return newInventory;
                });

        BigDecimal currentQuantity = inventory.getCurrentStock();
        BigDecimal newQuantity = currentQuantity.add(quantityChange);

        if (newQuantity.compareTo(BigDecimal.ZERO)<0) {
            throw new IllegalArgumentException("庫存不足。產品 '" + product.getName() + "' 在倉庫 '" + warehouse.getName()
                    + "' 的目前庫存為 " + currentQuantity + ", 無法扣減 " + quantityChange.abs());
        }
        if(movementType == MovementType.PURCHASE_IN && quantityChange.compareTo(BigDecimal.ZERO)>0){
            BigDecimal currentTotalValue = currentQuantity.multiply(inventory.getAverageCost());
            BigDecimal newItemsValue = quantityChange.multiply(unitCost);

            if(newQuantity.compareTo(BigDecimal.ZERO)!= 0){
                BigDecimal newAverageCost = (currentTotalValue.add(newItemsValue))
                        .divide(newQuantity, 4, RoundingMode.HALF_UP);
                inventory.setAverageCost(newAverageCost);
            }
        }

        inventory.setCurrentStock(newQuantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventory.setUpdatedBy(userId);

        Inventory updatedInventory = inventoryRepository.save(inventory);


//        InventoryMovement movement = new InventoryMovement();
//        movement.setProduct(product);
//        movement.setWarehouse(warehouse);
//        movement.setMovementType(movementType.name());
//        movement.setQuantityChange(quantityChange);
//        movement.setCurrentStockAfterMovement(newQuantity);
//        movement.setUnitCostAtMovement(unitCost);
//        movement.setMovementDate(LocalDateTime.now());
//        movement.setDocumentType(documentType);
//        movement.setDocumentId(documentId);
//        movement.setDocumentItemId(documentItemId);
//        movement.setRecordedBy(userRepository.getReferenceById(1L)
//                );
        createInventoryMovement(
                product,
                warehouse,
                movementType.name(),
                quantityChange,
                unitCost,
                newQuantity,
                documentType,
                documentId,
                documentItemId,
                user
        );

//        inventoryMovementRepository.save(movement);

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
        Warehouse shipmentWarehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + warehouseId + " 的出貨倉庫"));


        SalesShipment shipment = new SalesShipment();
        shipment.setSalesOrder(order);
        shipment.setCustomer(order.getCustomer());
        shipment.setWarehouse(shipmentWarehouse);
        shipment.setShipmentDate(LocalDate.now());
        shipment.setShippingMethod(order.getShippingMethod());
        shipment.setShippingStatus("SHIPPED");

        String shipmentNumber = "SHIP-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        shipment.setShipmentNumber(shipmentNumber);


        for (SalesOrderDetail detail : order.getDetails()) {


            int updatedRows = inventoryRepository.deductStock(
                    detail.getProduct().getProductId(),
                    warehouseId,
                    detail.getQuantity()
            );


            if (updatedRows == 0) {
                throw new DataConflictException("庫存不足：產品 '" + detail.getProduct().getName() + "' 在倉庫 ID " + warehouseId + " 中數量不足，無法出貨。");
            }


            SalesShipmentDetail shipmentDetail = new SalesShipmentDetail();
            shipmentDetail.setProduct(detail.getProduct());
            shipmentDetail.setShippedQuantity(detail.getQuantity());
            shipmentDetail.setSalesOrderItemId(detail.getItemId());
            shipment.addDetail(shipmentDetail);

        }

        SalesShipment savedShipment = salesShipmentRepository.save(shipment);

        for (SalesShipmentDetail savedShipmentDetail : savedShipment.getDetails()) {
            Product product = savedShipmentDetail.getProduct();
            Warehouse warehouse = savedShipment.getWarehouse();
            Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                    .orElseThrow(() -> new IllegalStateException("庫存紀錄消失，資料異常！產品ID: " + product.getProductId()));
//            Product product = detail.getProduct();
//            Warehouse warehouse = shipment.getWarehouse();
//            Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
//                    .orElseThrow(() -> new IllegalStateException("庫存紀錄消失，資料異常！"));

//            for(SalesShipmentDetail salesShipmentDetail : savedSh)

            createInventoryMovement(
                    product,
                    shipmentWarehouse,
                    "OUT_SALE",
                    savedShipmentDetail.getShippedQuantity().negate(),
                    inventory.getAverageCost(), // Cost for sales is the current average cost
                    inventory.getCurrentStock(),
                    "SalesShipment",
                    savedShipment.getShipmentId(),
                    savedShipmentDetail.getItemId(),
                    operator
            );
//            InventoryMovement movement = new InventoryMovement();
//            movement.setProduct(product);
//            movement.setWarehouse(shipment.getWarehouse());
//            movement.setMovementType("OUT_SALE");
//            movement.setQuantityChange(savedShipmentDetail.getShippedQuantity().negate());
//            movement.setCurrentStockAfterMovement(inventory.getCurrentStock());
//            movement.setUnitCostAtMovement(inventory.getAverageCost());
//            movement.setDocumentType("SalesShipment");
//            movement.setDocumentId(savedShipment.getShipmentId());
//            movement.setDocumentItemId(savedShipmentDetail.getItemId());
//            movement.setRecordedBy(operator);
//            movement.setMovementDate(LocalDateTime.now());
//            inventoryMovementRepository.save(movement);
        }


        order.setOrderStatus(SalesOrderStatus.SHIPPED);
        salesOrderRepository.save(order);


        return savedShipment;
    }

    private void createInventoryMovement(Product product, Warehouse warehouse, String movementType,
                                         BigDecimal quantityChange, BigDecimal unitCost, BigDecimal stockAfterMovement,
                                         String documentType, Long documentId, Long documentItemId, Users recordedBy) {
        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setMovementType(movementType);
        movement.setQuantityChange(quantityChange);
        movement.setUnitCostAtMovement(unitCost);
        movement.setTotalCostChange(unitCost.multiply(quantityChange));
        movement.setCurrentStockAfterMovement(stockAfterMovement);
        movement.setDocumentType(documentType);
        movement.setDocumentId(documentId);
        movement.setDocumentItemId(documentItemId);
        movement.setRecordedBy(recordedBy);
        movement.setMovementDate(LocalDateTime.now());

        inventoryMovementRepository.save(movement);
    }
}

