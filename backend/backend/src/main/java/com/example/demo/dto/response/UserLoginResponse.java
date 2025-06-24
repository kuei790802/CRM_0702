package com.example.demo.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponse{
    private String token;
    private String account;
    private Long userId;
    private String userName;
    private String email;
    private String roleName;
    private LocalDateTime lastLogin;
    private LocalDate accessEndDate;
}
