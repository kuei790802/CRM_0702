package com.example.demo.repository;

import com.example.demo.entity.UserLogAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogActionRepo extends JpaRepository<UserLogAction, Long> {
}
