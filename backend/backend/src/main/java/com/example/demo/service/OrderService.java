package com.example.demo.service;

import com.example.demo.config.EcpayProperties;
import com.example.demo.dto.request.CreateOrderRequestDto;
import com.example.demo.dto.response.OrderDetailDto;
import com.example.demo.dto.response.OrderDto;
import com.example.demo.entity.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.repository.*;
import com.example.demo.util.EcpayCheckMacValueUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private EcpayProperties ecpayProperties; // <<--【新增】注入 EcpayProperties 以取得 Key/IV
    @Autowired
    private EcpayService ecpayService;

    /**
     * 從購物車建立訂單
     */
    @Transactional
    public OrderDto createOrderFromCart(Long customerId, CreateOrderRequestDto requestDto) {
        // 1. 找到使用者的購物車
        Cart cart = cartRepository.findByCCustomer_CustomerId(customerId)
                .orElseThrow(() -> new IllegalStateException("找不到使用者的購物車"));

        if (cart.getCartdetails() == null || cart.getCartdetails().isEmpty()) {
            throw new IllegalStateException("購物車是空的，無法建立訂單");
        }

        // 2. 驗證收貨地址是否屬於該使用者
        CCustomerAddress address = CCustomerAddressRepository.findById(requestDto.getAddressId().longValue())
                .filter(addr -> addr.getCCustomer().getCustomerId().equals(customerId))
                .orElseThrow(() -> new EntityNotFoundException("無效的地址 ID"));

        // 3. 檢查所有商品的庫存
        for (CartDetail detail : cart.getCartdetails()) {
            Product product = detail.getProduct();
            BigDecimal requiredQuantity = BigDecimal.valueOf(detail.getQuantity());
            BigDecimal availableStock = getAvailableStock(product.getProductId());

//            int requiredQuantity = detail.getQuantity();
//            int availableStock = product.getInventories().stream()
//                    .mapToInt(inv -> inv.getUnitsinstock() - inv.getUnitsinreserved())
//                    .sum();
            if (availableStock < requiredQuantity) {
                throw new IllegalStateException("商品 [" + product.getName() + "] 庫存不足");
            }
        }

        Platform defaultPlatform = platformRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("找不到預設的平台 (ID: 1)"));

        // 4. 建立 Order 主體
        Order newOrder = new Order();

        // ====================== 【這裏是修改重點】 ======================
        // 原本的寫法:
        // String merchantTradeNo = "ORDER" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);

        // 修改後的寫法 (確保長度在20以內):
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomPart = UUID.randomUUID().toString().substring(0, 6);
        String merchantTradeNo = "T" + timestamp + randomPart; // T(1) + timestamp(13) + random(6) = 20
        newOrder.setMerchantTradeNo(merchantTradeNo);
        // ===============================================================

        newOrder.setPlatform(defaultPlatform);
        newOrder.setCCustomer(cart.getCCustomer());
        newOrder.setCCustomerAddress(address);
        newOrder.setOrderdate(LocalDate.now());
        newOrder.setCreateat(LocalDateTime.now());
        newOrder.setUpdateat(LocalDateTime.now());
        newOrder.setOrderDetails(new ArrayList<>());

        // ====================== 【整合您的核心邏輯】 ======================
        // 從 requestDto 取得並設定付款方式
        // 假設 CreateOrderRequestDto 有 getPaymentMethod() 方法回傳 PaymentMethod Enum
        PaymentMethod paymentMethod = PaymentMethod.valueOf(requestDto.getPaymentMethod().toUpperCase());
        newOrder.setPaymentMethod(paymentMethod);

        // 根據付款方式設定訂單和付款的初始狀態
        switch (paymentMethod) {
            case ONLINE_PAYMENT:
                newOrder.setOrderStatus(OrderStatus.PENDING_PAYMENT); // 狀態為：待付款
                newOrder.setPaymentStatus(PaymentStatus.UNPAID);       // 付款狀態：未付款
                break;
            case CASH_ON_DELIVERY:
                newOrder.setOrderStatus(OrderStatus.PENDING_SHIPMENT); // 狀態為：待出貨
                newOrder.setPaymentStatus(PaymentStatus.UNPAID);        // 付款狀態：未付款
                break;
        }
        // ===============================================================

        double calculatedTotalAmount = 0.0;

        // 5. 從 CartDetail 複製到 OrderDetail，並更新庫存預訂量
        for (CartDetail detail : cart.getCartdetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(detail.getProduct());
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setUnitprice(detail.getProduct().getUnitprice());
            orderDetail.setCreateat(LocalDateTime.now());
            orderDetail.setUpdateat(LocalDateTime.now());
            newOrder.addOrderDetail(orderDetail);

            calculatedTotalAmount += detail.getProduct().getUnitprice() * detail.getQuantity();

            List<Inventory> inventories = detail.getProduct().getInventories();
            if (!inventories.isEmpty()) {
                Inventory inventoryToUpdate = inventories.get(0);
                inventoryToUpdate.setUnitsinreserved(inventoryToUpdate.getUnitsinreserved() + detail.getQuantity());
                inventoryRepository.save(inventoryToUpdate);
            }
        }
        newOrder.setTotalAmount(calculatedTotalAmount);

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

    // --- 綠界 ---
    /**
     * 【新增】處理門市選擇結果，並觸發建立物流訂單
     */
    @Transactional
    public void processStoreSelection(Map<String, String> replyData) {
        // 這裡也可以加上 CheckMacValue 驗證 (為求簡潔暫時省略)
        String merchantTradeNo = replyData.get("MerchantTradeNo");
        Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單: " + merchantTradeNo));

        // 可將回傳的門市名稱、地址等資訊存到訂單的備註欄位
//        order.setNote("物流門市: " + replyData.get("CVSStoreName")); // 假設 Order 有 Note 欄位

        // 觸發步驟 B
        String result = ecpayService.createLogisticsOrder(order, replyData);
        System.out.println("建立物流訂單結果: " + result);

        // 解析 result，如果成功，可以更新訂單狀態為 PENDING_SHIPMENT
        if (result != null && result.startsWith("1|")) {
            order.setOrderStatus(OrderStatus.PENDING_SHIPMENT);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("建立物流訂單失敗: " + result);
        }
    }

    /**
     * 【新增】處理綠界物流回調的業務邏輯
     */
    @Transactional
    public void processLogisticsCallback(Map<String, String> callbackData) {
        // ... 此方法內部的驗證和 switch-case 邏輯完全正確，維持不變 ...
        String receivedMacValue = callbackData.get("CheckMacValue");
        // 1. 為了驗證，需要從 callbackData 中移除 CheckMacValue
        Map<String, String> dataToVerify = new java.util.TreeMap<>(callbackData);
        dataToVerify.remove("CheckMacValue");

        String expectedMacValue = EcpayCheckMacValueUtil.generate(
                dataToVerify,
                ecpayProperties.getLogistics().getHashKey(),
                ecpayProperties.getLogistics().getHashIv()
        );

        if (receivedMacValue == null || !receivedMacValue.equals(expectedMacValue)) {
            throw new SecurityException("物流回調 CheckMacValue 驗證失敗！");
        }

        // 2. 取得訂單編號和物流狀態碼
        String merchantTradeNo = callbackData.get("MerchantTradeNo");
        String rtnCode = callbackData.get("RtnCode");

        // 3. 根據商家訂單編號找到我們的訂單
        Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo) // <<--【重要】需要在 OrderRepository 新增此方法
                .orElseThrow(() -> new EntityNotFoundException("找不到對應的訂單編號: " + merchantTradeNo));

        // 4. 根據物流狀態碼更新訂單狀態
        updateOrderStatusFromLogistics(order, rtnCode);
    }

    /**
     * 【新增】根據物流狀態碼更新訂單狀態的私有方法
     */
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

    // ====================== 【以下為新增的程式碼】 ======================
    /**
     * 處理綠界金流付款後的回調通知
     * @param callbackData 來自綠界的回調資料
     */
    @Transactional
    public void processPaymentCallback(Map<String, String> callbackData) {

        // 1. 驗證 CheckMacValue，確保請求的合法性
        String receivedMacValue = callbackData.get("CheckMacValue");
        if (receivedMacValue == null) {
            throw new IllegalArgumentException("缺少 CheckMacValue，請求無效");
        }

        // 為了產生我們自己的 CheckMacValue，需要先把綠界傳來的 CheckMacValue 從 Map 中移除
        // 但因為 @RequestParam 建立的 Map 是不可變的，我們先複製一份
        Map<String, String> dataToVerify = new java.util.TreeMap<>(callbackData);
        dataToVerify.remove("CheckMacValue");

        String expectedMacValue = EcpayCheckMacValueUtil.generate(
                dataToVerify,
                ecpayProperties.getAio().getHashKey(),
                ecpayProperties.getAio().getHashIv()
        );

        if (!receivedMacValue.equals(expectedMacValue)) {
            throw new SecurityException("金流回調 CheckMacValue 驗證失敗！");
        }

        // 2. 驗證通過後，取得訂單編號和交易狀態
        String merchantTradeNo = callbackData.get("MerchantTradeNo");
        String rtnCode = callbackData.get("RtnCode"); // RtnCode = 1 表示交易成功

        if ("1".equals(rtnCode)) {
            // 3. 根據商家訂單編號找到我們的訂單
            //    注意：您需要在 OrderRepository 中新增 findByMerchantTradeNo 方法
            Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo)
                    .orElseThrow(() -> new EntityNotFoundException("找不到對應的訂單編號: " + merchantTradeNo));

            // 4. 更新訂單狀態
            // 只有在訂單處於 PENDING_PAYMENT 時才更新，避免重複處理
            if (order.getOrderStatus() == OrderStatus.PENDING_PAYMENT) {
                order.setPaymentStatus(PaymentStatus.PAID);
                order.setOrderStatus(OrderStatus.PENDING_SHIPMENT); // 付款成功，轉為待出貨
                order.setUpdateat(LocalDateTime.now());
                orderRepository.save(order);
                System.out.println("訂單 " + merchantTradeNo + " 付款成功，狀態已更新為待出貨。");
            } else {
                System.out.println("訂單 " + merchantTradeNo + " 的狀態不是 PENDING_PAYMENT，可能已被處理。");
            }
        } else {
            // 交易失敗的處理邏輯
            System.out.println("訂單 " + merchantTradeNo + " 交易失敗，RtnCode: " + rtnCode);
            // 您可以考慮將訂單狀態更新為 CANCELLED
        }
    }

}
