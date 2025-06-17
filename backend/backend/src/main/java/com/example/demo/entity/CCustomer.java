package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "customer_coupon",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private List<Coupon> coupons = new ArrayList<>();
}
