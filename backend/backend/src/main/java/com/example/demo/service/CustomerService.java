package com.example.demo.service;

import com.example.demo.entity.Customer;
import java.util.List;

public interface CustomerService {

    /**
     * 獲取所有客戶。
     * @return 客戶列表
     */
    List<Customer> findAll();

    /**
     * 根據 ID 獲取客戶。
     * @param id 客戶 ID
     * @return 匹配的客戶實體，如果找不到則返回 null
     */
    Customer findById(Long id);

    /**
     * 儲存或更新客戶。
     * 如果客戶 ID 存在且客戶存在，則為更新；否則為建立。
     * @param customer 要儲存的客戶實體
     * @return 儲存後的客戶實體
     */
    Customer save(Customer customer);

    /**
     * 根據 ID 刪除客戶。
     * @param id 要刪除的客戶 ID
     */
    void delete(Long id);

    /**
     * 為指定客戶新增一個標籤。
     * @param customerId 客戶 ID
     * @param tagId 標籤 ID
     * @throws jakarta.persistence.EntityNotFoundException 如果客戶或標籤不存在
     */
    void addTag(Long customerId, Long tagId);

    /**
     * 從指定客戶移除一個標籤。
     * @param customerId 客戶 ID
     * @param tagId 標籤 ID
     * @throws jakarta.persistence.EntityNotFoundException 如果客戶或標籤不存在
     */
    void removeTag(Long customerId, Long tagId);
}
