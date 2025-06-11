package com.example.demo.controller;


import com.example.demo.entity.QuotationDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.QuotationDetailService;



import java.util.List;


@RestController
@RequestMapping("/api/quotationDetails")
public class QuotationDetailController {

    @Autowired
    private QuotationDetailService service;

    // Read All
    @GetMapping
    public List<QuotationDetail> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public QuotationDetail getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public QuotationDetail create(@RequestBody QuotationDetail quotationDetail) {
        return service.save(quotationDetail);
    }

    // Update
    @PutMapping("/{id}")
    public QuotationDetail update(@PathVariable Long id, @RequestBody QuotationDetail quotationDetail) {
        quotationDetail.setId(id);
        return service.save(quotationDetail);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
