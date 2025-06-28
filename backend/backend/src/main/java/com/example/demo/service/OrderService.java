package com.example.demo.service;

import com.example.demo.config.EcpayProperties;
import com.example.demo.dto.request.CreateOrderRequestDto;
import com.example.demo.dto.response.OrderDetailDto;
import com.example.demo.dto.response.OrderDto;
import com.example.demo.entity.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.erp.InventoryService;
import com.example.demo.util.EcpayCheckMacValueUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

import static com.example.demo.enums.PaymentMethod.CASH_ON_DELIVERY;
import static com.example.demo.enums.PaymentMethod.ONLINE_PAYMENT;

@Service
@RequiredArgsConstructor // 使用 Lombok 的 @RequiredArgsConstructor 簡化依賴注入
public class OrderService {

    // 使用 final 關鍵字，Lombok 會自動生成包含這些欄位的建構子
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CCustomerRepo cCustomerRepo;
    private final CCustomerAddressRepository cCustomerAddressRepository;
    private final InventoryService inventoryService;
    private final PlatformRepository platformRepository;
    private final EntityManager entityManager;
    private final CCustomerService cCustomerService;
    private final UserRepository userRepository; // ✨ 1. 確保 UserRepository 已注入
    private final EcpayProperties ecpayProperties;
    private final EcpayService ecpayService;

    // 定義一個常數來代表「系統使用者」的ID
    private static final Long SYSTEM_USER_ID = 1L;

    /**
     * 從購物車建立訂單
     */
    @Transactional
    public OrderDto createOrderFromCart(Long customerId, CreateOrderRequestDto requestDto) {
        CCustomer customer = cCustomerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到顧客，ID: " + customerId));

        Cart cart = cartRepository.findByCCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("找不到顧客 " + customer.getName() + " 的購物車"));

        if (cart.getCartdetails() == null || cart.getCartdetails().isEmpty()) {
            throw new IllegalStateException("購物車是空的，無法建立訂單。");
        }

        CCustomerAddress address = cCustomerAddressRepository.findById((long)requestDto.getAddressId())
                .filter(addr -> addr.getCCustomer().getCustomerId().equals(customerId))
                .orElseThrow(() -> new EntityNotFoundException("無效的地址 ID: " + requestDto.getAddressId()));

        User systemUser = userRepository.findById(SYSTEM_USER_ID)
                .orElseThrow(() -> new IllegalStateException("資料庫中找不到 ID 為 " + SYSTEM_USER_ID + " 的系統使用者帳號，請先建立"));

