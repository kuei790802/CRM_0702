package com.example.demo.dto.response;

import com.example.demo.enums.BCustomerIndustry;
import com.example.demo.enums.BCustomerLevel;
import com.example.demo.enums.BCustomerType;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BCustomerDto {
    /**
     * 用於 API 回應，提供客戶的詳細資訊。
     * 只包含想給外部系統或前端的欄位。
     */

    private Long customerId;
    private String customerName;

    private BCustomerIndustry industry;
    private BCustomerType BCustomerType;
    private BCustomerLevel BCustomerLevel;

    private String customerAddress;
    private String customerTel;
    private String customerEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}