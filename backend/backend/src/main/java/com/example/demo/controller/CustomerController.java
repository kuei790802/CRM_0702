package com.example.demo.controller;

import com.example.demo.dto.request.CreateOrUpdateCustomerRequest;
import com.example.demo.dto.request.UpdateTagsRequest;
import com.example.demo.dto.response.CustomerDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.impl.CustomerServiceImpl;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    /**
     * 獲取所有客戶 (分頁)
     * GET /api/customers?page=0&size=10&sort=customerName,asc
     * @param pageable Spring 自動傳入的分頁與排序參數
     * @return 客戶分頁資料
     */
    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAll(Pageable pageable) {
        Page<CustomerDTO> customers = customerService.findAll(pageable);
        return ResponseEntity.ok(customers);
    }

    /**
     * 根據 ID 獲取單一客戶
     * GET /api/customers/{id}
     * @param id 客戶 ID
     * @return 找到的客戶 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        CustomerDTO customerDto = customerService.findById(id);
        return ResponseEntity.ok(customerDto);
    }

    /**
     * 建立一個新客戶
     * POST /api/customers
     * @param request 包含客戶資料的請求 DTO
     * @return 建立成功後的新客戶 DTO，HTTP 狀態為 201 Created
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CreateOrUpdateCustomerRequest request) {
        CustomerDTO createdCustomer = customerService.create(request);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * 根據 ID 更新一個客戶
     * PUT /api/customers/{id}
     * @param id 要更新的客戶 ID
     * @param request 包含更新資料的請求 DTO
     * @return 更新成功後的客戶 DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateCustomerRequest request) {
        CustomerDTO updatedCustomer = customerService.update(id, request);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * 根據 ID 刪除一個客戶
     * DELETE /api/customers/{id}
     * @param id 要刪除的客戶 ID
     * @return HTTP 狀態 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 更新指定客戶的所有標籤
     * PUT /api/customers/{customerId}/tags
     * @param customerId 客戶 ID
     * @param request 包含新的標籤 ID 列表
     * @return 更新後的客戶 DTO
     */
    @PutMapping("/{customerId}/tags")
    public ResponseEntity<CustomerDTO> updateTags(@PathVariable Long customerId, @RequestBody UpdateTagsRequest request) {
        CustomerDTO updatedCustomer = customerService.updateTags(customerId, request);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * 為客戶新增一個標籤 (雖然上面有批次更新，但也可以保留單一操作)
     * POST /api/customers/{customerId}/tags/{tagId}
     * @param customerId 客戶 ID
     * @param tagId 要新增的標籤 ID
     * @return 更新後的客戶 DTO
     */
    @PostMapping("/{customerId}/tags/{tagId}")
    public ResponseEntity<CustomerDTO> addTag(@PathVariable Long customerId, @PathVariable Long tagId) {
        CustomerDTO customer = customerService.addTag(customerId, tagId);
        return ResponseEntity.ok(customer);
    }

    /**
     * 從客戶移除一個標籤
     * DELETE /api/customers/{customerId}/tags/{tagId}
     * @param customerId 客戶 ID
     * @param tagId 要移除的標籤 ID
     * @return 更新後的客戶 DTO
     */
    @DeleteMapping("/{customerId}/tags/{tagId}")
    public ResponseEntity<CustomerDTO> removeTag(@PathVariable Long customerId, @PathVariable Long tagId) {
        CustomerDTO customer = customerService.removeTag(customerId, tagId);
        return ResponseEntity.ok(customer);
    }

}
