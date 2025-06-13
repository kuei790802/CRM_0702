package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 查詢某個使用者的所有歷史訂單，並按日期降序排列
    List<Order> findByUser_UseridOrderByOrderdateDesc(Integer userId);

    // (給後台管理用) 根據訂單狀態查詢，並支援分頁
    Page<Order> findByOrderstatus(OrderStatus status, Pageable pageable);
}
