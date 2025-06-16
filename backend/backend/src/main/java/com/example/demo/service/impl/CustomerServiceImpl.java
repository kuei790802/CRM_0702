package com.example.demo.service.impl;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerDto;
import com.example.demo.entity.Customer;


import com.example.demo.enums.CustomerIndustry;
import com.example.demo.enums.CustomerLevel;
import com.example.demo.enums.CustomerType;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * 以分頁的方式獲取所有客戶。
     * @param pageable 包含分頁和排序資訊的請求物件。
     * @return 包含客戶 DTO 列表和分頁資訊的 Page 物件。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    /**
     * 根據客戶的唯一 ID 尋找客戶。
     * @param id 要尋找的客戶 ID。
     * @return 包含客戶詳細資訊的 DTO。
     * @throws EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerDto findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + id));
        return convertToDto(customer);
    }

    /**
     * 根據請求資料建立一個新的客戶。
     * @param request 包含新客戶所有必要資訊的請求 DTO。
     * @return 建立成功後，包含新客戶完整資訊（含 ID）的 DTO。
     */
    @Override
    @Transactional
    public CustomerDto create(CustomerRequest request) {
        Customer customer = new Customer();
        mapRequestToEntity(request, customer);

        if (customer.getCustomerType() == null) {
            customer.setCustomerType(CustomerType.REGULAR); // 預設客戶類型
        }
        if (customer.getCustomerLevel() == null) {
            customer.setCustomerLevel(CustomerLevel.BRONZE); // 預設客戶等級
        }
        if (customer.getIndustry() == null) {
            customer.setIndustry(CustomerIndustry.OTHER); // 預設客戶行業
        }

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    /**
     * 根據 ID 更新一個已存在的客戶資訊。
     * @param id 要更新的客戶 ID。
     * @param request 包含要更新欄位的請求 DTO。
     * @return 更新成功後，包含最新客戶資訊的 DTO。
     * @throws EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    @Override
    @Transactional
    public CustomerDto update(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + id));

        mapRequestToEntity(request, customer);

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDto(updatedCustomer);
    }

    /**
     * 根據 ID 刪除一個客戶。
     * @param id 要刪除的客戶 ID。
     * @throws EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("找不到客戶，ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    /**
     * 根據客戶名稱進行模糊查詢（不區分大小寫）。
     * @param name 客戶名稱的部分字串。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶 DTO 分頁列表。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> findCustomersByNameContaining(String name, Pageable pageable) {
        return customerRepository.findByCustomerNameContainingIgnoreCase(name, pageable)
                .map(this::convertToDto);
    }

    /**
     * 根據行業查詢客戶。
     * @param industry 行業名稱 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶 DTO 分頁列表。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> findCustomersByIndustry(CustomerIndustry industry, Pageable pageable) {
        return customerRepository.findByIndustry(industry, pageable)
                .map(this::convertToDto);
    }

    /**
     * 根據客戶類型查詢客戶。
     * @param customerType 客戶類型 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶 DTO 分頁列表。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> findCustomersByCustomerType(CustomerType customerType, Pageable pageable) {
        return customerRepository.findByCustomerType(customerType, pageable)
                .map(this::convertToDto);
    }

    /**
     * 根據客戶等級查詢客戶。
     * @param customerLevel 客戶等級 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶 DTO 分頁列表。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> findCustomersByCustomerLevel(CustomerLevel customerLevel, Pageable pageable) {
        return customerRepository.findByCustomerLevel(customerLevel, pageable)
                .map(this::convertToDto);
    }

    // ----- 輔助方法 -----
    /**
     * 將 Customer 實體轉換為 CustomerDto。
     * 此方法用於向前端返回資料，只包含前端所需的資訊。
     * @param customer 要轉換的 Customer 實體。
     * @return 轉換後的 CustomerDto。
     */
    private CustomerDto convertToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setIndustry(customer.getIndustry());
        dto.setCustomerType(customer.getCustomerType());
        dto.setCustomerLevel(customer.getCustomerLevel());
        dto.setCustomerAddress(customer.getCustomerAddress());
        dto.setCustomerTel(customer.getCustomerTel());
        dto.setCustomerEmail(customer.getCustomerEmail());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        return dto;
    }

    /**
     * 將 CustomerRequest DTO 中的資料映射到 Customer 實體。
     * 此方法主要處理簡單的屬性複製。
     * @param request 包含請求資料的 DTO。
     * @param customer 要映射的目標 Customer 實體。
     */
    private void mapRequestToEntity(CustomerRequest request, Customer customer) {
        customer.setCustomerName(request.getCustomerName());
        customer.setIndustry(request.getIndustry());
        customer.setCustomerType(request.getCustomerType());
        customer.setCustomerLevel(request.getCustomerLevel());
        customer.setCustomerAddress(request.getCustomerAddress());
        customer.setCustomerTel(request.getCustomerTel());
        customer.setCustomerEmail(request.getCustomerEmail());
    }
}
