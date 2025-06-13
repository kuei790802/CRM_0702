package com.example.demo.repository;

import com.example.demo.entity.CCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CCustomerRepo extends JpaRepository<CCustomer, Long> {
    Optional<CCustomer> findByAccount(String account);


}
