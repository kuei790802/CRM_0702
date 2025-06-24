package com.example.demo.service.impl;

import com.example.demo.dto.response.dashboard.ChartDataDto;
import com.example.demo.dto.response.dashboard.DashboardDto;
import com.example.demo.dto.response.dashboard.KpiDto;
import com.example.demo.dto.response.dashboard.NameValueDto;
import com.example.demo.enums.OpportunityStage;
import com.example.demo.enums.OpportunityStatus;
import com.example.demo.repository.OpportunityRepository;
import com.example.demo.repository.SalesFunnelRepository;
import com.example.demo.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final OpportunityRepository opportunityRepository;

    public DashboardServiceImpl(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    public DashboardDto getDashboardData() {
        DashboardDto dashboardDto = new DashboardDto();

        // 1. 取得 KPI 數據
        dashboardDto.setKpis(getKpiData());

        // 2. 取得商機階段分佈 (圓餅圖)
        List<Object[]> stageResults = opportunityRepository.countOpenOpportunitiesByStage();
        List<NameValueDto> stageData = stageResults.stream()
                .map(result -> new NameValueDto(((OpportunityStage) result[0]).name(), (Long) result[1]))
                .collect(Collectors.toList());
        dashboardDto.setStageDistribution(new ChartDataDto<>("活躍商機階段分佈", stageData));

        // 3. 取得過去6個月每月新增商機趨勢 (長條圖)
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        List<Object[]> trendResults = opportunityRepository.countNewOpportunitiesByMonth(sixMonthsAgo);
        List<NameValueDto> trendData = trendResults.stream()
                .map(result -> new NameValueDto((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
        dashboardDto.setMonthlyTrend(new ChartDataDto<>("每月新增商機趨勢", trendData));

        return dashboardDto;
    }

    private List<KpiDto> getKpiData() {
        List<KpiDto> kpis = new ArrayList<>();

        long totalOpportunities = opportunityRepository.count();
        long wonOpportunities = opportunityRepository.countByStatus(OpportunityStatus.WON);

        BigDecimal conversionRate = BigDecimal.ZERO;
        if (totalOpportunities > 0) {
            conversionRate = BigDecimal.valueOf(wonOpportunities)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalOpportunities), 2, RoundingMode.HALF_UP);
        }

        kpis.add(new KpiDto("總商機數", BigDecimal.valueOf(totalOpportunities), "筆"));
        kpis.add(new KpiDto("轉換率", conversionRate, "%"));

        return kpis;
    }
}
