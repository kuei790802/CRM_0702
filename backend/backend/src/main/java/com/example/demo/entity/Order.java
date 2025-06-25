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
    @JoinColumn(name = "customerId")
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // 【重要】新增與 CustomerCoupon 的反向關聯
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private CustomerCoupon usedCoupon; // 反向關聯，用於查詢此訂單用了哪張券

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
