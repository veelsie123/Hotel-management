package com.example.demo.service;

import com.example.demo.dto.ReservationDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, CustomerRepository customerRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    public Optional<Reservation> createReservation(ReservationDTO dto) {
        if (reservationRepository.existsOverlap(dto.roomId, dto.checkIn, dto.checkOut)) {
            return Optional.empty();
        }

        Customer customer = customerRepository.findById(dto.customerId).orElse(null);
        Room room = roomRepository.findById(dto.roomId).orElse(null);
        if (customer == null || room == null) return Optional.empty();

        Reservation reservation = new Reservation(customer, room, dto.checkIn, dto.checkOut);
        return Optional.of(reservationRepository.save(reservation));
    }
}
