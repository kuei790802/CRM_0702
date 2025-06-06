package com.example.demo.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class UserLogAction {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
