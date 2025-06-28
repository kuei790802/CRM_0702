package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "customer")
@DiscriminatorValue("B2C")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CCustomer extends CustomerBase {

    @Column(nullable = false, unique = true)
    private String account;
    @Column(nullable = false)
    private String password;
    private LocalDate birthday;
    private LocalDateTime lastLogin;
    private LocalDateTime accessStartTime;
    private LocalDateTime accessEndTime;


//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;

//    @CreatedDate // Use annotation
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt; //TODO(joshkuei): Add for test.
//
//    @LastModifiedDate // Use annotation
//    @Column(name = "updated_at", nullable = false)
//    private LocalDateTime updatedAt; //TODO(joshkuei): Add for test.test

//    @PrePersist
//    public void onCreate() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//        if (this.isDeleted == null) this.isDeleted = false;
//        if (this.isActive == null) this.isActive = true;
//
//    }
//
//    @PreUpdate
//    public void onUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }


    @OneToOne(mappedBy = "CCustomer")
    private Cart cart;

    @OneToMany(mappedBy = "CCustomer")
//    @Builder.Default
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

//    @Builder.Default
    private List<Coupon> coupons = new ArrayList<>();


}
