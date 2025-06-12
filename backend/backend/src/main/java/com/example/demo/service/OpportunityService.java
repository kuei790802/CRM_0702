package com.example.demo.service;

import com.example.demo.dto.OpportunityRequestDto;
import com.example.demo.dto.OpportunityResponseDto;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.OpportunityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商機服務介面。
 * 定義了商機相關的業務操作。
 */
public interface OpportunityService {

    /**
     * 獲取所有商機的回應 DTO 列表。
     *
     * @return 商機回應 DTO 列表
     */
    List<OpportunityResponseDto> findAll();

    /**
     * 根據 ID 獲取單一商機的回應 DTO。
     *
     * @param id 商機 ID
     * @return 匹配的商機回應 DTO
     * @throws OpportunityNotFoundException 如果找不到該商機
     */
    OpportunityResponseDto findById(Long id) throws OpportunityNotFoundException;

    /**
     * 儲存或更新商機。
     * 如果 DTO 中包含 ID，則執行更新；否則執行新增。
     *
     * @param opportunityRequestDto 要儲存的商機請求 DTO
     * @return 儲存後的商機回應 DTO
     * @throws CustomerNotFoundException 如果指定的客戶 ID 不存在
     * @throws OpportunityNotFoundException 如果要更新的商機不存在 (當 DTO 包含 ID 但找不到商機時)
     */
    OpportunityResponseDto save(OpportunityRequestDto opportunityRequestDto) throws CustomerNotFoundException, OpportunityNotFoundException;

    /**
     * 根據 ID 刪除商機。
     *
     * @param id 要刪除的商機 ID
     * @throws OpportunityNotFoundException 如果找不到要刪除的商機
     */
    void delete(Long id) throws OpportunityNotFoundException;

    /**
     * 根據客戶 ID 獲取該客戶下的所有商機的回應 DTO 列表。
     *
     * @param customerId 客戶 ID
     * @return 屬於該客戶的商機回應 DTO 列表
     * @throws CustomerNotFoundException 如果客戶不存在
     */
    List<OpportunityResponseDto> findOpportunitiesByCustomerId(Long customerId) throws CustomerNotFoundException;

    /**
     * 根據客戶 ID 獲取該客戶下的所有商機的回應 DTO 分頁結果，支援分頁和排序。
     *
     * @param customerId 客戶 ID
     * @param pageable   分頁和排序資訊
     * @return 屬於該客戶的商機回應 DTO 分頁結果
     * @throws CustomerNotFoundException 如果客戶不存在
     */
    Page<OpportunityResponseDto> findOpportunitiesByCustomerId(Long customerId, Pageable pageable) throws CustomerNotFoundException;
}
