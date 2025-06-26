package com.example.demo.controller;


import com.example.demo.dto.erp.SalesOrderCreateDTO;
import com.example.demo.dto.erp.SalesOrderSummaryDTO;
import com.example.demo.dto.erp.SalesOrderViewDTO;
import com.example.demo.entity.SalesOrder;
import com.example.demo.enums.SalesOrderStatus;
import com.example.demo.service.SalesOrderService;
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

    @PostMapping
    public ResponseEntity<SalesOrder> createSalesOrder(
            @Valid @RequestBody SalesOrderCreateDTO createDTO){
        Long currentUserId=1L;
        SalesOrder createdOrder=salesOrderService
                .createSalesOrder(createDTO, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderViewDTO> getSalesOrderById(@PathVariable Long id) {
        SalesOrderViewDTO orderDetails = salesOrderService.getSalesOrderById(id);
        return ResponseEntity.ok(orderDetails);
    }


    @DeleteMapping("/{id}/")
    public ResponseEntity<SalesOrderViewDTO> deleteSalesOrder(@PathVariable Long id) {
        Long currentUserId = 1L;
        SalesOrderViewDTO deletedOrder = salesOrderService.deleteSalesOrder(id, currentUserId);
        return ResponseEntity.ok(deletedOrder);
    }



}
