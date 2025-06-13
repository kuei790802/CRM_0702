package com.example.demo.repository;

import com.example.demo.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRpository extends JpaRepository<Quotation, Long> {
}
