package com.example.demo.repository;

import com.example.demo.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * 根據聯絡人姓名進行模糊查詢（不區分大小寫），並支持分頁。
     * @param contactName 聯絡人名稱的部分字串。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的聯絡人分頁列表。
     */
    Page<Contact> findByContactNameContainingIgnoreCase(String contactName, Pageable pageable);

    /**
     * 根據聯絡人的電子郵件查詢聯絡人。
     * @param email 聯絡人的電子郵件。
     * @return 包含聯絡人的 Optional，如果找不到則為空。
     */
    Page<Contact> findByEmail(String email, Pageable pageable);

    /**
     * 根據聯絡人的電話號碼查詢聯絡人。
     * @param contactPhone 聯絡人的電話號碼。
     * @return 包含聯絡人的 Optional，如果找不到則為空。
     */
    Page<Contact> findByContactPhone(String contactPhone, Pageable pageable);

    /**
     * 根據聯絡人所屬的客戶 ID 查詢聯絡人列表，並支持分頁。
     * 使用屬性遍歷 (`Customer_CustomerId`) 語法來查詢關聯實體的屬性。
     * Spring Data JPA 將查找 Contact.customer 關聯物件中的 customerId 屬性。
     * @param customerId 客戶的唯一 ID。
     * @param pageable 分頁和排序資訊。
     * @return 屬於指定客戶的聯絡人分頁列表。
     */
    Page<Contact> findByCustomer_CustomerId(Long customerId, Pageable pageable);
}
