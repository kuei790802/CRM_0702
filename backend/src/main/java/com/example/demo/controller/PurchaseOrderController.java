package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PurchaseOrderCreateDTO;
import com.example.demo.entity.PurchaseOrder;
import com.example.demo.service.PurchaseOrderService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/purchaseOrders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService poService;

    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(
        @Valid
        @RequestBody
        PurchaseOrderCreateDTO poCreateDTO){
            Long currentUserId = 1L;
            PurchaseOrder createOrder = poService.createPurchaseOrder(poCreateDTO,currentUserId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createOrder);
        }
    

}
