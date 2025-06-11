package com.example.demo.service;

import com.example.demo.entity.Inventory;
import com.example.demo.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public Inventory test1(Long inventoryId) {
        return inventoryRepository.findById(inventoryId).orElse(null);
    }

    public List<Inventory> findByProduct_ProductnameContaining(String productname) {
        return inventoryRepository.findByProduct_ProductnameContaining(productname);
    }









    // 此功能可移去庫存異動
    public List<Inventory> findByUpdateatBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryRepository.findByUpdateatBetween(startTime, endTime);
    }


}
