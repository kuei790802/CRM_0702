package com.example.demo.service.impl;

import com.example.demo.dto.request.OpportunityRequest;
import com.example.demo.dto.response.OpportunityDto;
import com.example.demo.entity.BCustomer;
import com.example.demo.entity.Contact;
import com.example.demo.entity.Opportunity;
import com.example.demo.enums.OpportunityStage;
import com.example.demo.enums.OpportunityStatus;
import com.example.demo.mapper.OpportunityMapper;
import com.example.demo.repository.BCustomerRepository;
import com.example.demo.repository.ContactRepository;
import com.example.demo.repository.OpportunityRepository;
import com.example.demo.service.OpportunityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final BCustomerRepository bCustomerRepository;
    private final ContactRepository contactRepository;
    private final OpportunityMapper opportunityMapper;

    public OpportunityServiceImpl(OpportunityRepository opportunityRepository,
                                  BCustomerRepository bCustomerRepository,
                                  ContactRepository contactRepository,
                                  OpportunityMapper opportunityMapper) {
        this.opportunityRepository = opportunityRepository;
        this.bCustomerRepository = bCustomerRepository;
        this.contactRepository = contactRepository;
        this.opportunityMapper = opportunityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityDto> findAll(Pageable pageable) {
        return opportunityRepository.findAll(pageable)
                .map(opportunityMapper::toResponse);
    }


    @Override
    public Optional<OpportunityDto> findById(Long id) {
        Optional<Opportunity> opportunityOptional = opportunityRepository.findById(id);
        return opportunityOptional.map(opportunityMapper::toResponse);
    }

    @Override
    @Transactional
    public OpportunityDto create(OpportunityRequest request) {
        Opportunity opportunity = opportunityMapper.toEntity(request);

        BCustomer bCustomer = bCustomerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("找不到 ID 為 " + request.getCustomerId() + " 的客戶"));
        opportunity.setBCustomer(bCustomer);

        if (request.getContactId() != null) {
            Contact contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new EntityNotFoundException("找不到 ID 為 " + request.getContactId() + " 的聯絡人"));
            opportunity.setContact(contact);
        }

        Opportunity savedOpportunity = opportunityRepository.save(opportunity);
        return opportunityMapper.toResponse(savedOpportunity);
    }

    @Override
    @Transactional
    public OpportunityDto update(Long id, OpportunityRequest request) {
        Opportunity existingOpportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到 ID 為 " + id + " 的商機"));

        opportunityMapper.updateEntityFromRequest(existingOpportunity, request);

        if (!existingOpportunity.getBCustomer().getCustomerId().equals(request.getCustomerId())) {
            BCustomer newBCustomer = bCustomerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("找不到 ID 為 " + request.getCustomerId() + " 的客戶"));
            existingOpportunity.setBCustomer(newBCustomer);
        }

        if (request.getContactId() != null) {
            if (existingOpportunity.getContact() == null || !existingOpportunity.getContact().getContactId().equals(request.getContactId())) {
                Contact newContact = contactRepository.findById(request.getContactId())
                        .orElseThrow(() -> new EntityNotFoundException("找不到 ID 為 " + request.getContactId() + " 的聯絡人"));
                existingOpportunity.setContact(newContact);
            }
        } else {
            existingOpportunity.setContact(null);
        }

        Opportunity updatedOpportunity = opportunityRepository.save(existingOpportunity);
        return opportunityMapper.toResponse(updatedOpportunity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        opportunityRepository.deleteById(id);
    }


    /**
     * 實作多條件動態搜尋商機的方法。
     * 利用 Spring Data JPA 的 Specification 來動態構建查詢條件。
     * @param name 商機名稱模糊搜尋關鍵字
     * @param status 商機狀態
     * @param stage 商機階段
     * @param customerId 關聯客戶ID
     * @param contactId 關聯聯絡人ID
     * @param closeDateBefore 預計結束日期在指定日期之前
     * @param pageable 分頁資訊
     * @return 符合所有條件的商機回應 DTO 分頁列表
     */
    @Override
    @Transactional(readOnly = true) // 標註為只讀事務
    public Page<OpportunityDto> searchOpportunities(String name, OpportunityStatus status, OpportunityStage stage, Long customerId, Long contactId, LocalDate closeDateBefore, Pageable pageable) {
        Specification<Opportunity> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 條件：商機名稱模糊搜尋 (不區分大小寫)
            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("opportunityName")), "%" + name.toLowerCase() + "%"));
            }
            // 條件：商機狀態
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            // 條件：商機階段
            if (stage != null) {
                predicates.add(criteriaBuilder.equal(root.get("stage"), stage));
            }
            // 條件：關聯客戶ID
            if (customerId != null) {
                // 通過關聯物件 bCustomer 訪問其 customerId 屬性
                predicates.add(criteriaBuilder.equal(root.get("bCustomer").get("customerId"), customerId));
            }
            // 條件：關聯聯絡人ID
            if (contactId != null) {
                predicates.add(criteriaBuilder.equal(root.get("contact").get("contactId"), contactId));
            }
            // 條件：預計結束日期在指定日期之前 (包含指定日期)
            if (closeDateBefore != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("closeDate"), closeDateBefore));
            }

            // 將所有條件組合成一個 AND 邏輯的 Predicate
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return opportunityRepository.findAll(spec, pageable)
                .map(opportunityMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityDto> findByBCustomerId(Long customerId, Pageable pageable) {
        return opportunityRepository.findByBCustomer_CustomerId(customerId, pageable)
                .map(opportunityMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityDto> findByStatus(OpportunityStatus status, Pageable pageable) {
        return opportunityRepository.findByStatus(status, pageable)
                .map(opportunityMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityDto> findByStage(OpportunityStage stage, Pageable pageable) {
        return opportunityRepository.findByStage(stage, pageable)
                .map(opportunityMapper::toResponse);
    }
}