package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.example.demo.dto.InventoryAdjustmentCreateDTO;
import com.example.demo.dto.InventoryAdjustmentDetailCreateDTO;
import com.example.demo.dto.InventoryViewDTO;
import com.example.demo.entity.*;
import com.example.demo.enums.SalesOrderStatus;
import com.example.demo.exception.DataConflictException;
import com.example.demo.repository.*;
import com.example.demo.specification.InventorySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final InventoryAdjustmentRepository adjustmentRepository;

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

//        User receivingUser = userRepository.findById(receivingUserId)
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


    public BigDecimal getProductStock(Long productId) {

        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);

        return inventoryOpt.map(Inventory::getCurrentStock).orElse(BigDecimal.valueOf(0));
    }

    public Page<InventoryViewDTO> searchInventories(Long productId, Long warehouseId, Pageable pageable) {

        Specification<Inventory> spec = InventorySpecification.findByCriteria(productId, warehouseId);


        Page<Inventory> inventoryPage = inventoryRepository.findAll(spec, pageable);


        return inventoryPage.map(InventoryViewDTO::fromEntity);
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
        User user = userRepository.getReferenceById(userId);

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
    public InventoryAdjustment createInventoryAdjustment(InventoryAdjustmentCreateDTO dto, Long operatorId) {

        if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
            throw new DataConflictException("庫存調整單至少需要一筆明細。");
        }

        User operator = userRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + operatorId + " 的操作員"));


        Long warehouseId = dto.getDetails().get(0).getWarehouseId();
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + warehouseId + " 的倉庫"));


        InventoryAdjustment adjustment = new InventoryAdjustment();
        adjustment.setAdjustmentNumber("ADJ-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))); //TODO(joshkuei): check if duplicate.
        adjustment.setAdjustmentDate(dto.getAdjustmentDate());
        adjustment.setAdjustmentType(dto.getAdjustmentType());
        adjustment.setRemarks(dto.getRemarks());
        adjustment.setStatus("EXECUTED");
        adjustment.setWarehouse(warehouse);

        LocalDateTime now = LocalDateTime.now();
        adjustment.setCreatedBy(operatorId);
        adjustment.setUpdatedBy(operatorId);
        adjustment.setCreatedAt(now);
        adjustment.setUpdatedAt(now);


        for (InventoryAdjustmentDetailCreateDTO detailDTO : dto.getDetails()) {

            if (!detailDTO.getWarehouseId().equals(warehouseId)) {
                throw new DataConflictException("單張調整單的所有明細必須屬於同一個倉庫。");
            }

            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + detailDTO.getProductId() + " 的產品"));


            Inventory inventory = inventoryRepository.findByProductAndWarehouse(product,warehouse)
                    .orElseGet(() -> {
                        Inventory newInventory = new Inventory();
                        newInventory.setProduct(product);
                        newInventory.setWarehouse(warehouse);
                        newInventory.setCurrentStock(BigDecimal.ZERO);
                        newInventory.setAverageCost(BigDecimal.ZERO);
                        newInventory.setCreatedBy(operatorId);
                        newInventory.setCreatedAt(now);
                        return newInventory;
                    });

            BigDecimal newStock = inventory.getCurrentStock().add(detailDTO.getAdjustedQuantity());
            if (newStock.compareTo(BigDecimal.ZERO) < 0) {
                throw new DataConflictException("庫存不足，產品 '" + product.getName() + "' 調整後庫存不可為負數。");
            }
            inventory.setCurrentStock(newStock);
            inventory.setUpdatedBy(operatorId);
            inventory.setUpdatedAt(now);
            inventoryRepository.save(inventory);


            InventoryAdjustmentDetail detail = new InventoryAdjustmentDetail();
            detail.setProduct(product);
            detail.setAdjustedQuantity(detailDTO.getAdjustedQuantity());
            detail.setRemarks(detailDTO.getRemarks());
            detail.setCreatedBy(operatorId);
            detail.setUpdatedBy(operatorId);
            detail.setCreatedAt(now);
            detail.setUpdatedAt(now);
            adjustment.addDetail(detail);


            InventoryMovement movement = new InventoryMovement();
            movement.setProduct(product);
            movement.setWarehouse(warehouse);
            movement.setMovementType("ADJUSTMENT_" + dto.getAdjustmentType().name()); // 例如 ADJUSTMENT_SCRAP
            movement.setQuantityChange(detailDTO.getAdjustedQuantity());
            movement.setCurrentStockAfterMovement(newStock);
            movement.setDocumentType("InventoryAdjustment");
            movement.setRecordedBy(operator);
            movement.setMovementDate(now);
            inventoryMovementRepository.save(movement);
        }
        String adjustmentNumber = "ADJ-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        adjustment.setAdjustmentNumber(adjustmentNumber);

        InventoryAdjustment savedAdjustment = adjustmentRepository.save(adjustment);


        savedAdjustment.getDetails().forEach(detail -> {
            inventoryMovementRepository.findByDocumentItemId(detail.getItemId()).ifPresent(movement -> {
                movement.setDocumentId(savedAdjustment.getAdjustmentId());
                inventoryMovementRepository.save(movement);
            });
        });

        return savedAdjustment;
    }

    @Transactional
    public SalesShipment shipSalesOrder(Long salesOrderId, Long warehouseId, Long operatorId) {


        SalesOrder order = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + salesOrderId + " 的銷售訂單"));

        if (order.getOrderStatus() != SalesOrderStatus.CONFIRMED) {
            throw new DataConflictException("此銷售訂單狀態為 " + order.getOrderStatus() + "，不可執行出貨。");
        }

        User operator = userRepository.findById(operatorId)
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
                                         String documentType, Long documentId, Long documentItemId, User recordedBy) {
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

