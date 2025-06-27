package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orderdetails")
@IdClass(OrderDetailPK.class)
@Getter
@Setter
public class OrderDetail {
//    private Long orderid;
//    private Long productid; // 主鍵的一部分同時也是外鍵，直接將 @Id 註解放在代表關聯的 order 和 product 屬性上。
    private Integer quantity;
    private Double unitprice;
    private LocalDateTime createat;
    private LocalDateTime updateat;

    //------
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    private Order order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product product;
}
