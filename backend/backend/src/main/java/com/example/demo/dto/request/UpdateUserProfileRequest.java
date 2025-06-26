package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {
    private String userName;
    private String email;

    private String oldPassword; // 若更改密碼，需驗證
    private String newPassword;

    // admin
    private String roleName;
    private List<String> authorityCodes;
    private LocalDate accessEndDate;
}
