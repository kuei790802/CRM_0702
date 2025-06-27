package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailDto {
    private String productName;
    private int quantity;
    private double unitPrice; // 下單時的單價
}
