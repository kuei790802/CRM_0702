package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {
    private String userName;
    private String email;

    private String oldPassword; // 若更改密碼，需驗證
    private String newPassword;
}
