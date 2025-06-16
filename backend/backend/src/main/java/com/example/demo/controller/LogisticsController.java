package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.EcpayService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {

    @Autowired
    private EcpayService ecpayService;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 為指定訂單建立綠界物流訂單
     * @param orderId 系統內部的訂單ID
     * @return 綠界的回應
     */
    @PostMapping("/create/{orderId}")
    public ResponseEntity<String> createEcpayLogisticsOrder(@PathVariable Long orderId) {
        // 1. 從資料庫找到我們的訂單
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單 ID: " + orderId));

        // 2. 呼叫 EcpayService 來處理串接
        String ecpayResponse = ecpayService.createLogisticsOrder(order);

        // 3. 將綠界的回應直接回傳給前端
        // 這裡可以再優化，將綠界回應的 JSON 解析成物件再回傳，但目前先以最簡單的方式實作
        return ResponseEntity.ok(ecpayResponse);
    }
}