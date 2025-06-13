package com.example.demo.repository;

import com.example.demo.entity.QuotationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationDetailRepository extends JpaRepository<QuotationDetail, Long> {
}
