package com.example.demo.service;

import com.example.demo.dto.request.CreateOrUpdateCustomerRequest;
import com.example.demo.dto.request.UpdateTagsRequest;
import com.example.demo.dto.response.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerService {

    /**
     * 以分頁的方式獲取所有客戶。
     * @param pageable 包含分頁和排序資訊的請求物件。
     * @return 包含客戶 DTO 列表和分頁資訊的 Page 物件。
     */
    Page<CustomerDTO> findAll(Pageable pageable);

    /**
     * 根據客戶的唯一 ID 尋找客戶。
     * @param id 要尋找的客戶 ID。
     * @return 包含客戶詳細資訊的 DTO。
     * @throws jakarta.persistence.EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    CustomerDTO findById(Long id);

    /**
     * 根據請求資料建立一個新的客戶。
     * @param request 包含新客戶所有必要資訊的請求 DTO。
     * @return 建立成功後，包含新客戶完整資訊（含 ID）的 DTO。
     */
    CustomerDTO create(CreateOrUpdateCustomerRequest request);

    /**
     * 根據 ID 更新一個已存在的客戶資訊。
     * @param id 要更新的客戶 ID。
     * @param request 包含要更新欄位的請求 DTO。
     * @return 更新成功後，包含最新客戶資訊的 DTO。
     * @throws jakarta.persistence.EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    CustomerDTO update(Long id, CreateOrUpdateCustomerRequest request);

    /**
     * 根據 ID 刪除一個客戶。
     * @param id 要刪除的客戶 ID。
     * @throws jakarta.persistence.EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    void delete(Long id);

    /**
     * 為指定的客戶新增一個標籤。
     * @param customerId 客戶的 ID。
     * @param tagId 要新增的標籤 ID。
     * @return 操作完成後，包含最新標籤資訊的客戶 DTO。
     * @throws jakarta.persistence.EntityNotFoundException 如果客戶或標籤不存在。
     */
    CustomerDTO addTag(Long customerId, Long tagId);

    /**
     * 從指定的客戶移除一個標籤。
     * @param customerId 客戶的 ID。
     * @param tagId 要移除的標籤 ID。
     * @return 操作完成後，包含最新標籤資訊的客戶 DTO。
     * @throws jakarta.persistence.EntityNotFoundException 如果客戶或標籤不存在。
     */
    CustomerDTO removeTag(Long customerId, Long tagId);

    /**
     * 同步（完全替換）指定客戶的所有標籤。
     * @param customerId 客戶的 ID。
     * @param request 包含客戶應具備的完整標籤 ID 列表。
     * @return 操作完成後，包含最新標籤資訊的客戶 DTO。
     * @throws jakarta.persistence.EntityNotFoundException 如果客戶不存在。
     */
    CustomerDTO updateTags(Long customerId, UpdateTagsRequest request);
}
