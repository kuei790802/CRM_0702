package com.example.demo.controller.erp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.WarehouseDTO;
import com.example.demo.entity.Warehouse;
import com.example.demo.repository.WarehouseRepository;
import com.example.dto.mapper.WarehouseMapper;

@RestController
@RequestMapping("/api/erp/warehouses")
public class WarehouseController {

private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public WarehouseController(WarehouseRepository warehouseRepository, WarehouseMapper warehouseMapper) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<WarehouseDTO> warehouseDTOs = warehouses.stream()
                .map(warehouseMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(warehouseDTOs);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<WarehouseDTO>> getWarehousesPaged(@PageableDefault(size = 20) Pageable pageable) {
        Page<Warehouse> warehouses = warehouseRepository.findAll(pageable);
        Page<WarehouseDTO> warehouseDTOs = warehouses.map(warehouseMapper::toDto);
        return ResponseEntity.ok(warehouseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable Long id) {
        return warehouseRepository.findById(id)
                .map(warehouse -> ResponseEntity.ok(warehouseMapper.toDto(warehouse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return ResponseEntity.ok(warehouseMapper.toDto(savedWarehouse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(
            @PathVariable Long id, 
            @RequestBody WarehouseDTO warehouseDTO) {
        
        return warehouseRepository.findById(id)
                .map(existingWarehouse -> {
                    warehouseDTO.setWarehouseId(id);
                    Warehouse updatedWarehouse = warehouseMapper.toEntity(warehouseDTO);
                    updatedWarehouse = warehouseRepository.save(updatedWarehouse);
                    return ResponseEntity.ok(warehouseMapper.toDto(updatedWarehouse));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        return warehouseRepository.findById(id)
                .map(warehouse -> {
                    warehouseRepository.delete(warehouse);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<WarehouseDTO>> getActiveWarehouses() {
        List<Warehouse> activeWarehouses = warehouseRepository.findByIsActiveTrue();
        List<WarehouseDTO> warehouseDTOs = activeWarehouses.stream()
                .map(warehouseMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(warehouseDTOs);
    }
}