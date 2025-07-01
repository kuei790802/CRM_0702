package com.example.demo.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    private Integer addressId;      // 使用者選擇的收貨地址ID
    private String paymentMethod;   // 付款方式，例如 "CREDIT_CARD"
    // 注意：要從哪個購物車下單，我們直接從登入使用者身上取得，不需要前端傳
    private Long customerCouponId; // 可以是 null，代表不使用優惠券
}
