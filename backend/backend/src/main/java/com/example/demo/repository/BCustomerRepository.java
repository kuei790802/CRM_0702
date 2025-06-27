package com.example.demo.repository;

import com.example.demo.entity.BCustomer;
import com.example.demo.enums.BCustomerIndustry;
import com.example.demo.enums.BCustomerLevel;
import com.example.demo.enums.BCustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BCustomerRepository extends JpaRepository<BCustomer, Long> {

    /**
     * 根據客戶名稱進行模糊查詢（不區分大小寫），並支持分頁。
     * Spring Data JPA 會自動為此方法生成查詢。
     * @param customerName 客戶名稱的部分字串。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶分頁列表。
     */
    Page<BCustomer> findByNameContainingIgnoreCase(String customerName, Pageable pageable);

    /**
     * 根據行業查詢客戶，參數變為 Enum 類型。
     * @param industry 行業名稱 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶分頁列表。
     */
    Page<BCustomer> findByIndustry(BCustomerIndustry industry, Pageable pageable);

    /**
     * 根據客戶類型查詢客戶，參數變為 Enum 類型。
     * @param BCustomerType 客戶類型 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶分頁列表。
     */
    Page<BCustomer> findByBCustomerType(BCustomerType BCustomerType, Pageable pageable);

    /**
     * 根據客戶等級查詢客戶，參數變為 Enum 類型。
     * @param BCustomerLevel 客戶等級 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶分頁列表。
     */
    Page<BCustomer> findByBCustomerLevel(BCustomerLevel BCustomerLevel, Pageable pageable);


}
