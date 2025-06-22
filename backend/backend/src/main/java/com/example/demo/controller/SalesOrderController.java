package com.example.demo.controller;


import com.example.demo.dto.SalesOrderCreateDTO;
import com.example.demo.entity.SalesOrder;
import com.example.demo.service.SalesOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
