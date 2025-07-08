package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDTO {
    private Long warehouseId;
    private String name;
    private boolean isActive;
    private Long createdBy;
    private LocalDateTime createdAt;
}
