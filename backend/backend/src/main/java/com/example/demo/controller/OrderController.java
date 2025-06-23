package com.example.demo.controller;

import com.example.demo.dto.request.CreateOrderRequestDto;
import com.example.demo.dto.request.UpdateStatusRequestDto;
import com.example.demo.dto.response.OrderDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.security.CheckJwt; // 引入 CheckJwt
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 結帳 - 從購物車建立新訂單
     */
//    @PostMapping
//    public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal UserPrincipal currentUser,
//                                                @RequestBody CreateOrderRequestDto requestDto) {
//        OrderDto newOrder = orderService.createOrderFromCart(currentUser.getId(), requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
//    }
    // 修改前：@PostMapping("/{userId}")
    // 修改後：
    @PostMapping("/create")
    @CheckJwt // ★★★【修改重點】★★★
    public ResponseEntity<OrderDto> createOrder(HttpServletRequest request,
                                                @RequestBody CreateOrderRequestDto requestDto) {
        Long customerId = (Long) request.getAttribute("customerId");
        OrderDto newOrder = orderService.createOrderFromCart(customerId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    /**
     * 查詢自己的歷史訂單
     */
//    @GetMapping("/my-orders")
//    public ResponseEntity<List<OrderDto>> getMyOrders(@AuthenticationPrincipal UserPrincipal currentUser) {
//        List<OrderDto> orders = orderService.getOrdersByUserId(currentUser.getId());
//        return ResponseEntity.ok(orders);
//    }
    // 修改前：@GetMapping("/my-orders/{userId}")
    // 修改後：
    @GetMapping("/my-orders")
    @CheckJwt // ★★★【修改重點】★★★
    public ResponseEntity<List<OrderDto>> getMyOrders(HttpServletRequest request) {
        Long customerId = (Long) request.getAttribute("customerId");
        List<OrderDto> orders = orderService.getOrdersBycustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    // --- 以下為後台管理用 API (假設有 ADMIN 權限，暫時不加 @CheckJwt 保護) ---

    @GetMapping("/all")
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @ParameterObject @PageableDefault(size = 10, sort = "orderid", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderDto> orderPage = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orderPage);
    }

    // ... 其他後台 API 維持不變 ...
    @GetMapping("/status")
    public ResponseEntity<Page<OrderDto>> getOrdersByStatus(
            @RequestParam OrderStatus status,
            @ParameterObject @PageableDefault(size = 10, sort = "orderdate") Pageable pageable) {
        Page<OrderDto> orderPage = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orderPage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderDto>> searchOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endTime,
            @RequestParam(required = false) String productName
    ) {
        List<OrderDto> result = orderService.searchOrders(startTime, endTime, productName);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateStatusRequestDto requestDto) {
        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, requestDto.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }
}