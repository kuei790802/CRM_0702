package com.example.demo.controller;

import com.example.demo.dto.APIResponse;
import com.example.demo.dto.ContactRequestDto;
import com.example.demo.dto.ContactResponseDto;
import com.example.demo.entity.Contact;
import com.example.demo.entity.BCustomer;
import com.example.demo.service.BCustomerService;
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
    private final BCustomerService BCustomerService;

    public ContactController(ContactService contactService, BCustomerService BCustomerService) {
        this.contactService = contactService;
        this.BCustomerService = BCustomerService;
    }

    // ----- 將 Entity 轉換為 Response DTO -----
    private ContactResponseDto convertToContactResponse(Contact contact) {
        String customerName = null;
        Long customerId = null;
        if (contact.getCustomer() != null) {
            customerId = contact.getCustomer().getCustomerId();
            customerName = contact.getCustomer().getName();

        }
        return new ContactResponseDto(
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
    public ResponseEntity<List<ContactResponseDto>> getAll() {
        List<Contact> contacts = contactService.findAll();
        List<ContactResponseDto> contactResponsDtos = contacts.stream()
                .map(this::convertToContactResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contactResponsDtos);
    }
    // Read By id
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponseDto> getById(@PathVariable Long id) {
        Contact contact = contactService.findById(id);
        return ResponseEntity.ok(convertToContactResponse(contact));
    }

    // Create
    @PostMapping
    public ResponseEntity<ContactResponseDto> create(@Valid @RequestBody ContactRequestDto contactRequestDto) {
        // 從 Request DTO 轉換為 Entity
        Contact contact = new Contact();
        contact.setName(contactRequestDto.getName());
        contact.setTitle(contactRequestDto.getTitle());
        contact.setPhone(contactRequestDto.getPhone());
        contact.setEmail(contactRequestDto.getEmail());
        contact.setNotes(contactRequestDto.getNotes());

        BCustomer BCustomer = BCustomerService.findById(contactRequestDto.getCustomerId());

        contact.setCustomer(BCustomer);

        Contact savedContact = contactService.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(convertToContactResponse(savedContact));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody ContactRequestDto contactRequestDto) {

        Contact existingContact = contactService.findById(id);

        // 更新屬性
        existingContact.setName(contactRequestDto.getName());
        existingContact.setTitle(contactRequestDto.getTitle());
        existingContact.setPhone(contactRequestDto.getPhone());
        existingContact.setEmail(contactRequestDto.getEmail());
        existingContact.setNotes(contactRequestDto.getNotes());

        // 如果請求中提供了不同的客戶 ID，則更新所屬客戶
        if (contactRequestDto.getCustomerId() != null &&
                !contactRequestDto.getCustomerId().equals(existingContact.getCustomer().getCustomerId())) {


            BCustomer newBCustomer = BCustomerService.findById(contactRequestDto.getCustomerId());
            existingContact.setCustomer(newBCustomer);
        }

        Contact updatedContact = contactService.save(existingContact);
        return ResponseEntity.ok(convertToContactResponse(updatedContact));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.ok(new APIResponse("Contact with ID " + id + " deleted successfully.", true));
    }

    // ----- 獲取特定客戶下的所有聯絡人 -----
    @GetMapping("/byCustomer/{customerId}")
    public ResponseEntity<List<ContactResponseDto>> getContactsByCustomerId(@PathVariable Long customerId) {
        List<Contact> contacts = contactService.findContactsByCustomerId(customerId);
        List<ContactResponseDto> contactResponsDtos = contacts.stream()
                .map(this::convertToContactResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contactResponsDtos);
    }

}
