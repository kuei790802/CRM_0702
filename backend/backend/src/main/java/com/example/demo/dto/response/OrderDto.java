package com.example.demo.dto.response;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderDto {

    private Long orderid;
    private Long customerId;
    private String customerName;
    private LocalDate orderdate;
    private OrderStatus orderStatus;
    private Double totalAmount;
    private List<OrderDetailDto> orderDetails;

    // ✨ 1. 新增一個無參數的建構子 (好習慣)
    public OrderDto() {
    }

    // ✨ 2. 新增一個公開的、包含所有參數的建構子
    public OrderDto(Long orderid, Long customerId, String customerName, LocalDate orderdate, OrderStatus orderStatus, Double totalAmount, List<OrderDetailDto> orderDetails) {
        this.orderid = orderid;
        this.customerId = customerId;
        this.customerName = customerName;
        this.orderdate = orderdate;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
    }
}
