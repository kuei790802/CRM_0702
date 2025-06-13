package com.example.demo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateRequestDTO {
    
    @NotEmpty(message = "商品名稱不可為空")
    private String name;

    private String description;

    @NotNull(message = "商品分類ID不可為空")
    private Long categoryId;

    @NotNull(message = "商品單位ID不可為空")
    private Long unitId;

    @NotNull(message = "基本售價不可為空")
    @DecimalMin(value = "0.0", inclusive = false, message = "售價必須大於 0")
    private BigDecimal basePrice;

    @NotEmpty(message = "稅別不可為空")
    private String taxType;

    @NotNull(message = "啟用狀態不可為空")
    private Boolean isActive;
}
