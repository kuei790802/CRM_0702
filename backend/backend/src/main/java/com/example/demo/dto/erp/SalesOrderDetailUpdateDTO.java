package com.example.demo.dto.erp;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SalesOrderDetailUpdateDTO {

    @NotNull(message = "產品ID不可為空")
    private Long productId;

    @NotNull(message = "訂單數量不可為空")
    @Min(value = 0, message = "訂單數量不可為負數")
    private BigDecimal quantity;

    @NotNull(message = "銷售單價不可為空")
    @Min(value = 0, message = "銷售單價不可為負數")
    private BigDecimal unitPrice;


}

