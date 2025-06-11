package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.catalina.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory_movements")
@Getter
@Setter
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private Long movementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "movement_type", nullable = false)
    private String movementType;

    @Column(name = "quantity_change", nullable = false)
    private BigDecimal quantityChange;

    @Column(name = "current_stock_after_movement", nullable = false)
    private BigDecimal currentStockAfterMovement;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "document_item_id")
    private Long documentItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by", nullable = false)
    private User recordedBy;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;
}
