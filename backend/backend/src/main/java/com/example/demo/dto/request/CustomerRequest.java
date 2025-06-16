package com.example.demo.dto.request;

import com.example.demo.enums.CustomerIndustry;
import com.example.demo.enums.CustomerLevel;
import com.example.demo.enums.CustomerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerRequest {

    /**
     * 用於接收「建立」或「更新」客戶時的請求資料。
     * 只包含允許客戶端修改的欄位。
     */

    @NotBlank(message = "客戶名稱不能為空")
    private String customerName;

    private CustomerIndustry industry;
    private CustomerType customerType;
    private CustomerLevel customerLevel;

    private String customerAddress;
    private String customerTel;

    @Email(message = "客戶電子郵件格式不正確")
    private String customerEmail;



}
