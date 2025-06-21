package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Getter
@Setter
public class ShipmentRequestDTO {

    @NotNull(message = "出貨倉庫ID不可為空")
    private Long warehouseId;

}
