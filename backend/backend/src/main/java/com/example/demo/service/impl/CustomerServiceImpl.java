package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateOrUpdateCustomerRequest;
import com.example.demo.dto.request.UpdateTagsRequest;
import com.example.demo.dto.response.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Source;
import com.example.demo.entity.Tag;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.SourceRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final SourceRepository sourceRepository;
    private final TagRepository tagRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               SourceRepository sourceRepository,
                               TagRepository tagRepository) {
        this.customerRepository = customerRepository;
        this.sourceRepository = sourceRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + id));
        return convertToDto(customer);
    }

    @Override
    @Transactional
    public CustomerDTO create(CreateOrUpdateCustomerRequest request) {
        Customer customer = new Customer();
        mapRequestToEntity(request, customer);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerDTO update(Long id, CreateOrUpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + id));
        mapRequestToEntity(request, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDto(updatedCustomer);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("找不到客戶，ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CustomerDTO addTag(Long customerId, Long tagId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + customerId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("找不到標籤，ID: " + tagId));

        customer.addTag(tag);
        return convertToDto(customer);
    }

    @Override
    @Transactional
    public CustomerDTO removeTag(Long customerId, Long tagId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + customerId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("找不到標籤，ID: " + tagId));

        customer.removeTag(tag);
        return convertToDto(customer);
    }

    @Override
    @Transactional
    public CustomerDTO updateTags(Long customerId, UpdateTagsRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + customerId));

        Set<Tag> currentTags = new HashSet<>(customer.getTags());
        Set<Tag> newTags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));

        Set<Tag> tagsToRemove = new HashSet<>(currentTags);
        tagsToRemove.removeAll(newTags);

        Set<Tag> tagsToAdd = new HashSet<>(newTags);
        tagsToAdd.removeAll(currentTags);

        for (Tag tag : tagsToRemove) {
            customer.removeTag(tag);
        }
        for (Tag tag : tagsToAdd) {
            customer.addTag(tag);
        }

        return convertToDto(customer);
    }

    // ----- 輔助方法 -----
    private CustomerDTO convertToDto(Customer customer) {
        CustomerDTO dto = new CustomerDTO();

        dto.setCustomerId(customer.getCustomerId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setIndustry(customer.getIndustry());
        dto.setCustomerType(customer.getCustomerType());
        dto.setCustomerLevel(customer.getCustomerLevel());
        dto.setCustomerAddress(customer.getCustomerAddress());
        dto.setCustomerTel(customer.getCustomerTel());
        dto.setCustomerEmail(customer.getCustomerEmail());

        if (customer.getSource() != null) {
            dto.setSourceName(customer.getSource().getSourceName());
        }

        if (customer.getTags() != null) {
            dto.setTagNames(customer.getTags().stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    private void mapRequestToEntity(CreateOrUpdateCustomerRequest request, Customer customer) {
        customer.setCustomerName(request.getCustomerName());
        customer.setIndustry(request.getIndustry());
        customer.setCustomerType(request.getCustomerType());
        customer.setCustomerLevel(request.getCustomerLevel());
        customer.setCustomerAddress(request.getCustomerAddress());
        customer.setCustomerTel(request.getCustomerTel());
        customer.setCustomerEmail(request.getCustomerEmail());

        if (request.getSourceId() != null) {
            Source source = sourceRepository.findById(request.getSourceId())
                    .orElseThrow(() -> new EntityNotFoundException("找不到來源，ID: " + request.getSourceId()));
            customer.setSource(source);
        } else {
            customer.setSource(null);
        }
    }
}
