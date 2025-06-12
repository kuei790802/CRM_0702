package com.example.demo.repository;

import com.example.demo.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    // 查找屬於特定客戶的所有聯絡人
    List<Contact> findByCustomerId(Long customerId);
}
