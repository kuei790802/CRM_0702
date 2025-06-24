package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_address")
@Getter
@Setter
public class CCustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressid;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CCustomer CCustomer;

    private String name;
    private String phone;
    private String street;
    private String city;
    private String district;
    private String zipcode;
    private Boolean isdefault;
    private LocalDateTime createat;
    private LocalDateTime updateat;

    /**
     * 這是一個輔助方法，它不會對應到資料庫的任何欄位。
     * @Transient 註解會告訴 JPA 忽略這個方法，不要試圖將它映射到欄位。
     * @return 組合好的完整地址字串
     */
    @Transient
    public String getFullAddress() {
        return (this.zipcode != null ? this.zipcode : "") +
                (this.city != null ? this.city : "") +
                (this.district != null ? this.district : "") +
                (this.street != null ? this.street : "");
    }
}