        Platform defaultPlatform = platformRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("找不到預設的平台 (ID: 1)"));

        Order newOrder = new Order();
        newOrder.setPlatform(defaultPlatform);
        newOrder.setCCustomer(customer); // 維持您正確的寫法
        newOrder.setCCustomerAddress(address);
        newOrder.setOrderdate(LocalDate.now());

        PaymentMethod paymentMethod = PaymentMethod.valueOf(requestDto.getPaymentMethod());
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setCreateat(LocalDateTime.now());
        newOrder.setUpdateat(LocalDateTime.now());

        switch (paymentMethod) {
            case ONLINE_PAYMENT:
                newOrder.setOrderStatus(OrderStatus.PENDING_PAYMENT);
                newOrder.setPaymentStatus(PaymentStatus.UNPAID);
                break;
            case CASH_ON_DELIVERY:
                newOrder.setOrderStatus(OrderStatus.PENDING_SHIPMENT);
                newOrder.setPaymentStatus(PaymentStatus.UNPAID);
                break;
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomPart = UUID.randomUUID().toString().substring(0, 6);
        newOrder.setMerchantTradeNo("T" + timestamp + randomPart);

        double totalAmount = 0;
        // ✨【關鍵修正】✨: 不要建立新的 List，而是直接操作 newOrder 內部的 orderDetails
        for (CartDetail cartDetail : cart.getCartdetails()) {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrder(newOrder); // 設定關聯
            orderDetail.setProduct(cartDetail.getProduct()); // 設定關聯

            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setUnitprice(cartDetail.getProduct().getBasePrice().doubleValue());
            orderDetail.setCreateat(LocalDateTime.now());
            orderDetail.setUpdateat(LocalDateTime.now());

            // 直接將新的 detail 加入到 newOrder 持有的列表中
            newOrder.getOrderDetails().add(orderDetail);
            totalAmount += orderDetail.getQuantity() * orderDetail.getUnitprice();
        }
        newOrder.setTotalAmount(totalAmount);

        // 儲存 Order，JPA 會因為 Cascade 設定而一併儲存所有 OrderDetail
        Order savedOrder = orderRepository.save(newOrder);

        // 預留庫存
        for (OrderDetail detail : savedOrder.getOrderDetails()) {
            inventoryService.reserveStock(
                    detail.getProduct().getProductId(),
                    1L,
                    BigDecimal.valueOf(detail.getQuantity()),
                    "SALES_ORDER",
                    savedOrder.getOrderid(),
                    detail.getProduct().getProductId(),
                    systemUser.getUserId()
            );
        }

        // 清空購物車
        cartRepository.delete(cart);

        return mapToOrderDto(savedOrder);
    }

    public List<OrderDto> getOrdersBycustomerId(Long customerId) {
        return orderRepository.findByCCustomer_CustomerIdOrderByOrderdateDesc(customerId).stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(this::mapToOrderDto);
    }

    @Transactional
    public OrderDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單 ID: " + orderId));

        OrderStatus oldStatus = order.getOrderStatus();
        order.setOrderStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);

        if (newStatus == OrderStatus.COMPLETE && oldStatus != OrderStatus.COMPLETE) {
            if (order.getCCustomer() != null && order.getCCustomer().getCustomerId() != null) {
                Long customerId = order.getCCustomer().getCustomerId();
                cCustomerService.updateCustomerSpending(customerId);
            }
        }
        return mapToOrderDto(updatedOrder);
    }

    public Page<OrderDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByOrderStatus(status, pageable)
                .map(this::mapToOrderDto);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> searchOrders(LocalDate startTime, LocalDate endTime, String productName) {
        List<Order> orders = orderRepository.searchOrders(startTime, endTime, productName);
        return orders.stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::mapToOrderDto)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單 ID: " + orderId));
    }

    private OrderDto mapToOrderDto(Order order) {
        List<OrderDetailDto> detailDtos = new ArrayList<>();
        if (order.getOrderDetails() != null) {
            for (OrderDetail detail : order.getOrderDetails()) {
                if(detail != null && detail.getProduct() != null) {
                    detailDtos.add(new OrderDetailDto(
                            detail.getProduct().getProductId(),
                            detail.getProduct().getName(),
                            detail.getQuantity(),
                            detail.getUnitprice()));
                }
            }
        }

        return new OrderDto(
                order.getOrderid(),
                order.getCCustomer().getCustomerId(),
                order.getCCustomer().getName(),
                order.getOrderdate(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                detailDtos);
    }

    @Transactional
    public void processStoreSelection(Map<String, String> replyData) {
        String merchantTradeNo = replyData.get("MerchantTradeNo");
        Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單: " + merchantTradeNo));

        String result = ecpayService.createLogisticsOrder(order, replyData);
        System.out.println("建立物流訂單結果: " + result);

        if (result != null && result.startsWith("1|")) {
            order.setOrderStatus(OrderStatus.PENDING_SHIPMENT);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("建立物流訂單失敗: " + result);
        }
    }

    @Transactional
    public void processLogisticsCallback(Map<String, String> callbackData) {
        String receivedMacValue = callbackData.get("CheckMacValue");
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

        String merchantTradeNo = callbackData.get("MerchantTradeNo");
        String rtnCode = callbackData.get("RtnCode");

        Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo)
                .orElseThrow(() -> new EntityNotFoundException("找不到對應的訂單編號: " + merchantTradeNo));

        updateOrderStatusFromLogistics(order, rtnCode);
    }

    private void updateOrderStatusFromLogistics(Order order, String logisticsStatusCode) {
        switch (logisticsStatusCode) {
            case "2063":
                order.setOrderStatus(OrderStatus.PENDING_PICKUP);
                break;
            case "2067":
                if (order.getPaymentMethod() == ONLINE_PAYMENT) {
                    order.setOrderStatus(OrderStatus.COMPLETE);
                    triggerUpdateSpending(order);
                }
                break;
            case "2073":
                if (order.getPaymentMethod() == CASH_ON_DELIVERY) {
                    order.setOrderStatus(OrderStatus.COMPLETE);
                    order.setPaymentStatus(PaymentStatus.PAID);
                    triggerUpdateSpending(order);
                }
                break;
        }
        orderRepository.save(order);
    }

    private void triggerUpdateSpending(Order order) {
        if (order.getCCustomer() != null && order.getCCustomer().getCustomerId() != null) {
            cCustomerService.updateCustomerSpending(order.getCCustomer().getCustomerId());
        }
    }

    @Transactional
    public void processPaymentCallback(Map<String, String> callbackData) {
        String receivedMacValue = callbackData.get("CheckMacValue");
        if (receivedMacValue == null) {
            throw new IllegalArgumentException("缺少 CheckMacValue，請求無效");
        }
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

        String merchantTradeNo = callbackData.get("MerchantTradeNo");
        String rtnCode = callbackData.get("RtnCode");

        if ("1".equals(rtnCode)) {
            Order order = orderRepository.findByMerchantTradeNo(merchantTradeNo)
                    .orElseThrow(() -> new EntityNotFoundException("找不到對應的訂單編號: " + merchantTradeNo));

            if (order.getOrderStatus() == OrderStatus.PENDING_PAYMENT) {
                order.setPaymentStatus(PaymentStatus.PAID);
                order.setOrderStatus(OrderStatus.PENDING_SHIPMENT);
                order.setUpdateat(LocalDateTime.now());
                orderRepository.save(order);
                System.out.println("訂單 " + merchantTradeNo + " 付款成功，狀態已更新為待出貨。");
            } else {
                System.out.println("訂單 " + merchantTradeNo + " 的狀態不是 PENDING_PAYMENT，可能已被處理。");
            }
        } else {
            System.out.println("訂單 " + merchantTradeNo + " 交易失敗，RtnCode: " + rtnCode);
        }
    }
}
