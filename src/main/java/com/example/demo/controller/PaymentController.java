package com.example.demo.controller;

import com.example.demo.dto.PaymentDTO;
import com.example.demo.model.Payment;
import com.example.demo.model.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final com.example.demo.service.PaymentService paymentService;

    public PaymentController(com.example.demo.service.PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public java.util.List<Payment> list() { return paymentService.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> get(@PathVariable Long id) {
        return paymentService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PaymentDTO dto) {
        Reservation res = new Reservation();
        res.setId(dto.reservationId);
        Payment p = new Payment(dto.amount, dto.method == null ? "UNKNOWN" : dto.method, dto.paidAt == null ? LocalDateTime.now() : dto.paidAt, res);
        return paymentService.create(p)
                .map(saved -> ResponseEntity.created(URI.create("/api/payments/" + Objects.requireNonNull(saved.getId()))).<Object>body(saved))
                .orElseGet(() -> ResponseEntity.badRequest().body("Invalid reservation"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (paymentService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
