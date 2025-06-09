package com.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.PurchaseOrderCreateDTO;
import com.example.demo.dto.PurchaseOrderDetailCreateDTO;
import com.example.demo.entity.PurchaseOrder;

import jakarta.inject.Inject;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Inject Mock
    private PurchaseOrderService purchaseOrderService;

    @Test
    void testCreatePurchaseOrder_Success(){
        PurchaseOrderDetailCreateDTO detailCreateDTO = new PurchaseOrderDetailCreateDTO();
        detailCreateDTO.setProductId(1L);
        detailCreateDTO.setQuantity(new BigDecimal(10));
        detailCreateDTO.setUnitPrice(new BigDecimal(100));
        detailCreateDTO.setWarehouseId(1L);

        PurchaseOrderCreateDTO orderCreateDTO = new PurchaseOrderCreateDTO();
        orderCreateDTO.setSupplierId(1L);
        orderCreateDTO.setOrderDate(LocalDate.now());
        orderCreateDTO.setCurrency("TWD");
        orderCreateDTO.setDetails(Collections.singletonList(detailCreateDTO));

        PurchaseOrder savedOrder = new PurchaseOrder();
        savedOrder.setPurchaseOrderId(1L);
        savedOrder.setStatus("DRAFT");

        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(savedOrder);

        PurchaseOrder result = purchaseOrderService.createPurchaseOrder(orderCreateDTO);

        assertNotNull(result);
        assertEquals(1L, result.getPurchaseOrderId());
        assertEquals("DRAFT", result.getStatus());

    }
}
