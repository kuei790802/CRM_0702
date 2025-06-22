package com.example.demo.service;

import com.example.demo.entity.BCustomer;
import com.example.demo.entity.Tag;
import com.example.demo.repository.BCustomerRepository;
import com.example.demo.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
// 實作 CustomerService 介面
public class BCustomerServiceImpl implements BCustomerService {

    private final BCustomerRepository BCustomerRepository;
    private final TagRepository tagRepository;

    public BCustomerServiceImpl(BCustomerRepository BCustomerRepository, TagRepository tagRepository) {
        this.BCustomerRepository = BCustomerRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<BCustomer> findAll() {
        return BCustomerRepository.findAll();
    }

    @Override
    public BCustomer findById(Long id) {
        // 使用 Optional 來處理可能找不到客戶的情況
        // 如果 findById 返回 Optional.empty()，則 orElse(null) 會返回 null
        return BCustomerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public BCustomer save(BCustomer BCustomer) {
        return BCustomerRepository.save(BCustomer);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        BCustomerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addTag(Long customerId, Long tagId) {
        // 1. 根據 ID 查找客戶實體
        BCustomer BCustomer = BCustomerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

        // 2. 根據 ID 查找標籤實體
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + tagId));

        // 3. 將查找到的標籤添加到客戶的標籤集合中
        BCustomer.getTags().add(tag);

        // 4. 儲存更新後的客戶實體，JPA 會自動更新中間表
        BCustomerRepository.save(BCustomer);
    }

    @Override
    @Transactional
    public void removeTag(Long customerId, Long tagId) {
        // 1. 根據 ID 查找客戶實體
        BCustomer BCustomer = BCustomerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

        // 2. 根據 ID 查找標籤實體
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + tagId));

        // 3. 從客戶的標籤集合中移除標籤
        BCustomer.getTags().removeIf(t -> t.getId().equals(tagId));

        // 4. 儲存更新後的客戶實體，JPA 會自動更新中間表
        BCustomerRepository.save(BCustomer);
    }
}
