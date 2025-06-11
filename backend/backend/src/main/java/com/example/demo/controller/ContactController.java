package com.example.demo.controller;

import com.example.demo.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService service;

    // Read All
    @GetMapping
    public List<Contact> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public Contact getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Contact create(@RequestBody Contact contact) {
        return service.save(contact);
    }

    // Update
    @PutMapping("/{id}")
    public Contact update(@PathVariable Long id, @RequestBody Contact contact) {
        contact.setId(id);
        return service.save(contact);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
