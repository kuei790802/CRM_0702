package com.example.demo.repository;

import com.example.demo.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<Authority, Long> {
}
