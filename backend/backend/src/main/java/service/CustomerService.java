package service;

import entity.Customer;
import org.springframework.stereotype.Service;
import repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer save(Customer customer) {
        // 新增填入時間
        if (customer.getId() == null) {
            customer.setCreatedAt(LocalDateTime.now());
        }
        // 更新時間
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
