package com.example.demo.entity;

import com.example.demo.enums.CouponType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private String name;
    private String description;

    private double discountAmount;
    private double discountRate;

    private Integer usageLimit;
    private Integer usedCount = 0;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    private boolean isActive;
    private boolean isSingleUse;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @ManyToOne
    @JoinColumn(name = "target_vip_level")
    private VIPLevel targetVipLevel;

    @ManyToMany(mappedBy = "coupons")
    private List<CCustomer> CCustomers = new ArrayList<>();
}
