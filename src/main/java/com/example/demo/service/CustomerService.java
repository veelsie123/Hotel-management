package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> listAll() { return customerRepository.findAll(); }

    public Optional<Customer> getById(Long id) { return customerRepository.findById(id); }

    public Customer create(Customer c) { return customerRepository.save(c); }

    public Optional<Customer> update(Long id, Customer in) {
        return customerRepository.findById(id).map(existing -> {
            existing.setFirstName(in.getFirstName());
            existing.setLastName(in.getLastName());
            existing.setEmail(in.getEmail());
            existing.setPhone(in.getPhone());
            return customerRepository.save(existing);
        });
    }

    public void delete(Long id) { customerRepository.deleteById(id); }
}
