package com.example.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactDto {

    private Long contactId;
    private String contactName;
    private String title;
    private String email;
    private String phone;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ----- 關聯客戶資訊 -----
    private Long customerId;   // 所屬客戶的 ID
    private String customerName; // 所屬客戶的名稱
}
