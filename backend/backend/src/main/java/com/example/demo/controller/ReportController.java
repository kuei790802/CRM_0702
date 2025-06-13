package com.example.demo.controller;

import com.example.demo.dto.report.RevenueForecastEntryDto;
import com.example.demo.dto.report.SalesFunnelReportDto;
import com.example.demo.dto.report.SalesFunnelReportEntryDto;
import com.example.demo.dto.report.SalespersonPerformanceDto;
import com.example.demo.service.OpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * 負責提供各種銷售和業務報告的數據。
 */
@RestController
@RequestMapping("/api/reports") //
public class ReportController {

    private final OpportunityService opportunityService;


    public ReportController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    /**
     * 獲取銷售漏斗報告。
     * GET /api/reports/sales-funnel
     * @return 銷售漏斗報告 DTO，HTTP 狀態碼 200 OK
     */
    @GetMapping("/sales-funnel")
    public ResponseEntity<SalesFunnelReportDto> getSalesFunnelReport() {
        List<SalesFunnelReportEntryDto> funnelData = opportunityService.getSalesFunnelReport();
        return ResponseEntity.ok(new SalesFunnelReportDto(funnelData));
    }

    /**
     * 獲取業務員績效報告。
     * GET /api/reports/salesperson-performance
     * @return 業務員績效報告 DTO 列表，HTTP 狀態碼 200 OK
     */
    @GetMapping("/salesperson-performance")
    public ResponseEntity<List<SalespersonPerformanceDto>> getSalespersonPerformanceReport() {
        List<SalespersonPerformanceDto> performanceData = opportunityService.getSalespersonPerformanceReport();
        return ResponseEntity.ok(performanceData);
    }

    /**
     * 獲取營收預測報告。
     * GET /api/reports/revenue-forecast
     * 可選的查詢參數：startDate, endDate (格式 YYYY-MM-DD)
     * @param startDate 預測開始日期
     * @param endDate   預測結束日期
     * @return 營收預測報告條目列表，HTTP 狀態碼 200 OK
     */
    @GetMapping("/revenue-forecast")
    public ResponseEntity<List<RevenueForecastEntryDto>> getRevenueForecastReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<RevenueForecastEntryDto> forecastData = opportunityService.getRevenueForecastReport(startDate, endDate);
        return ResponseEntity.ok(forecastData);
    }
}
