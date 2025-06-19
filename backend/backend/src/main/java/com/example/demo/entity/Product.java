package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_code", nullable = false, unique = true, length = 50)
    private String productCode;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(name = "is_purchasable", nullable = false)
    private boolean isPurchasable = true;

    @Column(name = "is_salable", nullable = false)
    private boolean isSalable = true;

    @Column(name = "base_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "tax_type", nullable = false, length = 20)
    private String taxType;

    @Column(name = "cost_method", nullable = false, length = 50)
    private String costMethod = "AVERAGE";


    @Column(name = "safety_stock_quantity", nullable = false)
    private Integer safetyStockQuantity = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 尚未加入所有欄位和完整的 @ManyToOne 關聯，先專注在核心功能上

}
