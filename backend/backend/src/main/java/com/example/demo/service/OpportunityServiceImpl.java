package com.example.demo.service;

import com.example.demo.dto.OpportunityRequestDto;
import com.example.demo.dto.OpportunityResponseDto;
import com.example.demo.dto.report.RevenueForecastEntryDto;
import com.example.demo.dto.report.SalesFunnelReportEntryDto;
import com.example.demo.dto.report.SalespersonPerformanceDto;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Opportunity;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.OpportunityNotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OpportunityRepository;
import com.example.demo.service.OpportunityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final CustomerRepository customerRepository;

    public OpportunityServiceImpl(OpportunityRepository opportunityRepository, CustomerRepository customerRepository) {
        this.opportunityRepository = opportunityRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * 將商機實體 (Opportunity) 轉換為商機回應 DTO (OpportunityResponseDto)。
     *
     * @param opportunity 商機實體
     * @return 商機回應 DTO
     */
    private OpportunityResponseDto convertToDto(Opportunity opportunity) {
        OpportunityResponseDto dto = new OpportunityResponseDto();
        dto.setId(opportunity.getId());
        dto.setCustomerId(opportunity.getCustomerId());

        if (opportunity.getCustomer() != null) {
            dto.setCustomerName(opportunity.getCustomer().getName());
        }
        dto.setStage(opportunity.getStage());
        dto.setStatus(opportunity.getStatus());
        dto.setDescription(opportunity.getDescription());
        dto.setCloseDate(opportunity.getCloseDate());
        dto.setAmount(opportunity.getAmount());
        dto.setSalesPersonName(opportunity.getSalesPersonName());
        dto.setCreatedAt(opportunity.getCreatedAt());
        dto.setUpdatedAt(opportunity.getUpdatedAt());
        return dto;
    }

    /**
     * 將商機請求 DTO (OpportunityRequestDto) 的資料複製到商機實體 (Opportunity) 中。
     * 此方法用於建立新實體或更新現有實體。
     *
     * @param opportunityRequestDto 商機請求 DTO
     * @param opportunity           要更新的商機實體
     * @throws CustomerNotFoundException 如果指定的客戶 ID 不存在
     */
    private void updateOpportunityFromDto(OpportunityRequestDto opportunityRequestDto, Opportunity opportunity) {
        opportunity.setStage(opportunityRequestDto.getStage());
        opportunity.setStatus(opportunityRequestDto.getStatus());
        opportunity.setDescription(opportunityRequestDto.getDescription());
        opportunity.setCloseDate(opportunityRequestDto.getCloseDate());
        opportunity.setAmount(opportunityRequestDto.getAmount());
        opportunity.setSalesPersonName(opportunityRequestDto.getSalesPersonName());

        // 處理客戶關聯
        if (opportunityRequestDto.getCustomerId() != null) {
            // 如果實體中的 customerId 與 DTO 中的不同，或者實體的 customer 物件為空，則重新查找並設定
            if (!Objects.equals(opportunity.getCustomerId(), opportunityRequestDto.getCustomerId()) || opportunity.getCustomer() == null) {
                Customer customer = customerRepository.findById(opportunityRequestDto.getCustomerId())
                        .orElseThrow(() -> new CustomerNotFoundException("客戶 ID: " + opportunityRequestDto.getCustomerId() + " 不存在。"));
                opportunity.setCustomer(customer);
            }
        } else {
            opportunity.setCustomer(null);
        }
    }


    /**
     * 獲取所有商機的回應 DTO 列表。
     *
     * @return 商機回應 DTO 列表
     */
    @Override
    public List<OpportunityResponseDto> findAll() {
        return opportunityRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 根據 ID 獲取單一商機的回應 DTO。
     *
     * @param id 商機 ID
     * @return 匹配的商機回應 DTO
     * @throws OpportunityNotFoundException 如果找不到該商機
     */
    @Override
    public OpportunityResponseDto findById(Long id) throws OpportunityNotFoundException {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new OpportunityNotFoundException("商機 ID: " + id + " 不存在"));
        return convertToDto(opportunity);
    }

    /**
     * 儲存或更新商機。
     * 如果 DTO 中包含 ID，則執行更新；否則執行新增。
     *
     * @param opportunityRequestDto 要儲存的商機請求 DTO
     * @return 儲存後的商機回應 DTO
     * @throws CustomerNotFoundException 如果指定的客戶 ID 不存在
     * @throws OpportunityNotFoundException 如果要更新的商機不存在 (當 DTO 包含 ID 但找不到商機時)
     */
    @Override
    @Transactional
    public OpportunityResponseDto save(OpportunityRequestDto opportunityRequestDto) throws CustomerNotFoundException, OpportunityNotFoundException {
        Opportunity opportunity;
        if (opportunityRequestDto.getId() != null) {
            // 更新現有商機
            opportunity = opportunityRepository.findById(opportunityRequestDto.getId())
                    .orElseThrow(() -> new OpportunityNotFoundException("欲更新的商機 ID: " + opportunityRequestDto.getId() + " 不存在"));
            updateOpportunityFromDto(opportunityRequestDto, opportunity);
        } else {
            // 建立新商機
            opportunity = new Opportunity();
            updateOpportunityFromDto(opportunityRequestDto, opportunity);
        }
        Opportunity savedOpportunity = opportunityRepository.save(opportunity);
        return convertToDto(savedOpportunity);
    }

    /**
     * 根據 ID 刪除商機。
     *
     * @param id 要刪除的商機 ID
     * @throws OpportunityNotFoundException 如果找不到要刪除的商機
     */
    @Override
    @Transactional
    public void delete(Long id) throws OpportunityNotFoundException {
        if (!opportunityRepository.existsById(id)) {
            throw new OpportunityNotFoundException("欲刪除的商機 ID: " + id + " 不存在。");
        }
        opportunityRepository.deleteById(id);
    }

    /**
     * 根據客戶 ID 獲取該客戶下的所有商機的回應 DTO 列表。
     *
     * @param customerId 客戶 ID
     * @return 屬於該客戶的商機回應 DTO 列表
     * @throws CustomerNotFoundException 如果客戶不存在
     */
    @Override
    public List<OpportunityResponseDto> findOpportunitiesByCustomerId(Long customerId) throws CustomerNotFoundException {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("客戶 ID: " + customerId + " 不存在"));

        return opportunityRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 根據客戶 ID 獲取該客戶下的所有商機的回應 DTO 分頁結果，支援分頁和排序。
     *
     * @param customerId 客戶 ID
     * @param pageable   分頁和排序資訊
     * @return 屬於該客戶的商機回應 DTO 分頁結果
     * @throws CustomerNotFoundException 如果客戶不存在
     */
    @Override
    public Page<OpportunityResponseDto> findOpportunitiesByCustomerId(Long customerId, Pageable pageable) throws CustomerNotFoundException {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("客戶 ID: " + customerId + " 不存在"));

        Page<Opportunity> opportunitiesPage = opportunityRepository.findByCustomerId(customerId, pageable);
        List<OpportunityResponseDto> dtoList = opportunitiesPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, opportunitiesPage.getTotalElements());
    }

    /**
     * 獲取銷售漏斗報告數據。
     * 根據商機階段聚合商機數量和總金額。
     *
     * @return 銷售漏斗報告條目列表
     */
    @Override
    public List<SalesFunnelReportEntryDto> getSalesFunnelReport() {
        List<Opportunity> allOpportunities = opportunityRepository.findAll();

        // 按階段分組，計算數量和總金額
        Map<String, Map<String, Object>> groupedByStage = allOpportunities.stream()
                .collect(Collectors.groupingBy(
                        Opportunity::getStage,
                        Collectors.teeing(
                                Collectors.counting(), // 獲取數量
                                Collectors.reducing(BigDecimal.ZERO, Opportunity::getAmount, BigDecimal::add), // 獲取總金額
                                (count, sum) -> {
                                    Map<String, Object> result = Map.of("count", count, "amount", sum);
                                    return result;
                                }
                        )
                ));

        // 轉換為 DTO 列表
        return groupedByStage.entrySet().stream()
                .map(entry -> new SalesFunnelReportEntryDto(
                        entry.getKey(),
                        (Long) entry.getValue().get("count"),
                        (BigDecimal) entry.getValue().get("amount")
                ))
                .sorted(Comparator.comparing(SalesFunnelReportEntryDto::getStageName)) // 可以選擇按階段名稱排序
                .collect(Collectors.toList());
    }

    /**
     * 獲取業務員績效報告數據。
     * 計算每個業務員的總商機數、贏單數、贏單金額、丟單數、丟單金額及贏率。
     *
     * @return 業務員績效報告 DTO 列表
     */
    @Override
    public List<SalespersonPerformanceDto> getSalespersonPerformanceReport() {
        List<Opportunity> allOpportunities = opportunityRepository.findAll();

        Map<String, List<Opportunity>> opportunitiesBySalesperson = allOpportunities.stream()
                .collect(Collectors.groupingBy(Opportunity::getSalesPersonName));

        return opportunitiesBySalesperson.entrySet().stream()
                .map(entry -> {
                    String salesPersonName = entry.getKey();
                    List<Opportunity> salespersonOpportunities = entry.getValue();

                    Long totalOpportunities = (long) salespersonOpportunities.size();
                    Long wonOpportunities = salespersonOpportunities.stream()
                            .filter(opp -> "Closed Won".equalsIgnoreCase(opp.getStatus())) // 假設 "Closed Won" 表示贏單
                            .count();
                    BigDecimal wonAmount = salespersonOpportunities.stream()
                            .filter(opp -> "Closed Won".equalsIgnoreCase(opp.getStatus()))
                            .map(Opportunity::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    Long lostOpportunities = salespersonOpportunities.stream()
                            .filter(opp -> "Closed Lost".equalsIgnoreCase(opp.getStatus())) // 假設 "Closed Lost" 表示丟單
                            .count();
                    BigDecimal lostAmount = salespersonOpportunities.stream()
                            .filter(opp -> "Closed Lost".equalsIgnoreCase(opp.getStatus()))
                            .map(Opportunity::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    Double winRate = (totalOpportunities > 0) ?
                            (double) wonOpportunities / totalOpportunities * 100 : 0.0;

                    SalespersonPerformanceDto dto = new SalespersonPerformanceDto();
                    dto.setSalesPersonName(salesPersonName);
                    dto.setTotalOpportunities(totalOpportunities);
                    dto.setWonOpportunities(wonOpportunities);
                    dto.setWonAmount(wonAmount);
                    dto.setLostOpportunities(lostOpportunities);
                    dto.setLostAmount(lostAmount);
                    dto.setWinRate(winRate);
                    return dto;
                })
                .sorted(Comparator.comparing(SalespersonPerformanceDto::getSalesPersonName)) // 按業務員名稱排序
                .collect(Collectors.toList());
    }

    /**
     * 獲取營收預測報告數據。
     * 根據預計結案日期按月聚合商機金額。
     *
     * @param startDate 預測開始日期
     * @param endDate   預測結束日期
     * @return 營收預測報告條目列表
     */
    @Override
    public List<RevenueForecastEntryDto> getRevenueForecastReport(LocalDate startDate, LocalDate endDate) {
        List<Opportunity> opportunities = opportunityRepository.findAll();

        // 過濾掉已結束 (贏單/丟單) 的商機，或只包含特定狀態的商機 (例如: 僅開放中或談判中的商機)
        // 只包含還未是「Closed Won」或「Closed Lost」的商機，且 closeDate 在預測範圍內
        List<Opportunity> relevantOpportunities = opportunities.stream()
                .filter(opp -> !"Closed Won".equalsIgnoreCase(opp.getStatus()) && !"Closed Lost".equalsIgnoreCase(opp.getStatus()))
                .filter(opp -> opp.getCloseDate() != null) // 確保 closeDate 不為 null
                .filter(opp -> (startDate == null || !opp.getCloseDate().isBefore(startDate))) // closeDate 晚於或等於開始日期
                .filter(opp -> (endDate == null || !opp.getCloseDate().isAfter(endDate))) // closeDate 早於或等於結束日期
                .collect(Collectors.toList());

        // 按年月分組並匯總金額
        Map<String, BigDecimal> forecastedAmountsByMonth = relevantOpportunities.stream()
                .collect(Collectors.groupingBy(
                        opp -> opp.getCloseDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), // 按年月格式分組
                        Collectors.reducing(BigDecimal.ZERO, Opportunity::getAmount, BigDecimal::add)
                ));

        return forecastedAmountsByMonth.entrySet().stream()
                .map(entry -> new RevenueForecastEntryDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(RevenueForecastEntryDto::getPeriod)) // 按時間段排序
                .collect(Collectors.toList());
    }
}
