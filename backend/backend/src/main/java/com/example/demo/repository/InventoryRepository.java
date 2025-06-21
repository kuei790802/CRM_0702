package com.example.demo.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Inventory;
import com.example.demo.entity.Product;
import com.example.demo.entity.Warehouse;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{

    Optional<Inventory> findByProduct_ProductIdAndWarehouse_WarehouseId(Long productId, Long warehouseId);
    List<Inventory> findByProduct_ProductId(Long productId);
    Optional<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);


    //prevent overbooking
    @Modifying
    @Query("UPDATE Inventory i " +
            "SET i.currentStock = i.currentStock - :quantity " +
            "WHERE i.product.productId = :productId AND i.warehouse.warehouseId = :warehouseId AND i.currentStock >= :quantity")
    int deductStock(
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId,
            @Param("quantity") BigDecimal quantity
    );
}

