package com.example.demo.repository;

import com.example.demo.entity.CCustomer;
import com.example.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    // 可依顧客ID取得留言
    List<Message> findByCustomerId(Long customerId);

    List<Message> findByCCustomer(CCustomer customer);

}
