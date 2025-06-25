package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ErpDashboardDTO {
    private KpiData kpis;
    private ChartData salesTrend;
    private List<ProductRankDTO> topSellingProducts;
    private List<LowStockProductDTO> lowStockProducts;
}
