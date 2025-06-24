package com.example.demo.service;

import com.example.demo.config.EcpayProperties;
import com.example.demo.dto.request.CreateOrderRequestDto;
import com.example.demo.dto.response.OrderDetailDto;
import com.example.demo.dto.response.OrderDto;
import com.example.demo.entity.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.ShippingMethod; // 【導入】
import com.example.demo.repository.*;
import com.example.demo.util.EcpayCheckMacValueUtil;
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
import java.util.Map;
import java.util.UUID;
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
    @Autowired
    private CCustomerService cCustomerService;
    @Autowired
    private EcpayProperties ecpayProperties;
    @Autowired
    private EcpayService ecpayService;

    @Transactional
    public OrderDto createOrderFromCart(Long customerId, CreateOrderRequestDto requestDto) {
        // 步驟 1：找到使用者的購物車並驗證是否為空
        Cart cart = cartRepository.findByCCustomer_CustomerId(customerId)
                .orElseThrow(() -> new IllegalStateException("找不到使用者的購物車"));

        if (cart.getCartdetails() == null || cart.getCartdetails().isEmpty()) {
            throw new IllegalStateException("購物車是空的，無法建立訂單");
        }

        // ====================== 【您提到的庫存檢查邏輯在這裡】 ======================
        // 步驟 2：在建立訂單前，遍歷所有商品，檢查庫存是否足夠
        for (CartDetail detail : cart.getCartdetails()) {
            Product product = detail.getProduct();
            int requiredQuantity = detail.getQuantity();
            // 計算公式：可用庫存 = 實際庫存 - 已被預訂的庫存
            int availableStock = product.getInventories().stream()
                    .mapToInt(inv -> inv.getUnitsinstock() - inv.getUnitsinreserved())
                    .sum();
            if (availableStock < requiredQuantity) {
                throw new IllegalStateException("商品 [" + product.getProductname() + "] 庫存不足，無法下單");
            }
        }
        // =========================================================================

        ShippingMethod shippingMethod = ShippingMethod.valueOf(requestDto.getShippingMethod().toUpperCase());
        PaymentMethod paymentMethod = PaymentMethod.valueOf(requestDto.getPaymentMethod().toUpperCase());

        // 步驟 3：建立 Order 主體
        Order newOrder = new Order();

        // 條件式處理地址：只有宅配需要地址
        if (shippingMethod == ShippingMethod.HOME_DELIVERY) {
            if (requestDto.getAddressId() == null) {
                throw new IllegalArgumentException("選擇宅配時，必須提供地址 ID");
            }
            CCustomerAddress address = CCustomerAddressRepository.findById(requestDto.getAddressId().longValue())
                    .filter(addr -> addr.getCCustomer().getCustomerId().equals(customerId))
                    .orElseThrow(() -> new EntityNotFoundException("無效的地址 ID"));
            newOrder.setCCustomerAddress(address);
        }

        // 產生唯一的商家交易單號
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomPart = UUID.randomUUID().toString().substring(0, Math.min(6, 20 - 1 - timestamp.length()));
        String merchantTradeNo = "T" + timestamp + randomPart;
        newOrder.setMerchantTradeNo(merchantTradeNo);

        // 設定其他訂單資訊
        newOrder.setPlatform(platformRepository.findById(1L).get());
        newOrder.setCCustomer(cart.getCCustomer());
        newOrder.setOrderdate(LocalDate.now());
        newOrder.setCreateat(LocalDateTime.now());
        newOrder.setUpdateat(LocalDateTime.now());
        newOrder.setOrderDetails(new ArrayList<>());
        newOrder.setShippingMethod(shippingMethod);
        newOrder.setPaymentMethod(paymentMethod);

        // 設定初始狀態
        if (paymentMethod == PaymentMethod.ONLINE_PAYMENT) {
            newOrder.setOrderStatus(OrderStatus.PENDING_PAYMENT);
            newOrder.setPaymentStatus(PaymentStatus.UNPAID);
        } else { // CASH_ON_DELIVERY
            newOrder.setOrderStatus(OrderStatus.PENDING_SHIPMENT);
            newOrder.setPaymentStatus(PaymentStatus.UNPAID);
        }

        // 步驟 4：處理訂單明細並「預訂」庫存
        double calculatedTotalAmount = 0.0;
        for (CartDetail detail : cart.getCartdetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(detail.getProduct());
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setUnitprice(detail.getProduct().getUnitprice());
            orderDetail.setCreateat(LocalDateTime.now());
            orderDetail.setUpdateat(LocalDateTime.now());
            newOrder.addOrderDetail(orderDetail);
            calculatedTotalAmount += detail.getProduct().getUnitprice() * detail.getQuantity();

            // 增加「預訂庫存」，而不是直接扣減「實際庫存」
            List<Inventory> inventories = detail.getProduct().getInventories();
            if (!inventories.isEmpty()) {
                Inventory inventoryToUpdate = inventories.get(0);
                inventoryToUpdate.setUnitsinreserved(inventoryToUpdate.getUnitsinreserved() + detail.getQuantity());
                inventoryRepository.save(inventoryToUpdate);
            }
        }
        newOrder.setTotalAmount(calculatedTotalAmount);

        // 步驟 5：儲存訂單
        Order savedOrder = orderRepository.saveAndFlush(newOrder);

        // 步驟 6：(可選) 如果是宅配訂單，可在此立即建立物流單
        if (shippingMethod == ShippingMethod.HOME_DELIVERY) {
            String result = ecpayService.createHomeDeliveryOrder(savedOrder);
            System.out.println("建立宅配物流訂單結果: " + result);
            if (result == null || !result.startsWith("1|")) {
                throw new RuntimeException("建立宅配物流訂單失敗: " + result);
            }
        }

        // 步驟 7：清空購物車
        cart.getCartdetails().clear();
        cartRepository.save(cart);

        entityManager.refresh(savedOrder);
        return mapToOrderDto(savedOrder);
    }

    /**
     * 【修改後】處理綠界金流付款後的回調通知
     */
    @Transactional
    public void processPaymentCallback(Map<String, String> callbackData) {
        String receivedMacValue = callbackData.get("CheckMacValue");
        if (receivedMacValue == null) {
            throw new IllegalArgumentException("缺少 CheckMacValue，請求無效");
        }
        Map<String, String> dataToVerify = new java.util.TreeMap<>(callbackData);
        dataToVerify.remove("CheckMacValue");
        String expectedMacValue = EcpayCheckMacValueUtil.generate(
                dataToVerify, ecpayProperties.getAio().getHashKey(), ecpayProperties.getAio().getHashIv());

        if (!receivedMacValue.equals(expectedMacValue)) {
            throw new SecurityException("金流回調 CheckMacValue 驗證失敗！");
        }

        String merchantTradeNo = callbackData.get("MerchantTradeNo");
        String rtnCode = callbackData.get("RtnCode");

        if ("1".equals(rtnCode)) {
            Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo)
                    .orElseThrow(() -> new EntityNotFoundException("找不到對應的訂單編號: " + merchantTradeNo));

            if (order.getPaymentStatus() == PaymentStatus.UNPAID) {
                order.setPaymentStatus(PaymentStatus.PAID);
                // 【智慧判斷】如果運送方式是宅配，付款後直接轉為待出貨
                if (order.getShippingMethod() == ShippingMethod.HOME_DELIVERY) {
                    order.setOrderStatus(OrderStatus.PENDING_SHIPMENT);
                }
                // 如果是超商取貨，則狀態維持 PENDING_PAYMENT，等待使用者選擇門市
                orderRepository.save(order);
            }
        } else {
            // 可在此加入交易失敗的處理邏輯
        }
    }

    /**
     * 【修改後】處理門市選擇結果，並觸發建立物流訂單
     */
    @Transactional
    public void processStoreSelection(Map<String, String> replyData) {
        // 建議也加入 CheckMacValue 驗證
        String merchantTradeNo = replyData.get("MerchantTradeNo");
        Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單: " + merchantTradeNo));

        // 觸發步驟 B：呼叫 API 建立物流訂單
        String result = ecpayService.createLogisticsOrder(order, replyData);
        System.out.println("建立物流訂單結果: " + result);

        if (result != null && result.startsWith("1|")) {
            // 【智慧判斷】
            if (order.getPaymentStatus() == PaymentStatus.PAID || order.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
                order.setOrderStatus(OrderStatus.PENDING_SHIPMENT);
            }
            orderRepository.save(order);
        } else {
            throw new RuntimeException("建立物流訂單失敗: " + result);
        }
    }

    /**
     * 【修改後】處理後續的物流狀態更新回調
     */
    @Transactional
    public void processLogisticsCallback(Map<String, String> callbackData) {
        // ... (省略 CheckMacValue 驗證) ...
        String merchantTradeNo = callbackData.get("MerchantTradeNo");
        String rtnCode = callbackData.get("RtnCode");
        Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo)
                .orElseThrow(() -> new EntityNotFoundException("找不到對應的訂單編號: " + merchantTradeNo));

        updateOrderStatusFromLogistics(order, rtnCode);
    }

    // 以下為既有的、不需修改的方法
    // ... updateOrderStatus, searchOrders, getOrdersByStatus, getAllOrders, getOrderById ...
    // ... mapToOrderDto, updateOrderStatusFromLogistics, triggerUpdateSpending ...

    /**
     * 查詢特定使用者的歷史訂單 (包含使用者查詢自己的歷史訂單)
     */
    public List<OrderDto> getOrdersBycustomerId(Long customerId) {
        return orderRepository.findByCCustomer_CustomerIdOrderByOrderdateDesc(customerId).stream()
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

        OrderStatus oldStatus = order.getOrderStatus();
        order.setOrderStatus(newStatus);

        // 這段邏輯我們在下個版本處理綠界回調時會更有用
        // if (newStatus == OrderStatus.PAID) {
        //     order.setPaymentStatus(PaymentStatus.PAID);
        // }

        Order updatedOrder = orderRepository.save(order);

        // 當訂單狀態從 "非完成" 變為 "已完成" 時，觸發更新客戶總消費
        if (newStatus == OrderStatus.COMPLETE && oldStatus != OrderStatus.COMPLETE) {
            if (order.getCCustomer() != null && order.getCCustomer().getCustomerId() != null) {
                Long customerId = order.getCCustomer().getCustomerId();
                cCustomerService.updateCustomerSpending(customerId);
            }
        }

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
    private void updateOrderStatusFromLogistics(Order order, String logisticsStatusCode) {
        // 詳細的 RtnCode 請參考綠界官方文件
        switch (logisticsStatusCode) {
            case "2063": // 貨品已到店
                order.setOrderStatus(OrderStatus.PENDING_PICKUP);
                break;
            case "2067": // 買家已取貨 (適用純取貨，即線上已付款)
                if (order.getPaymentMethod() == PaymentMethod.ONLINE_PAYMENT) {
                    order.setOrderStatus(OrderStatus.COMPLETE);
                    triggerUpdateSpending(order); // 更新客戶總消費
                }
                break;
            case "2073": // 買家取貨付款
                if (order.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
                    order.setOrderStatus(OrderStatus.COMPLETE);
                    order.setPaymentStatus(PaymentStatus.PAID); // <<-- 更新付款狀態
                    triggerUpdateSpending(order); // 更新客戶總消費
                }
                break;
            // 可以加入更多 case, 例如： "3024": 商品遺失，此時可能要轉為 CANCELLED 或其他狀態
        }
        orderRepository.save(order);
    }

    /**
     * 【新增】觸發更新消費的輔助方法
     */
    private void triggerUpdateSpending(Order order) {
        if (order.getCCustomer() != null && order.getCCustomer().getCustomerId() != null) {
            cCustomerService.updateCustomerSpending(order.getCCustomer().getCustomerId());
        }
    }
}