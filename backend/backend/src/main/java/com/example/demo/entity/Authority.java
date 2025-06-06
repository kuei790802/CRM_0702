package com.example.demo.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Authority {


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
