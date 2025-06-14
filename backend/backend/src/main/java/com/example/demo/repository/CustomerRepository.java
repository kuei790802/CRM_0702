package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 根據客戶名稱搜尋客戶
    Optional<Customer> findByCustomerName(String customerName);
}
