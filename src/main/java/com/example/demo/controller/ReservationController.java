package com.example.demo.controller;

import com.example.demo.model.Reservation;
import com.example.demo.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final com.example.demo.service.ReservationService reservationService;

    public ReservationController(ReservationRepository reservationRepository,
                                 com.example.demo.service.ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> list() { return reservationRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> get(@PathVariable Long id) {
        return reservationRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.example.demo.dto.ReservationDTO req) {
        var created = reservationService.createReservation(req);
        return created.map(r -> ResponseEntity.created(URI.create("/api/reservations/" + Objects.requireNonNull(r.getId()))).<Object>body(r))
                .orElseGet(() -> ResponseEntity.status(409).body("Room is not available or invalid data"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!reservationRepository.existsById(id)) return ResponseEntity.notFound().build();
        reservationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
