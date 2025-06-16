package com.example.demo.dto.response;

import com.example.demo.enums.CustomerIndustry;
import com.example.demo.enums.CustomerLevel;
import com.example.demo.enums.CustomerType;

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
public class CustomerDto {
    /**
     * 用於 API 回應，提供客戶的詳細資訊。
     * 只包含想給外部系統或前端的欄位。
     */

    private Long customerId;
    private String customerName;

    private CustomerIndustry industry;
    private CustomerType customerType;
    private CustomerLevel customerLevel;

    private String customerAddress;
    private String customerTel;
    private String customerEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}