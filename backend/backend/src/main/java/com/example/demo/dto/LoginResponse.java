package com.example.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String account;
    private String customerName;
}
