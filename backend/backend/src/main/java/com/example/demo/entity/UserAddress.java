package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "useraddress")
@Getter
@Setter
public class UserAddress {
    private Long addressid;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    private String name;
    private String phone;
    private String street;
    private String city;
    private String district;
    private String zipcode;
    private Boolean isdefault;
    private LocalDateTime createat;
    private LocalDateTime updateat;
}
