package com.example.demo.controller;

import com.example.demo.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.RecordService;

import java.util.List;


@RestController
@RequestMapping("/api/records")
public class RecordController {
    @Autowired
    private RecordService service;

    // Read All
    @GetMapping
    public List<Record> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public Record getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Record create(@RequestBody Record record) {
        return service.save(record);
    }

    // Update
    @PutMapping("/{id}")
    public Record update(@PathVariable Long id, @RequestBody Record record) {
        record.setId(id);
        return service.save(record);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
