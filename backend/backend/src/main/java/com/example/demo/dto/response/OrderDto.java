package com.example.demo.dto.response;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long orderId;
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private double totalPrice;
    private List<OrderDetailDto> orderDetails;
}
