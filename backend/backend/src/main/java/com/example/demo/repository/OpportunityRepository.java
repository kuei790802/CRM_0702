package com.example.demo.repository;

import com.example.demo.entity.Opportunity;
import com.example.demo.enums.OpportunityStage;
import com.example.demo.enums.OpportunityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long>,
        JpaSpecificationExecutor<Opportunity> {

    /**
     * 根據商機名稱進行模糊查詢（不區分大小寫），並支持分頁。
     * Spring Data JPA 會根據方法名自動構建查詢。
     * `Containing` 表示模糊查詢 (SQL 的 LIKE %value%)。
     * `IgnoreCase` 表示不區分大小寫。
     * @param opportunityName 商機名稱的部分字串 (例如輸入 "新產品" 可以找到 "新產品推廣")。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的商機分頁列表。
     */
    Page<Opportunity> findByOpportunityNameContainingIgnoreCase(String opportunityName, Pageable pageable);

    /**
     * 根據商機狀態查詢商機列表，並支持分頁。
     * @param status 商機狀態枚舉 (例如 OpportunityStatus.WON)。
     * @param pageable 分頁和排序資訊。
     * @return 符合指定狀態的商機分頁列表。
     */
    Page<Opportunity> findByStatus(OpportunityStatus status, Pageable pageable);

    /**
     * 根據商機階段查詢商機列表，並支持分頁。
     * @param stage 商機階段枚舉 (例如 OpportunityStage.PROPOSAL)。
     * @param pageable 分頁和排序資訊。
     * @return 符合指定階段的商機分頁列表。
     */
    Page<Opportunity> findByStage(OpportunityStage stage, Pageable pageable);

    /**
     * 根據關聯客戶的 ID 查詢商機列表，並支持分頁。
     * `findByBCustomer_CustomerId` 語法表示遍歷 `Opportunity` 的 `bCustomer` 屬性，
     * 再查詢 `bCustomer` 實體中的 `customerId` 屬性。
     * @param customerId 客戶的唯一識別碼。
     * @param pageable 分頁和排序資訊。
     * @return 屬於指定客戶的商機分頁列表。
     */
    Page<Opportunity> findByBCustomer_CustomerId(Long customerId, Pageable pageable);

    /**
     * 根據關聯聯絡人的 ID 查詢商機列表，並支持分頁。
     * `findByContact_ContactId` 語法表示遍歷 `Opportunity` 的 `contact` 屬性，
     * 再查詢 `contact` 實體中的 `contactId` 屬性。
     * @param contactId 聯絡人的唯一識別碼。
     * @param pageable 分頁和排序資訊。
     * @return 屬於指定聯絡人的商機分頁列表。
     */
    Page<Opportunity> findByContact_ContactId(Long contactId, Pageable pageable);

    /**
     * 查詢預計結束日期在指定日期之前的商機列表，並支持分頁。
     * `Before` 表示查詢日期小於或等於指定日期。
     * @param closeDate 指定的結束日期。
     * @param pageable 分頁和排序資訊。
     * @return 預計結束日期在指定日期之前的商機分頁列表。
     */
    Page<Opportunity> findByCloseDateBefore(LocalDate closeDate, Pageable pageable);
}
