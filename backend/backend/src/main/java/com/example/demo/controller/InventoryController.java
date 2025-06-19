package com.example.demo.controller;


import com.example.demo.dto.InventoryAdjustmentDTO;
import com.example.demo.entity.Inventory;
import com.example.demo.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @PostMapping("/purchase-orders/{orderId}/receive")
    public ResponseEntity<Void> receivePurchaseOrder(@PathVariable Long orderId) {

        Long currentUserId = 1L;

        inventoryService.receivePurchaseOrder(orderId, currentUserId);


        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Inventory>> getInventoryByProductId(@PathVariable Long productId) {
        List<Inventory> inventoryList = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventoryList);
    }

    @PostMapping("/adjust")
    public ResponseEntity<Inventory> adjustInventory(@RequestBody InventoryAdjustmentDTO adjustmentDTO) {
        Inventory updatedInventory = inventoryService.adjustInventory(
                adjustmentDTO.getProductId(),
                adjustmentDTO.getWarehouseId(),
                adjustmentDTO.getQuantity(),
                adjustmentDTO.getMovementType(),
                adjustmentDTO.getDocumentType(),
                adjustmentDTO.getDocumentId(),
                adjustmentDTO.getDocumentItemId()
        );
        return ResponseEntity.ok(updatedInventory);
    }
}
