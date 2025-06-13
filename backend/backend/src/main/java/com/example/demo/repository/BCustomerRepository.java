package com.example.demo.repository;

import com.example.demo.entity.BCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BCustomerRepository extends JpaRepository<BCustomer, Long> {

}
