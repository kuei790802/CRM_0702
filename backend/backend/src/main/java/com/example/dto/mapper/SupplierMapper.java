package com.example.dto.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.SupplierDTO;
import com.example.demo.entity.Supplier;

@Component
public class SupplierMapper {
    public SupplierDTO toDto(Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        
        SupplierDTO dto = new SupplierDTO();
        dto.setSupplierId(supplier.getSupplierId());
        dto.setSupplierCode(supplier.getSupplierCode());
        dto.setName(supplier.getName());
        dto.setActive(supplier.isActive());
        dto.setCreatedBy(supplier.getCreatedBy());
        dto.setCreatedAt(supplier.getCreatedAt());
        
        return dto;
    }

    public Supplier toEntity(SupplierDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Supplier supplier = new Supplier();
        supplier.setSupplierId(dto.getSupplierId());
        supplier.setSupplierCode(dto.getSupplierCode());
        supplier.setName(dto.getName());
        supplier.setActive(dto.isActive());
        supplier.setCreatedBy(dto.getCreatedBy());
        // createdAt is set automatically by the entity
        
        return supplier;
    }
}