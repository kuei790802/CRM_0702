package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ContactRequest {

    @NotBlank(message = "聯絡人姓名不能為空")
    private String contactName;

    private String title;

    @Email(message = "電子郵件格式不正確")
    private String email;

    private String phone;

    private String notes;

    @NotNull(message = "客戶ID不能為空")
    private Long customerId; // 關聯客戶的 ID



}
