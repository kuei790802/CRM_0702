package com.example.demo.repository;

import com.example.demo.entity.CCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCsutomerRepository extends JpaRepository<CCustomer, Long> {
}
