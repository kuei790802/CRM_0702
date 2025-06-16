package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CCustomerProfileResponse {
    private String account;
    private String customerName;
    private String email;
    private String address;
    private LocalDate birthday;
}
