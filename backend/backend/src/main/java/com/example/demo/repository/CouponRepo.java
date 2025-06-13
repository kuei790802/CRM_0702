package com.example.demo.repository;

import com.example.demo.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepo extends JpaRepository<Coupon, Long> {
}
