package com.example.demo.service;

import com.example.demo.model.Room;
import com.example.demo.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> listAll() { return roomRepository.findAll(); }

    public Optional<Room> getById(Long id) { return roomRepository.findById(id); }

    public Room create(Room r) { return roomRepository.save(r); }

    public Optional<Room> update(Long id, Room in) {
        return roomRepository.findById(id).map(existing -> {
            existing.setNumber(in.getNumber());
            existing.setType(in.getType());
            existing.setPricePerNight(in.getPricePerNight());
            return roomRepository.save(existing);
        });
    }

    public void delete(Long id) { roomRepository.deleteById(id); }
}
