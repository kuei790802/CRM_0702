package com.example.demo.controller;

import com.example.demo.entity.Inventory;
import com.example.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/test1/{id}")
    public ResponseEntity<Inventory> queryInventory(@PathVariable Long id) {
        Inventory inventory = inventoryService.test1(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/{productname}")
    public ResponseEntity<List<Inventory>> findByProduct_ProductnameContaining(@PathVariable String productname) {
        List<Inventory> inventories = inventoryService.findByProduct_ProductnameContaining(productname);
        return ResponseEntity.ok(inventories);
    }














    // 此功能可移去庫存異動
    @GetMapping("/{startTime}/{endTime}")
    public ResponseEntity<List<Inventory>> findByUpdateatBetween(
            @PathVariable LocalDateTime startTime, @PathVariable LocalDateTime endTime) {
        List<Inventory> inventories = inventoryService.findByUpdateatBetween(startTime, endTime);
        return ResponseEntity.ok(inventories);
    }



}
