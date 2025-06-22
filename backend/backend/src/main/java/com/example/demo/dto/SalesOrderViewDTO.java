package com.example.demo.dto;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.SalesOrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SalesOrderViewDTO {

    private Long salesOrderId;
    private String orderNumber;
    private LocalDate orderDate;
    private SalesOrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String shippingAddress;
    private String paymentMethod;
    private String remarks;
}
