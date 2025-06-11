package com.example.demo.controller;

import com.example.demo.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ActivityService;

import java.util.List;


@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    @Autowired
    private ActivityService service;

    // Read All
    @GetMapping
    public List<Activity> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public Activity getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Activity create(@RequestBody Activity activity) {
        return service.save(activity);
    }

    // Update
    @PutMapping("/{id}")
    public Activity update(@PathVariable Long id, @RequestBody Activity activity) {
        activity.setId(id);
        return service.save(activity);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
