package com.example.demo.dto.request;

import com.example.demo.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateStatusRequestDto {
    private OrderStatus status;
}
