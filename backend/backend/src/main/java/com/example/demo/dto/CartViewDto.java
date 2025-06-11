package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartViewDto {
    private Long cartid;
    private List<CartDetailDto> cartdetails;
    private Integer quantity;
    private Double totalprice;
}
