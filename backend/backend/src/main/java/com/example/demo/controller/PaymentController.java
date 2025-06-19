package com.example.demo.controller;

import com.example.demo.dto.ecpay.AioCheckoutDto;
import com.example.demo.service.EcpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.UUID;

@Controller
public class PaymentController {

    @Autowired
    private EcpayService ecpayService;

    @Autowired
    private com.example.demo.config.EcpayProperties ecpayProperties;

    @GetMapping("/pay")
    public String pay(Model model) {
        String merchantTradeNo = "test" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 14);
        AioCheckoutDto dto = ecpayService.createAioOrder(100, "範例商品一批", merchantTradeNo);

        model.addAttribute("ecpayUrl", ecpayProperties.getAio().getUrl());
        model.addAttribute("aioCheckoutDto", dto);

        return "ecpay-checkout";
    }

    @PostMapping("/ecpay/callback")
    @ResponseBody
    public String handleCallback(Map<String, String> callbackData) {
        // 在這裡處理 ECPay 的回傳結果
        // 驗證 CheckMacValue 等...
        System.out.println("ECPay callback received: " + callbackData);
        return "1|OK";
    }
}