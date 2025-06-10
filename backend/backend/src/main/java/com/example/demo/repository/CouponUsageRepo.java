package com.example.demo.repository;

import com.example.demo.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUsageRepo extends JpaRepository<CouponUsage, Long> {
}
