package com.example.demo.service;


import com.example.demo.entity.QuotationDetail;
import org.springframework.stereotype.Service;
import com.example.demo.repository.QuotationDetailRepository;


import java.util.List;

@Service
public class QuotationDetailService {

    private final QuotationDetailRepository quotationDetailRepository;

    public QuotationDetailService(QuotationDetailRepository quotationDetailRepository) {
        this.quotationDetailRepository = quotationDetailRepository;
    }

    public List<QuotationDetail> findAll() {
        return quotationDetailRepository.findAll();
    }

    public QuotationDetail findById(Long id) {
        return quotationDetailRepository.findById(id).orElse(null);
    }

    public QuotationDetail save(QuotationDetail contact) {
        return quotationDetailRepository.save(contact);
    }

    public void delete(Long id) {
        quotationDetailRepository.deleteById(id);
    }

}

