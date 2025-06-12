package com.example.demo.dto;

// 返回給前端的聯絡人資料格式
public class ContactResponse {
    private Long id;
    private Long customerId;
    private String customerName; // 所屬客戶的名稱
    private String name;
    private String title;
    private String phone;
    private String email;
    private String notes;


    public ContactResponse(Long id, Long customerId, String customerName, String name, String title, String phone, String email, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.name = name;
        this.title = title;
        this.phone = phone;
        this.email = email;
        this.notes = notes;
    }

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getNotes() {
        return notes;
    }

}
