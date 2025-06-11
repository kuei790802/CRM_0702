package com.example.demo.repository;

import com.example.demo.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByProduct_ProductnameContaining(String productname);










    // 此功能可移去庫存異動
    List<Inventory> findByUpdateatBetween(LocalDateTime startTime, LocalDateTime endTime);
}
