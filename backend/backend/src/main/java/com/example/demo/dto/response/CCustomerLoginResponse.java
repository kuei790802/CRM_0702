package com.example.demo.dto.response;


import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CCustomerLoginResponse{
    private String token;
    private String account;
    private String customerName;
    private String email;
    private LocalDate birthday;
    private LocalDateTime createdAt;
    private Long spending;


}
