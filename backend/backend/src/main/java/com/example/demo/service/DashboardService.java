package com.example.demo.service;


import com.example.demo.dto.erp.dashboard.*;
import com.example.demo.enums.SalesOrderStatus;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.SalesOrderDetailRepository;
import com.example.demo.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderDetailRepository salesOrderDetailRepository;
    private final InventoryRepository inventoryRepository;

    public ErpDashboardDTO getErpDashboardSummary() {
        ErpDashboardDTO dashboard = new ErpDashboardDTO();

        dashboard.setKpis(getKpis());
        dashboard.setSalesTrend(getSalesTrendForLast7Days());
        dashboard.setTopSellingProducts(getTopSellingProducts());
        dashboard.setLowStockProducts(getLowStockProducts());

        return dashboard;
    }

    private KpiData getKpis() {
        KpiData kpis = new KpiData();
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        // 查詢本月銷售總額 (僅計算已出貨訂單)
        BigDecimal monthlySales = salesOrderRepository.findDailySalesTotalBetween(startOfMonth, endOfMonth)
                .stream()
                .map(result -> (BigDecimal) result[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        kpis.setMonthlySalesTotal(monthlySales);

        // 查詢待處理訂單 (狀態為 CONFIRMED)
        Long pendingOrders = salesOrderRepository.countByOrderStatus(SalesOrderStatus.CONFIRMED);
        kpis.setPendingSalesOrders(pendingOrders);
        kpis.setPendingShipments(pendingOrders);

        // 查詢低庫存商品數量
        kpis.setProductsBelowSafetyStock(inventoryRepository.countLowStockInventories());

        return kpis;
    }

    private ChartData getSalesTrendForLast7Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        List<Object[]> results = salesOrderRepository.findDailySalesTotalBetween(startDate, endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");

        List<String> labels = startDate.datesUntil(endDate.plusDays(1)).map(formatter::format).collect(Collectors.toList());
        List<BigDecimal> values = new ArrayList<>(Collections.nCopies(labels.size(), BigDecimal.ZERO));

        results.forEach(result -> {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            BigDecimal total = (BigDecimal) result[1];
            int index = (int) startDate.until(date, java.time.temporal.ChronoUnit.DAYS);
            if (index >= 0 && index < values.size()) {
                values.set(index, total);
            }
        });

        return new ChartData(labels, values);
    }

    private List<ProductRankDTO> getTopSellingProducts() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29); // 最近 30 天
        return salesOrderDetailRepository.findTopSellingProducts(startDate, endDate, PageRequest.of(0, 5)); // 取前 5 名
    }

    private List<LowStockProductDTO> getLowStockProducts() {
        return inventoryRepository.findLowStockInventories().stream()
                .map(inventory -> {
                    LowStockProductDTO dto = new LowStockProductDTO();
                    dto.setProductId(inventory.getProduct().getProductId());
                    dto.setProductCode(inventory.getProduct().getProductCode());
                    dto.setProductName(inventory.getProduct().getName());
                    dto.setWarehouseId(inventory.getWarehouse().getWarehouseId());
                    dto.setWarehouseName(inventory.getWarehouse().getName());
                    dto.setCurrentStock(inventory.getCurrentStock());
                    dto.setSafetyStockQuantity(new BigDecimal(inventory.getProduct().getSafetyStockQuantity()));
                    return dto;
                }).collect(Collectors.toList());
    }
}