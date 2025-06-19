package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sales_orders")
@Getter
@Setter
public class SalesOrder {
    @Id
    @Column(name = "sales_order_id")
    private Long SalesOrderId;
}
