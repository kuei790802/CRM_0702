package com.example.demo.repository;

import com.example.demo.entity.CCustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCustomerAddressRepository extends JpaRepository<CCustomerAddress, Long> {
}
