package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Inventory;
import com.example.demo.entity.Product;
import com.example.demo.entity.Warehouse;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{

    Optional<Inventory> findByProduct_ProductIdAndWarehouse_WarehouseId(Long productId, Long warehouseId);
    List<Inventory> findByProduct_ProductId(Long productId);
    Optional<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);
}

