package com.example.demo.controller;

import com.example.demo.dto.APIResponse;
import com.example.demo.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CustomerServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {


    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }



    // Read All
    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }
    // Read By id
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            // null -> 404 Not Found
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    // Create
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        // 201
        Customer savedCustomer = customerService.save(customer);
        // 200
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        Customer updatedCustomer = customerService.save(customer); // 如果不存在，save 可能會建立新資源或拋出異常
        if (updatedCustomer == null) {
            // 假設 service.save 返回 null 如果資源不存在
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCustomer);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        // 刪除成功 -> 204 No Content，表示操作成功但沒有內容返回
        return ResponseEntity.noContent().build();
    }

    // ----- 新增標籤至客戶 -----
    @PostMapping("/{id}/tags/{tagId}")
    public ResponseEntity<APIResponse> addTag(@PathVariable Long id, @PathVariable Long tagId) {
        customerService.addTag(id, tagId);
        return ResponseEntity.ok(new APIResponse(
                "Tag " + tagId + " added successfully for customer " + id, true));
    }

    // ----- 從客戶移除標籤 -----
    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<APIResponse> removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        customerService.removeTag(id, tagId);
        return ResponseEntity.ok(new APIResponse(
                "Tag " + tagId + " removed successfully from customer " + id, true));
    }

}
