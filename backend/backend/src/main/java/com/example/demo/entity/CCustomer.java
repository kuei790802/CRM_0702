package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set; // 改用 Set

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false, unique = true)
    private String account;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;

    private String address;
    private LocalDate birthday;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    private LocalDateTime accessStartTime;
    private LocalDateTime accessEndTime;
    private Long spending;


    private boolean isDeleted;
    private boolean isActive;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @OneToOne(mappedBy = "CCustomer")
    private Cart cart;

    @OneToMany(mappedBy = "CCustomer")
    private List<CCustomerAddress> CCustomerAddress = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vip_level")
    private VIPLevel vipLevel;

    // 【重要】移除舊的多對多關聯
    // @ManyToMany(...)
    // private List<Coupon> coupons;

    // 【重要】新增與 CustomerCoupon 的一對多關聯
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CustomerCoupon> customerCoupons;
}
