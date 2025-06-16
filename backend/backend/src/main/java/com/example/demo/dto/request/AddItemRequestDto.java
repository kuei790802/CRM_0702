package com.example.demo.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class AddItemRequestDto {
    private Long productid;
    private Integer quantity;
}
