package com.example.demo.controller;


import com.example.demo.entity.Quotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.QuotationService;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    @Autowired
    private QuotationService service;

    // Read All
    @GetMapping
    public List<Quotation> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public Quotation getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Quotation create(@RequestBody Quotation quotation) {
        return service.save(quotation);
    }

    // Update
    @PutMapping("/{id}")
    public Quotation update(@PathVariable Long id, @RequestBody Quotation quotation) {
        quotation.setId(id);
        return service.save(quotation);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
