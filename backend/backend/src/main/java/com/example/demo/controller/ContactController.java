package com.example.demo.controller;

import com.example.demo.dto.APIResponse;
import com.example.demo.dto.ContactRequest;
import com.example.demo.dto.ContactResponse;
import com.example.demo.entity.Contact;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ContactService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;
    private final CustomerService customerService;

    public ContactController(ContactService contactService, CustomerService customerService) {
        this.contactService = contactService;
        this.customerService = customerService;
    }

    // ----- 將 Entity 轉換為 Response DTO -----
    private ContactResponse convertToContactResponse(Contact contact) {
        String customerName = null;
        Long customerId = null;
        if (contact.getCustomer() != null) {
            customerId = contact.getCustomer().getId();
            customerName = contact.getCustomer().getName();
        }
        return new ContactResponse(
                contact.getId(),
                customerId,
                customerName,
                contact.getName(),
                contact.getTitle(),
                contact.getPhone(),
                contact.getEmail(),
                contact.getNotes()
        );
    }

    // Read All
    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAll() {
        List<Contact> contacts = contactService.findAll();
        List<ContactResponse> contactResponses = contacts.stream()
                .map(this::convertToContactResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contactResponses);
    }
    // Read By id
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getById(@PathVariable Long id) {
        Contact contact = contactService.findById(id);
        return ResponseEntity.ok(convertToContactResponse(contact));
    }

    // Create
    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody ContactRequest contactRequest) {
        // 從 Request DTO 轉換為 Entity
        Contact contact = new Contact();
        contact.setName(contactRequest.getName());
        contact.setTitle(contactRequest.getTitle());
        contact.setPhone(contactRequest.getPhone());
        contact.setEmail(contactRequest.getEmail());
        contact.setNotes(contactRequest.getNotes());

        Customer customer = customerService.findById(contactRequest.getCustomerId());

        contact.setCustomer(customer);

        Contact savedContact = contactService.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(convertToContactResponse(savedContact));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ContactRequest contactRequest) {

        Contact existingContact = contactService.findById(id);

        // 更新屬性
        existingContact.setName(contactRequest.getName());
        existingContact.setTitle(contactRequest.getTitle());
        existingContact.setPhone(contactRequest.getPhone());
        existingContact.setEmail(contactRequest.getEmail());
        existingContact.setNotes(contactRequest.getNotes());

        // 如果請求中提供了不同的客戶 ID，則更新所屬客戶
        if (contactRequest.getCustomerId() != null &&
                !contactRequest.getCustomerId().equals(existingContact.getCustomer().getId())) {

            Customer newCustomer = customerService.findById(contactRequest.getCustomerId());
            existingContact.setCustomer(newCustomer);
        }

        Contact updatedContact = contactService.save(existingContact);
        return ResponseEntity.ok(convertToContactResponse(updatedContact));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteContact(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.ok(new APIResponse("Contact with ID " + id + " deleted successfully.", true));
    }

    // ----- 獲取特定客戶下的所有聯絡人 -----
    @GetMapping("/byCustomer/{customerId}")
    public ResponseEntity<List<ContactResponse>> getContactsByCustomerId(@PathVariable Long customerId) {
        List<Contact> contacts = contactService.findContactsByCustomerId(customerId);
        List<ContactResponse> contactResponses = contacts.stream()
                .map(this::convertToContactResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contactResponses);
    }

}
