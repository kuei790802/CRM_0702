package com.example.demo.controller;


import com.example.demo.entity.Opportunity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.OpportunityService;

import java.util.List;


@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired
    private OpportunityService service;

    // Read All
    @GetMapping
    public List<Opportunity> getAll() {
        return service.findAll();
    }

    // Read By id
    @GetMapping("/{id}")
    public Opportunity getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Opportunity create(@RequestBody Opportunity opportunity) {
        return service.save(opportunity);
    }

    // Update
    @PutMapping("/{id}")
    public Opportunity update(@PathVariable Long id, @RequestBody Opportunity opportunity) {
        opportunity.setId(id);
        return service.save(opportunity);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
