package com.example.demo.service;

import com.example.demo.entity.Contact;
import java.util.List;

public interface ContactService {

    List<Contact> findAll();

    Contact findById(Long id);

    Contact save(Contact contact);

    void delete(Long id);

    List<Contact> findContactsByCustomerId(Long customerId);
}
