package com.example.demo.service;

import com.example.demo.entity.Contact;
import com.example.demo.entity.BCustomer;
import com.example.demo.exception.ContactNotFoundException;
import com.example.demo.exception.BCustomerNotFoundException;
import com.example.demo.repository.ContactRepository;
import com.example.demo.repository.BCustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final BCustomerRepository BCustomerRepository;

    public ContactServiceImpl(ContactRepository contactRepository, BCustomerRepository BCustomerRepository) {
        this.contactRepository = contactRepository;
        this.BCustomerRepository = BCustomerRepository;
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public Contact findById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with ID: " + id));
    }

    @Override
    @Transactional
    public Contact save(Contact contact) {
        if (contact.getCustomer() == null || contact.getCustomer().getCustomerId() == null) {
            throw new IllegalArgumentException("Contact must be associated with an existing Customer ID.");
        }

        BCustomer BCustomer = BCustomerRepository.findById(contact.getCustomer().getCustomerId())
                .orElseThrow(() -> new BCustomerNotFoundException(
                        "Customer not found with ID: " + contact.getCustomer().getCustomerId()));

        contact.setCustomer(BCustomer);

        return contactRepository.save(contact);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ContactNotFoundException("Contact not found with ID: " + id);
        }
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> findContactsByCustomerId(Long customerId) {
        if (!BCustomerRepository.existsById(customerId)) {
            throw new BCustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        return contactRepository.findByCustomerId(customerId);
    }
}
