package controller;

import entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    // Read All
    @GetMapping
    public List<Customer> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return service.save(customer);
    }

    // Update
    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        return service.save(customer);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
