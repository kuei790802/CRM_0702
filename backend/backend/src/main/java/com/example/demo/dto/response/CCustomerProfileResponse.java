package com.example.demo.dto.response;

import com.example.demo.entity.Coupon;
import com.example.demo.entity.VIPLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class CCustomerProfileResponse {
    private String account;
    private String customerName;
    private String email;
    private String address;
    private LocalDate birthday;
    private List<Coupon> coupons;
    private Long spending;
    private VIPLevel vipLevel;
}
