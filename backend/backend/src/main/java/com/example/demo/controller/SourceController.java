package com.example.demo.controller;

import com.example.demo.entity.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.SourceService;

import java.util.List;

@RestController
@RequestMapping("/api/sources")
public class SourceController {

    @Autowired
    private SourceService service;

    // Read All
    @GetMapping
    public List<Source> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public Source getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Source create(@RequestBody Source source) {
        return service.save(source);
    }

    // Update
    @PutMapping("/{id}")
    public Source update(@PathVariable Long id, @RequestBody Source source) {
        source.setId(id);
        return service.save(source);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
