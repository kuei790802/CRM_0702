package com.example.demo.controller;

import com.example.demo.dto.ecpay.AioCheckoutDto;
import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatus;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.EcpayService;
import com.example.demo.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private EcpayService ecpayService;
    @Autowired
    private com.example.demo.config.EcpayProperties ecpayProperties;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

//    @GetMapping("/pay") // test用
//    public String pay(Model model) {
//        String merchantTradeNo = "test" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 14);
//        AioCheckoutDto dto = ecpayService.createAioOrder(100, "範例商品一批", merchantTradeNo);
//
//        model.addAttribute("ecpayUrl", ecpayProperties.getAio().getUrl());
//        model.addAttribute("aioCheckoutDto", dto);
//
//        return "ecpay-checkout";
//    }

    /**
     * 【建議新增】根據真實訂單 ID 發起金流支付
     * @param orderId 訂單 ID
     * @param model Spring Model
     * @return 導向到 ecpay-checkout 模板
     */
    @GetMapping("/order/{orderId}")
    public String payByOrder(@PathVariable Long orderId, Model model) {
        // 1. 根據 ID 找到真實訂單
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單 ID: " + orderId));

        // 2. 檢查訂單狀態是否為 PENDING_PAYMENT
        if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("此訂單狀態不是待付款，無法進行支付");
        }

        // === 【修改重點】將整個 order 物件傳遞給 service ===
        AioCheckoutDto dto = ecpayService.createAioOrder(order);

        model.addAttribute("ecpayUrl", ecpayProperties.getAio().getUrl());
        model.addAttribute("aioCheckoutDto", dto);

        return "ecpay-checkout";
    }

    /**
     * 【修改】接收綠界金流 AIO 付款結果通知
     * @param callbackData 綠界發送的 POST 表單資料
     * @return 字串 "1|OK" 或 "0|ErrorMessage"
     */
    @PostMapping("/ecpay/callback")
    @ResponseBody
    public String handleCallback(@RequestParam Map<String, String> callbackData) {
        System.out.println("ECPay 金流回調收到: " + callbackData);

        try {
            // 將處理邏輯交給 Service 層
            orderService.processPaymentCallback(callbackData);
            // 成功處理後，必須回傳 "1|OK"
            return "1|OK";
        } catch (Exception e) {
            System.err.println("處理金流回調時發生錯誤: " + e.getMessage());
            // 雖然發生錯誤，但為避免綠界不斷重試，通常仍建議回傳 "1|OK"
            // 並在系統內部做好錯誤日誌記錄，以便追查問題
            return "1|OK";
        }
    }
}