package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {

    private Long supplierId;
    private String supplierCode;
    private String name;
    private boolean isActive;
    private Long createdBy;
    private LocalDateTime createdAt;
    
}
