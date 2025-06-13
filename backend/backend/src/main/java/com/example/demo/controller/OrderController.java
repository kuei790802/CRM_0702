package com.example.demo.controller;

import com.example.demo.dto.request.CreateOrderRequestDto;
import com.example.demo.dto.request.UpdateStatusRequestDto;
import com.example.demo.dto.response.OrderDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;

    /**
     * 結帳 - 從購物車建立新訂單
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal UserPrincipal currentUser,
                                                @RequestBody CreateOrderRequestDto requestDto) {
        OrderDto newOrder = orderService.createOrderFromCart(currentUser.getId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    /**
     * 查詢自己的歷史訂單
     */
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDto>> getMyOrders(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<OrderDto> orders = orderService.getOrdersByUserId(currentUser.getId());
        return ResponseEntity.ok(orders);
    }

    // --- 以下為後台管理用 API (假設有 ADMIN 權限) ---

    /**
     * (後台) 根據狀態查詢訂單
     */
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrdersByStatus(
            @RequestParam OrderStatus status,
            @PageableDefault(size = 10, sort = "orderdate") Pageable pageable) {
        Page<OrderDto> orderPage = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orderPage);
    }

    /**
     * (後台) 更新特定訂單的狀態
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateStatusRequestDto requestDto) {
        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, requestDto.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }
}
