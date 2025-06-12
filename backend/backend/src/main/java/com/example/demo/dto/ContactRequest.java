package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// 用於接收前端建立或更新聯絡人的請求
public class ContactRequest {

    @NotNull(message = "客戶 ID 必填") // 聯絡人必須屬於一個客戶
    private Long customerId;

    @NotBlank(message = "聯絡人姓名必填") // 姓名不可為空或空白
    private String name;

    private String title;
    private String phone;

    @Email(message = "電子郵件格式不正確") // 驗證郵件格式
    private String email;

    private String notes;

    public ContactRequest() {}

    // --- Getters and Setters ---
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}