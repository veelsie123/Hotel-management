package com.example.demo.controller;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final com.example.demo.service.CustomerService customerService;

    public CustomerController(com.example.demo.service.CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> list() { return customerService.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable Long id) {
        return customerService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody CustomerDTO dto) {
        Customer c = new Customer(dto.firstName, dto.lastName, dto.email, dto.phone);
        Customer saved = customerService.create(c);
        return ResponseEntity.created(URI.create("/api/customers/" + Objects.requireNonNull(saved.getId()))).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @Valid @RequestBody CustomerDTO dto) {
        Customer in = new Customer(dto.firstName, dto.lastName, dto.email, dto.phone);
        return customerService.update(id, in).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (customerService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
