// CRM/backend/backend/src/main/java/com/example/demo/entity/Order.java
package com.example.demo.entity;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderid;

    @ManyToOne
    @JoinColumn(name = "platformid")
    private Platform platform;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CCustomer CCustomer;

    private LocalDate orderdate;

    private String paymethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderstatus")
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "paystatus")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "addressid")
    private CCustomerAddress CCustomerAddress;

    private LocalDateTime createat;
    private LocalDateTime updateat;

    // 新增總金額欄位
    @Column(name = "total_amount") // 請在資料庫中新增此欄位
    private Double totalAmount; // 假設訂單總金額是 Double

    //--------
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    //-------------雙向關聯輔助方法
    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    public void removeOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.remove(orderDetail);
        orderDetail.setOrder(null);
    }
}