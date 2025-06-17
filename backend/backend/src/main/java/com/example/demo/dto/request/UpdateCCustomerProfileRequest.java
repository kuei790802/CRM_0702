package com.example.demo.dto.request;
import lombok.Data;

@Data
public class UpdateCCustomerProfileRequest {
    private String customerName;
    private String email;
    private String address;

    private String oldPassword;          // 若要改密碼，需驗證舊密碼
    private String newPassword;
}
