package com.example.demo.controller;

import com.example.demo.dto.OpportunityRequestDto;
import com.example.demo.dto.OpportunityResponseDto;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.OpportunityNotFoundException;
import com.example.demo.service.OpportunityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @GetMapping
    public ResponseEntity<List<OpportunityResponseDto>> getAll() {
        List<OpportunityResponseDto> opportunities = opportunityService.findAll();
        return ResponseEntity.ok(opportunities);
    }

    /**
     * 根據 ID 獲取單一商機。
     * GET /api/opportunities/{id}
     *
     * @param id 商機 ID
     * @return 匹配的商機回應 DTO，HTTP 狀態碼 200 OK
     * @throws OpportunityNotFoundException 如果找不到該商機，由 @ResponseStatus 處理並返回 404 NOT FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<OpportunityResponseDto> getById(@PathVariable Long id) {
        OpportunityResponseDto opportunity = opportunityService.findById(id);
        return ResponseEntity.ok(opportunity);
    }

    /**
     * 建立新商機。
     * POST /api/opportunities
     *
     * @param opportunityRequestDto 包含新商機資料的 DTO (會自動進行驗證)
     * @return 建立後的商機回應 DTO，HTTP 狀態碼 201 CREATED
     * @throws CustomerNotFoundException 如果指定的客戶 ID 不存在
     */
    @PostMapping
    public ResponseEntity<OpportunityResponseDto> create(@Valid @RequestBody OpportunityRequestDto opportunityRequestDto) {
        OpportunityResponseDto savedOpportunity = opportunityService.save(opportunityRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOpportunity);
    }

    /**
     * 根據 ID 更新現有商機。
     * PUT /api/opportunities/{id}
     *
     * @param id             要更新的商機 ID
     * @param opportunityRequestDto 包含更新資料的 DTO (會自動進行驗證)
     * @return 更新後的商機回應 DTO，HTTP 狀態碼 200 OK
     * @throws OpportunityNotFoundException 如果找不到要更新的商機
     * @throws CustomerNotFoundException    如果指定的客戶 ID 不存在
     */
    @PutMapping("/{id}")
    public ResponseEntity<OpportunityResponseDto> update(@PathVariable Long id, @Valid @RequestBody OpportunityRequestDto opportunityRequestDto) {
        // 設定 DTO 的 ID，確保 Service 層知道是更新操作
        opportunityRequestDto.setId(id);
        OpportunityResponseDto updatedOpportunity = opportunityService.save(opportunityRequestDto);
        return ResponseEntity.ok(updatedOpportunity);
    }

    /**
     * 根據 ID 刪除商機。
     * DELETE /api/opportunities/{id}
     *
     * @param id 要刪除的商機 ID
     * @return 無內容回應，HTTP 狀態碼 204 NO CONTENT
     * @throws OpportunityNotFoundException 如果找不到要刪除的商機
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        opportunityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根據客戶 ID 獲取該客戶下的所有商機。
     * GET /api/opportunities/customer/{customerId}
     *
     * @param customerId 客戶 ID
     * @return 屬於該客戶的商機回應 DTO 列表，HTTP 狀態碼 200 OK
     * @throws CustomerNotFoundException 如果客戶不存在
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OpportunityResponseDto>> getOpportunitiesByCustomerId(@PathVariable Long customerId) {
        List<OpportunityResponseDto> opportunities = opportunityService.findOpportunitiesByCustomerId(customerId);
        return ResponseEntity.ok(opportunities);
    }

    /**
     * 根據客戶 ID 獲取該客戶下的所有商機，支援分頁和排序。
     * GET /api/opportunities/customer/{customerId}/paged
     *
     * @param customerId 客戶 ID
     * @param pageable   分頁和排序資訊 (例如: ?page=0&size=10&sort=closeDate,desc)
     * @return 屬於該客戶的商機分頁結果，HTTP 狀態碼 200 OK
     * @throws CustomerNotFoundException 如果客戶不存在
     */
    @GetMapping("/customer/{customerId}/paged")
    public ResponseEntity<Page<OpportunityResponseDto>> getPagedOpportunitiesByCustomerId(
            @PathVariable Long customerId,
            Pageable pageable) {
        Page<OpportunityResponseDto> opportunitiesPage = opportunityService.findOpportunitiesByCustomerId(customerId, pageable);
        return ResponseEntity.ok(opportunitiesPage);
    }
}
