package com.example.demo.controller;

import com.example.demo.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final com.example.demo.service.RoomService roomService;

    public RoomController(com.example.demo.service.RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> list() { return roomService.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Room> get(@PathVariable Long id) {
        return roomService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> create(@org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.example.demo.dto.RoomDTO dto) {
        Room room = new Room(dto.number, dto.type, dto.pricePerNight);
        Room saved = roomService.create(room);
        return ResponseEntity.created(URI.create("/api/rooms/" + Objects.requireNonNull(saved.getId()))).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.example.demo.dto.RoomDTO dto) {
        Room in = new Room(dto.number, dto.type, dto.pricePerNight);
        return roomService.update(id, in).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (roomService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
