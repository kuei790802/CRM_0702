package com.example.demo.service;

import com.example.demo.dto.SalesOrderCreateDTO;
import com.example.demo.entity.SalesOrder;
import com.example.demo.repository.BCustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SalesOrderRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SalesOrderService {
    private final SalesOrderRepository salesOrderRepository;
    private final ProductRepository productRepository;
    private final BCustomerRepository bCustomerRepository;

    @Transactional
    public SalesOrder createSalesOrder(SalesOrderCreateDTO dto, Long userId){

        return null;
    }
}
