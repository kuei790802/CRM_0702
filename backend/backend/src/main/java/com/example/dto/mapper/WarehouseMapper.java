package com.example.dto.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.WarehouseDTO;
import com.example.demo.entity.Warehouse;

@Component
public class WarehouseMapper {
    public WarehouseDTO toDto(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        
        WarehouseDTO dto = new WarehouseDTO();
        dto.setWarehouseId(warehouse.getWarehouseId());
        dto.setName(warehouse.getName());
        dto.setActive(warehouse.isActive());
        dto.setCreatedBy(warehouse.getCreatedBy());
        dto.setCreatedAt(warehouse.getCreatedAt());
        
        return dto;
    }

    public Warehouse toEntity(WarehouseDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(dto.getWarehouseId());
        warehouse.setName(dto.getName());
        warehouse.setActive(dto.isActive());
        warehouse.setCreatedBy(dto.getCreatedBy());
        // createdAt is set automatically by the entity
        
        return warehouse;
    }
}
