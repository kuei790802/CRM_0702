package com.example.demo.service;

import com.example.demo.dto.request.CreateOrderRequestDto;
import com.example.demo.dto.response.OrderDetailDto;
import com.example.demo.dto.response.OrderDto;
import com.example.demo.entity.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CCustomerAddressRepository CCustomerAddressRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private EntityManager entityManager;

    /**
     * 從購物車建立訂單
     */
    @Transactional
    public OrderDto createOrderFromCart(Long userId, CreateOrderRequestDto requestDto) {
        // 1. 找到使用者的購物車
        Cart cart = cartRepository.findByCCustomer_CustomerId(userId)
                .orElseThrow(() -> new IllegalStateException("找不到使用者的購物車"));

        if (cart.getCartdetails() == null || cart.getCartdetails().isEmpty()) {
            throw new IllegalStateException("購物車是空的，無法建立訂單");
        }

        // 2. 驗證收貨地址是否屬於該使用者
        CCustomerAddress address = CCustomerAddressRepository.findById(requestDto.getAddressId().longValue())
                .filter(addr -> addr.getCCustomer().getCustomerId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("無效的地址 ID"));

        // 3. 檢查所有商品的庫存 (這是關鍵步驟！)
        for (CartDetail detail : cart.getCartdetails()) {
            Product product = detail.getProduct();
            int requiredQuantity = detail.getQuantity();
            // 可銷售庫存 = 實體庫存 - 已預訂庫存
            int availableStock = product.getInventories().stream()
                    .mapToInt(inv -> inv.getUnitsinstock() - inv.getUnitsinreserved())
                    .sum();
            if (availableStock < requiredQuantity) {
                throw new IllegalStateException("商品 [" + product.getProductname() + "] 庫存不足");
            }
        }

        // 假設所有訂單都來自 ID 為 1 的平台。
        // 請確保您的 platforms 資料表裡，確實有一筆 platformid = 1 的資料。
        Platform defaultPlatform = platformRepository.findById(1L) // 相當於(long)1
                .orElseThrow(() -> new EntityNotFoundException("找不到預設的平台 (ID: 1)"));

        // 4. 建立 Order 主體
        Order newOrder = new Order();
        newOrder.setPlatform(defaultPlatform);
        newOrder.setCCustomer(cart.getCCustomer());
        newOrder.setCCustomerAddress(address);
        newOrder.setOrderdate(LocalDate.now());
        newOrder.setOrderStatus(OrderStatus.PROCESSING);
        newOrder.setPaymentStatus(PaymentStatus.NONPAYMENT);
        newOrder.setPaymethod(requestDto.getPaymentMethod());
        newOrder.setCreateat(LocalDateTime.now()); // <<-- 修正：補上時間戳
        newOrder.setUpdateat(LocalDateTime.now()); // <<-- 修正：補上時間戳
        newOrder.setOrderDetails(new ArrayList<>()); // 初始化 List

        double calculatedTotalAmount = 0.0; // 初始化一個變數來累計總金額

        // 5. 從 CartDetail 複製到 OrderDetail，並更新庫存預訂量
        for (CartDetail detail : cart.getCartdetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(detail.getProduct());
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setUnitprice(detail.getProduct().getUnitprice());
            orderDetail.setCreateat(LocalDateTime.now());
            orderDetail.setUpdateat(LocalDateTime.now());
            newOrder.addOrderDetail(orderDetail);

            calculatedTotalAmount += detail.getProduct().getUnitprice() * detail.getQuantity(); // 累計金額

            // 更新庫存
            List<Inventory> inventories = detail.getProduct().getInventories();
            if (!inventories.isEmpty()) {
                Inventory inventoryToUpdate = inventories.get(0);
                inventoryToUpdate.setUnitsinreserved(inventoryToUpdate.getUnitsinreserved() + detail.getQuantity());
                inventoryRepository.save(inventoryToUpdate);
            }
        }
        newOrder.setTotalAmount(calculatedTotalAmount); // 設定訂單總金額

        // 6. 儲存訂單
        Order savedOrder = orderRepository.saveAndFlush(newOrder);

        // 7. 清空購物車
        cart.getCartdetails().clear();
        cartRepository.save(cart);

        // 8. 刷新狀態並回傳 DTO
        entityManager.refresh(savedOrder);

        return mapToOrderDto(savedOrder);
    }

    /**
     * 查詢特定使用者的歷史訂單 (包含使用者查詢自己的歷史訂單)
     */
    public List<OrderDto> getOrdersByUserId(Long userId) {
        return orderRepository.findByCCustomer_CustomerIdOrderByOrderdateDesc(userId).stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    /**
     * ★★★ 新增：查詢所有歷史訂單 (分頁) ★★★
     */
    @Transactional(readOnly = true)
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        // 呼叫 JpaRepository 內建的 findAll 方法
        Page<Order> orderPage = orderRepository.findAll(pageable);

        // 使用 Page 物件內建的 map 功能，將 Page<Order> 轉換為 Page<OrderDto>
        return orderPage.map(this::mapToOrderDto);
    }

    /**
     * 更新訂單狀態 (給後台管理或支付回調使用)
     */
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單 ID: " + orderId));

        // 在這裡可以加入更複雜的狀態機邏輯，例如：只有 PAID 狀態才能轉為 SHIPPED
        order.setOrderStatus(newStatus);

        // 如果狀態是已付款，同步更新付款狀態
        if (newStatus == OrderStatus.PAID) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }

        Order updatedOrder = orderRepository.save(order);
        return mapToOrderDto(updatedOrder);
    }

    /**
     * (給後台管理用) 根據狀態查詢訂單
     */
    public Page<OrderDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByOrderStatus(status, pageable)
                .map(this::mapToOrderDto);
    }

    /**
     * ★★★ 新增的複合查詢訂單方法 ★★★
     */
    @Transactional(readOnly = true)
    public List<OrderDto> searchOrders(LocalDate startTime, LocalDate endTime, String productName) {
        // 直接呼叫我們在 Repository 中定義好的自訂查詢
        List<Order> orders = orderRepository.searchOrders(startTime, endTime, productName);

        // 將查詢到的 Order 實體列表，轉換成前端需要的 OrderDto 列表
        return orders.stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    /**
     * 根據 ID 查詢單一訂單 (出貨條碼檢查，給掃描訂單使用)
     */
    @Transactional(readOnly = true) // 查詢操作建議加上 readOnly = true，可以優化效能
    public OrderDto getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::mapToOrderDto) // 如果找到，就轉換成 DTO
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單 ID: " + orderId)); // 如果找不到，就拋出例外
    }


    // --- Private Helper Method for DTO Mapping ---
    private OrderDto mapToOrderDto(Order order) {
        List<OrderDetailDto> detailDtos = order.getOrderDetails().stream()
                .map(detail -> OrderDetailDto.builder()
                        .productName(detail.getProduct().getProductname())
                        .quantity(detail.getQuantity())
                        .unitPrice(detail.getUnitprice())
                        .build())
                .collect(Collectors.toList());

        double totalPrice = detailDtos.stream()
                .mapToDouble(d -> d.getUnitPrice() * d.getQuantity())
                .sum();

        return OrderDto.builder()
                .orderId(order.getOrderid())
                .orderDate(order.getOrderdate())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .totalPrice(order.getTotalAmount())
                .orderDetails(detailDtos)
                .build();
    }
}
