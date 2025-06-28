package com.example.demo.controller.erp;


import com.example.demo.dto.erp.SalesOrderCreateDTO;
import com.example.demo.dto.erp.SalesOrderSummaryDTO;
import com.example.demo.dto.erp.SalesOrderViewDTO;
import com.example.demo.dto.erp.SalesOrderUpdateDTO;
import com.example.demo.entity.SalesOrder;
import com.example.demo.enums.SalesOrderStatus;
import com.example.demo.service.erp.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @Operation(summary = "建立銷售訂單", description = "建立銷售訂單包含多筆銷售明細")
    @PostMapping
    public ResponseEntity<SalesOrderViewDTO> createSalesOrder(
                                                               @Valid @RequestBody SalesOrderCreateDTO createDTO){
        Long currentUserId=1L;
        SalesOrder createdOrderEntity = salesOrderService.createSalesOrder(createDTO, currentUserId);

        SalesOrderViewDTO viewDTO = SalesOrderViewDTO.fromEntity(createdOrderEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(viewDTO);
    }

    @Operation(summary = "更新銷售訂單", description = "更新銷售訂單包含多筆銷售明細")
    @PutMapping("/{id}")
    public ResponseEntity<SalesOrderViewDTO> updateSalesOrder(
            @PathVariable Long id,
            @Valid @RequestBody SalesOrderUpdateDTO updateDTO) {
        Long currentUserId = 1L;
        SalesOrderViewDTO updatedOrder = salesOrderService.updateSalesOrder(id, updateDTO, currentUserId);
        return ResponseEntity.ok(updatedOrder);
    }


    @Operation(summary = "查詢銷售訂單", description = "查詢銷售訂單包含多筆銷售明細")
    @GetMapping
    public ResponseEntity<Page<SalesOrderSummaryDTO>> searchSalesOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false)SalesOrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size =10)Pageable pageable){

        Page<SalesOrderSummaryDTO> resultPage = salesOrderService.searchSalesOrders(
                customerId, status, startDate, endDate, keyword, pageable);

        return ResponseEntity.ok(resultPage);

    }

    @Operation(summary = "查詢銷售訂單明細", description = "查詢銷售訂單明細")
    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderViewDTO> getSalesOrderById(@PathVariable Long id) {
        SalesOrderViewDTO orderDetails = salesOrderService.getSalesOrderById(id);
        return ResponseEntity.ok(orderDetails);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<SalesOrderViewDTO> deleteSalesOrder(@PathVariable Long id) {
        Long currentUserId = 1L;
        SalesOrderViewDTO deletedOrder = salesOrderService.deleteSalesOrder(id, currentUserId);
        return ResponseEntity.ok(deletedOrder);
    }



}
