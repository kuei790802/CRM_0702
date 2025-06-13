package com.example.demo.dto.report;

import java.math.BigDecimal;

/**
 * 營收預測報告中的單一時間段條目。
 * 表示某個時間段的預計總金額。
 */
public class RevenueForecastEntryDto {
    private String period; // 時間段 (例如: "2025-06", "2025 Q3")
    private BigDecimal forecastedAmount; // 預計總金額

    public RevenueForecastEntryDto(String period, BigDecimal forecastedAmount) {
        this.period = period;
        this.forecastedAmount = forecastedAmount;
    }

    // --- Getters and Setters ---
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getForecastedAmount() {
        return forecastedAmount;
    }

    public void setForecastedAmount(BigDecimal forecastedAmount) {
        this.forecastedAmount = forecastedAmount;
    }
}

