//package com.example.demo.controller;
//
//import com.example.demo.dto.request.CreateOrderRequestDto;
//import com.example.demo.dto.request.UpdateStatusRequestDto;
//import com.example.demo.dto.response.OrderDto;
//import com.example.demo.enums.OrderStatus;
//import com.example.demo.service.OrderService;
//import org.springdoc.core.annotations.ParameterObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderController {
//    @Autowired
//    private OrderService orderService;
//
//    /**
//     * 結帳 - 從購物車建立新訂單
//     */
////    @PostMapping
////    public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal UserPrincipal currentUser,
////                                                @RequestBody CreateOrderRequestDto requestDto) {
////        OrderDto newOrder = orderService.createOrderFromCart(currentUser.getId(), requestDto);
////        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
////    }
//
//    @PostMapping("/{userId}")
//    public ResponseEntity<OrderDto> createOrder(@PathVariable Long userId,
//                                                @RequestBody CreateOrderRequestDto requestDto) {
//        OrderDto newOrder = orderService.createOrderFromCart(userId, requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
//    }
//
//    /**
//     * 查詢自己的歷史訂單
//     */
////    @GetMapping("/my-orders")
////    public ResponseEntity<List<OrderDto>> getMyOrders(@AuthenticationPrincipal UserPrincipal currentUser) {
////        List<OrderDto> orders = orderService.getOrdersByUserId(currentUser.getId());
////        return ResponseEntity.ok(orders);
////    }
//
//    @GetMapping("/my-orders/{userId}")
//    public ResponseEntity<List<OrderDto>> getMyOrders(@PathVariable Long userId) {
//        List<OrderDto> orders = orderService.getOrdersByUserId(userId);
//        return ResponseEntity.ok(orders);
//    }
//
//    // --- 以下為後台管理用 API (假設有 ADMIN 權限) ---
//
//    /**
//     * ★★★ 新增：(後台) 查詢所有歷史訂單，並支援分頁與排序 ★★★
//     */
//    @GetMapping("/all")
//    public ResponseEntity<Page<OrderDto>> getAllOrders(
//            @ParameterObject @PageableDefault(size = 10, sort = "orderid", direction = Sort.Direction.DESC) Pageable pageable
//    ) {
//        Page<OrderDto> orderPage = orderService.getAllOrders(pageable);
//        return ResponseEntity.ok(orderPage);
//    }
//
//    /**
//     * (後台) 根據狀態查詢訂單
//     */
//    @GetMapping
//    public ResponseEntity<Page<OrderDto>> getOrdersByStatus(
//            @RequestParam OrderStatus status,
//            @ParameterObject @PageableDefault(size = 10, sort = "orderdate") Pageable pageable) {
//        Page<OrderDto> orderPage = orderService.getOrdersByStatus(status, pageable);
//        return ResponseEntity.ok(orderPage);
//    }
//
//    /**
//     * (後台) 根據時間範圍和商品名稱搜尋訂單
//     * 注意：這個 API 會返回符合條件的所有訂單，不分狀態
//     */
//    @GetMapping("/search")
//    public ResponseEntity<List<OrderDto>> searchOrders(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endTime,
//            @RequestParam(required = false) String productName
//    ) {
//        List<OrderDto> result = orderService.searchOrders(startTime, endTime, productName);
//        return ResponseEntity.ok(result);
//    }
//
//    /**
//     * (後台) 更新特定訂單的狀態
//     */
//    @PatchMapping("/{orderId}/status")
//    public ResponseEntity<OrderDto> updateOrderStatus(
//            @PathVariable Long orderId,
//            @RequestBody UpdateStatusRequestDto requestDto) {
//        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, requestDto.getStatus());
//        return ResponseEntity.ok(updatedOrder);
//    }
//
//    /**
//     * 根據單一 ID 查詢特定訂單的詳細資料
//     * 這就是給掃描功能使用的 API
//     */
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
//        OrderDto orderDto = orderService.getOrderById(orderId);
//        return ResponseEntity.ok(orderDto);
//    }
//}
