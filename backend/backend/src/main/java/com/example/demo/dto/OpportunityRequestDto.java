package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 商機請求資料傳輸物件 (用於請求：建立或更新商機)。
 * 包含客戶端提交的商機資訊。
 */
public class OpportunityRequestDto {
    private Long id; // 對於更新操作，此欄位會被設定

    @NotNull(message = "客戶ID不能為空")
    private Long customerId;

    @NotBlank(message = "商機階段不能為空")
    private String stage;

    private String status;

    private String description;

    private LocalDate closeDate;

    @NotNull(message = "商機金額不能為空")
    @PositiveOrZero(message = "商機金額必須為正數或零")
    @DecimalMin(value = "0.00", inclusive = true, message = "商機金額不能為負數")
    private BigDecimal amount;

    private String salesPersonName;

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() { // 新增 Getter
        return description;
    }

    public void setDescription(String description) { // 新增 Setter
        this.description = description;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }
}
