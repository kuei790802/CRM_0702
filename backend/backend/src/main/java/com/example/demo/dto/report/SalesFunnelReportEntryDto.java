package com.example.demo.dto.report;

import java.math.BigDecimal;

/**
 * 銷售漏斗報告中的單一階段條目。
 * 表示某個階段的商機數量和總金額。
 */
public class SalesFunnelReportEntryDto {
    private String stageName; // 銷售階段名稱
    private Long opportunityCount; // 該階段的商機數量
    private BigDecimal totalAmount; // 該階段的商機總金額

    public SalesFunnelReportEntryDto(String stageName, Long opportunityCount, BigDecimal totalAmount) {
        this.stageName = stageName;
        this.opportunityCount = opportunityCount;
        this.totalAmount = totalAmount;
    }

    // --- Getters and Setters ---
    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Long getOpportunityCount() {
        return opportunityCount;
    }

    public void setOpportunityCount(Long opportunityCount) {
        this.opportunityCount = opportunityCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
