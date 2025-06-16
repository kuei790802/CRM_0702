package com.example.demo.repository;

import com.example.demo.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    // 根據購物車ID和商品ID查找購物車項目
    Optional<CartDetail> findByCart_CartidAndProduct_Productid(Long cartid, Long productid);
}
