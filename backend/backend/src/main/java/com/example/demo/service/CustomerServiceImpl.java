package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Tag;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
// 實作 CustomerService 介面
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TagRepository tagRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, TagRepository tagRepository) {
        this.customerRepository = customerRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Long id) {
        // 使用 Optional 來處理可能找不到客戶的情況
        // 如果 findById 返回 Optional.empty()，則 orElse(null) 會返回 null
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addTag(Long customerId, Long tagId) {
        // 1. 根據 ID 查找客戶實體
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

        // 2. 根據 ID 查找標籤實體
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + tagId));

        // 3. 將查找到的標籤添加到客戶的標籤集合中
        customer.addTag(tag);

        // 4. 儲存更新後的客戶實體，JPA 會自動更新中間表
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void removeTag(Long customerId, Long tagId) {
        // 1. 根據 ID 查找客戶實體
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

        // 2. 根據 ID 查找標籤實體
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + tagId));

        // 3. 從客戶的標籤集合中移除標籤
        customer.removeTag(tag);

        // 4. 儲存更新後的客戶實體，JPA 會自動更新中間表
        customerRepository.save(customer);
    }
}
