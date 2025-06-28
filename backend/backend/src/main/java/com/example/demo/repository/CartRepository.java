package com.example.demo.repository;

import com.example.demo.entity.CCustomer;
import com.example.demo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCCustomer_CustomerId(Long customerId);

    Optional<Cart> findByCCustomer(CCustomer customer);
}
