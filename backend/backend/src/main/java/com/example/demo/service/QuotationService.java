package com.example.demo.service;

import com.example.demo.entity.Quotation;
import org.springframework.stereotype.Service;
import com.example.demo.repository.QuotationRpository;


import java.util.List;

@Service
public class QuotationService {

    private final QuotationRpository quotationRepository;

    public QuotationService(QuotationRpository quotationRepository) {
        this.quotationRepository = quotationRepository;
    }

    public List<Quotation> findAll() {
        return quotationRepository.findAll();
    }

    public Quotation findById(Long id) {
        return quotationRepository.findById(id).orElse(null);
    }

    public Quotation save(Quotation contact) {
        return quotationRepository.save(contact);
    }

    public void delete(Long id) {
        quotationRepository.deleteById(id);
    }

}

