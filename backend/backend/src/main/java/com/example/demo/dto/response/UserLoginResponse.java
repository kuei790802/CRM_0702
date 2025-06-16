package com.example.demo.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;


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
}
