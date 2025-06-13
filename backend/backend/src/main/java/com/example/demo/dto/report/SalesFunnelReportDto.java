package com.example.demo.dto.report;

import java.util.List;

/**
 * 完整的銷售漏斗報告。
 * 包含所有銷售階段的數據列表。
 */
public class SalesFunnelReportDto {
    private List<SalesFunnelReportEntryDto> funnelStages;

    public SalesFunnelReportDto(List<SalesFunnelReportEntryDto> funnelStages) {
        this.funnelStages = funnelStages;
    }

    // --- Getter and Setter ---
    public List<SalesFunnelReportEntryDto> getFunnelStages() {
        return funnelStages;
    }

    public void setFunnelStages(List<SalesFunnelReportEntryDto> funnelStages) {
        this.funnelStages = funnelStages;
    }
}
