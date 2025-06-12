package com.example.demo.service;

import com.example.demo.entity.Contact;
import com.example.demo.entity.Customer;
import com.example.demo.exception.ContactNotFoundException;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.repository.ContactRepository;
import com.example.demo.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;

    public ContactServiceImpl(ContactRepository contactRepository, CustomerRepository customerRepository) {
        this.contactRepository = contactRepository;
        this.customerRepository = customerRepository;
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
        if (contact.getCustomer() == null || contact.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Contact must be associated with an existing Customer ID.");
        }

        Customer customer = customerRepository.findById(contact.getCustomer().getId())
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + contact.getCustomer().getId()));

        contact.setCustomer(customer);

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
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        return contactRepository.findByCustomerId(customerId);
    }
}
