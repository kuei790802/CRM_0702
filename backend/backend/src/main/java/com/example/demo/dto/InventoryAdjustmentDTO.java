package com.example.demo.dto;


import com.example.demo.enums.MovementType;
import lombok.Data;

@Data
public class InventoryAdjustmentDTO {

    private Long productId;

    private Long warehouseId;

    private int quantity;

    private MovementType movementType;

    private String documentType;

    private Long documentId;

    private Long documentItemId;

}
