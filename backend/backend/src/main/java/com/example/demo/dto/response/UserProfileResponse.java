package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String account;
    private String email;
    private String userName;
    private boolean isActive;
    private String roleName;
    private List<String> authorityCodes;
    private LocalDate accessStartDate;
    private LocalDate accessEndDate;
    private LocalDateTime lastLogin;
}
