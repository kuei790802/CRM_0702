package com.example.demo.repository;

import com.example.demo.entity.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 商機資料庫存取介面。
 * 繼承 JpaRepository 提供基本的 CRUD 操作，並定義自定義查詢方法。
 */
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    /**
     * 根據客戶 ID 查找所有商機。
     *
     * @param customerId 客戶 ID
     * @return 屬於該客戶的商機列表
     */
    List<Opportunity> findByCustomerId(Long customerId);

    /**
     * 根據客戶 ID 查找所有商機，並支援分頁和排序。
     *
     * @param customerId 客戶 ID
     * @param pageable   分頁和排序資訊
     * @return 屬於該客戶的商機分頁結果
     */
    Page<Opportunity> findByCustomerId(Long customerId, Pageable pageable);
}
