package com.example.demo.controller;

import com.example.demo.dto.APIResponse;
import com.example.demo.entity.BCustomer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.BCustomerServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class BCustomerController {


    private final BCustomerServiceImpl customerService;

    public BCustomerController(BCustomerServiceImpl customerService) {
        this.customerService = customerService;
    }



    // Read All
    @GetMapping
    public ResponseEntity<List<BCustomer>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }
    // Read By id
    @GetMapping("/{id}")
    public ResponseEntity<BCustomer> getById(@PathVariable Long id) {
        BCustomer BCustomer = customerService.findById(id);
        if (BCustomer == null) {
            // null -> 404 Not Found
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BCustomer);
    }

    // Create
    @PostMapping
    public ResponseEntity<BCustomer> create(@RequestBody BCustomer BCustomer) {
        // 201
        BCustomer savedBCustomer = customerService.save(BCustomer);
        // 200
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBCustomer);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<BCustomer> update(@PathVariable Long id, @RequestBody BCustomer BCustomer) {
        BCustomer.setCustomerId(id);
        BCustomer updatedBCustomer = customerService.save(BCustomer);
        if (updatedBCustomer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBCustomer);
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
