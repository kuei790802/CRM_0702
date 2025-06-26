package com.example.demo.controller.erp;

import com.example.demo.dto.PurchaseOrderSummaryDTO;
import com.example.demo.dto.erp.PurchaseOrderUpdateDTO;
import com.example.demo.dto.erp.PurchaseOrderViewDTO;
import com.example.demo.enums.PurchaseOrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.erp.PurchaseOrderCreateDTO;
import com.example.demo.entity.PurchaseOrder;
import com.example.demo.service.erp.PurchaseOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/purchaseOrders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService poService;

    @PostMapping
    public ResponseEntity<PurchaseOrderViewDTO> createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderCreateDTO poCreateDTO){
        Long currentUserId = 1L; // Placeholder for actual user ID
        PurchaseOrder createdOrder = poService.createPurchaseOrder(poCreateDTO, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(poService.getPurchaseOrderViewById(createdOrder.getPurchaseOrderId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderViewDTO> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrderViewDTO orderDetails = poService.getPurchaseOrderViewById(id);
        return ResponseEntity.ok(orderDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderViewDTO> updatePurchaseOrder(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderUpdateDTO poUpdateDTO) {
        Long currentUserId = 1L; // Placeholder for actual user ID
        PurchaseOrderViewDTO updatedOrder = poService.updatePurchaseOrder(id, poUpdateDTO, currentUserId);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PurchaseOrderViewDTO> deletePurchaseOrder(@PathVariable Long id) {
        Long currentUserId = 1L; // Placeholder for actual user ID
        // Consider if response should be different, e.g., ResponseEntity.noContent()
        // Returning the (now cancelled) order DTO for confirmation
        PurchaseOrderViewDTO deletedOrder = poService.deletePurchaseOrder(id, currentUserId);
        return ResponseEntity.ok(deletedOrder);
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseOrderSummaryDTO>> searchPurchaseOrders(
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) PurchaseOrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<PurchaseOrderSummaryDTO> resultPage = poService.searchPurchaseOrders(
                supplierId, status, startDate, endDate, keyword, pageable);
        return ResponseEntity.ok(resultPage);
    }
}
