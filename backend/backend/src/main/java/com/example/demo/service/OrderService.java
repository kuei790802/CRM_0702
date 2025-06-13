package com.example.demo.service;

import com.example.demo.dto.request.CreateOrderRequestDto;
import com.example.demo.dto.response.OrderDetailDto;
import com.example.demo.dto.response.OrderDto;
import com.example.demo.entity.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserAddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private UserAddressRepository userAddressRepository;
    private InventoryRepository inventoryRepository;

    /**
     * 從購物車建立訂單
     */
    public OrderDto createOrderFromCart(Long userId, CreateOrderRequestDto requestDto) {
        // 1. 找到使用者的購物車
        Cart cart = cartRepository.findByUser_Userid(userId)
                .orElseThrow(() -> new IllegalStateException("找不到使用者的購物車"));

        if (cart.getCartdetails().isEmpty()) {
            throw new IllegalStateException("購物車是空的，無法建立訂單");
        }

        // 2. 驗證收貨地址是否屬於該使用者
        UserAddress address = userAddressRepository.findById((long)requestDto.getAddressId())
                .filter(addr -> addr.getUser().getUserid().equals(userId))
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

        // 4. 建立 Order 主體
        Order newOrder = new Order();
        newOrder.setUser(cart.getUser());
        newOrder.setUserAddress(address);
        newOrder.setOrderdate(LocalDate.now());
        newOrder.setOrderStatus(OrderStatus.PROCESSING); // 初始狀態
        newOrder.setPaymentStatus(PaymentStatus.NONPAYMENT);         // 初始狀態
        newOrder.setPaymethod(requestDto.getPaymentMethod());

        // 5. 從 CartDetail 複製到 OrderDetail，並更新庫存預訂量
        for (CartDetail detail : cart.getCartdetails()) {
            // 建立訂單明細
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(detail.getProduct());
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setUnitprice(detail.getProduct().getUnitprice()); // 記錄下單時的價格
            newOrder.addOrderDetail(orderDetail); // 假設 Order 有輔助方法

            // 更新庫存：增加 unitsInReserved 數量
            List<Inventory> inventories = detail.getProduct().getInventories();
            // 簡單起見，我們更新第一個找到的庫存紀錄。實務上可能需要更複雜的分配邏輯。
            if (!inventories.isEmpty()) {
                Inventory inventoryToUpdate = inventories.get(0);
                inventoryToUpdate.setUnitsinreserved(inventoryToUpdate.getUnitsinreserved() + detail.getQuantity());
                inventoryRepository.save(inventoryToUpdate);
            }
        }

        // 6. 清空購物車
        cart.getCartdetails().clear();
        cartRepository.save(cart);

        // 7. 儲存訂單 (因為有 cascade，OrderDetail 會一併儲存)
        Order savedOrder = orderRepository.save(newOrder);

        return mapToOrderDto(savedOrder);
    }

    /**
     * 更新訂單狀態 (給後台管理或支付回調使用)
     */
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
     * 查詢特定使用者的歷史訂單
     */
    public List<OrderDto> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUser_UseridOrderByOrderdateDesc(userId).stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    /**
     * (給後台管理用) 根據狀態查詢訂單
     */
    public Page<OrderDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByOrderstatus(status, pageable)
                .map(this::mapToOrderDto);
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
                .totalPrice(totalPrice)
                .orderDetails(detailDtos)
                .build();
    }
}
