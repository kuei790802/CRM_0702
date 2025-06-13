package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orderdetails")
@Getter
@Setter
public class OrderDetail {
    @Id
    private Long orderid;
    @Id
    private Long productid;
    private Integer quantity;
    private Double unitprice;
    private LocalDateTime createat;
    private LocalDateTime updateat;

    //------
    @ManyToOne
    @JoinColumn(name = "orderid")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;
}
