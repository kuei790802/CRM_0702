package com.example.demo.dto.report;

import java.math.BigDecimal;

/**
 * 業務員績效報告條目。
 * 表示單一業務員的銷售相關指標。
 */
public class SalespersonPerformanceDto {
    private String salesPersonName; // 業務員名稱
    private Long totalOpportunities; // 總商機數
    private Long wonOpportunities; // 成交商機數
    private BigDecimal wonAmount; // 成交總金額
    private Long lostOpportunities; // 丟失商機數
    private BigDecimal lostAmount; // 丟失總金額
    private Double winRate; // 成交率 (wonOpportunities / totalOpportunities)

    public SalespersonPerformanceDto() {}

    // --- Getters and Setters ---
    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }

    public Long getTotalOpportunities() {
        return totalOpportunities;
    }

    public void setTotalOpportunities(Long totalOpportunities) {
        this.totalOpportunities = totalOpportunities;
    }

    public Long getWonOpportunities() {
        return wonOpportunities;
    }

    public void setWonOpportunities(Long wonOpportunities) {
        this.wonOpportunities = wonOpportunities;
    }

    public BigDecimal getWonAmount() {
        return wonAmount;
    }

    public void setWonAmount(BigDecimal wonAmount) {
        this.wonAmount = wonAmount;
    }

    public Long getLostOpportunities() {
        return lostOpportunities;
    }

    public void setLostOpportunities(Long lostOpportunities) {
        this.lostOpportunities = lostOpportunities;
    }

    public BigDecimal getLostAmount() {
        return lostAmount;
    }

    public void setLostAmount(BigDecimal lostAmount) {
        this.lostAmount = lostAmount;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }
}
