package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.enums.PurchaseOrderStatus;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="purchase_order_id")
    private Long purchaseOrderId;

    @Column(name = "order_number", unique = true, nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "warehouse_id", nullable = true)
    private Long warehouseId;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PurchaseOrderStatus status;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;
    
    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_tax_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalTaxAmount;

    @Column(name = "total_net_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalNetAmount;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @OneToMany(
        mappedBy = "purchaseOrder",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference
    private List<PurchaseOrderDetail> details = new ArrayList<>();

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    

    public void addDetail(PurchaseOrderDetail detail) {
        details.add(detail);
        detail.setPurchaseOrder(this);
    }
}
