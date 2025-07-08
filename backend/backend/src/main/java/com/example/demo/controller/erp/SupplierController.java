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

import com.example.demo.dto.SupplierDTO;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.SupplierRepository;
import com.example.dto.mapper.SupplierMapper;




@RestController
@RequestMapping("/api/erp/suppliers")
public class SupplierController {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Autowired
    public SupplierController(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierDTO> supplierDTOs = suppliers.stream()
                .map(supplierMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(supplierDTOs);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<SupplierDTO>> getSuppliersPaged(@PageableDefault(size = 20) Pageable pageable) {
        Page<Supplier> suppliers = supplierRepository.findAll(pageable);
        Page<SupplierDTO> supplierDTOs = suppliers.map(supplierMapper::toDto);
        return ResponseEntity.ok(supplierDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long id) {
        return supplierRepository.findById(id)
                .map(supplier -> ResponseEntity.ok(supplierMapper.toDto(supplier)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierDTO supplierDTO) {
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return ResponseEntity.ok(supplierMapper.toDto(savedSupplier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(
            @PathVariable Long id, 
            @RequestBody SupplierDTO supplierDTO) {
        
        return supplierRepository.findById(id)
                .map(existingSupplier -> {
                    supplierDTO.setSupplierId(id);
                    Supplier updatedSupplier = supplierMapper.toEntity(supplierDTO);
                    updatedSupplier = supplierRepository.save(updatedSupplier);
                    return ResponseEntity.ok(supplierMapper.toDto(updatedSupplier));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplierRepository.delete(supplier);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupplierDTO>> getActiveSuppliers() {
        List<Supplier> activeSuppliers = supplierRepository.findByIsActiveTrue();
        List<SupplierDTO> supplierDTOs = activeSuppliers.stream()
                .map(supplierMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(supplierDTOs);
    }
}
