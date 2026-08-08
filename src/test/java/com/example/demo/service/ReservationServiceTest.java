package com.example.demo.service;

import com.example.demo.dto.ReservationDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void createReservation_returnsEmptyWhenOverlap() {
        ReservationDTO dto = new ReservationDTO();
        dto.customerId = 1L;
        dto.roomId = 2L;
        dto.checkIn = LocalDate.now().plusDays(1);
        dto.checkOut = LocalDate.now().plusDays(2);

        when(reservationRepository.existsOverlap(dto.roomId, dto.checkIn, dto.checkOut)).thenReturn(true);

        Optional<Reservation> result = reservationService.createReservation(dto);

        assertTrue(result.isEmpty());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_returnsEmptyWhenCustomerMissing() {
        ReservationDTO dto = new ReservationDTO();
        dto.customerId = 5L;
        dto.roomId = 2L;
        dto.checkIn = LocalDate.now().plusDays(1);
        dto.checkOut = LocalDate.now().plusDays(2);

        when(reservationRepository.existsOverlap(dto.roomId, dto.checkIn, dto.checkOut)).thenReturn(false);
        when(customerRepository.findById(dto.customerId)).thenReturn(Optional.empty());

        Optional<Reservation> result = reservationService.createReservation(dto);

        assertTrue(result.isEmpty());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_returnsEmptyWhenRoomMissing() {
        ReservationDTO dto = new ReservationDTO();
        dto.customerId = 1L;
        dto.roomId = 2L;
        dto.checkIn = LocalDate.now().plusDays(1);
        dto.checkOut = LocalDate.now().plusDays(2);

        when(reservationRepository.existsOverlap(dto.roomId, dto.checkIn, dto.checkOut)).thenReturn(false);
        when(customerRepository.findById(dto.customerId)).thenReturn(Optional.of(new Customer()));
        when(roomRepository.findById(dto.roomId)).thenReturn(Optional.empty());

        Optional<Reservation> result = reservationService.createReservation(dto);

        assertTrue(result.isEmpty());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_savesReservationWhenValid() {
        ReservationDTO dto = new ReservationDTO();
        dto.customerId = 1L;
        dto.roomId = 2L;
        dto.checkIn = LocalDate.now().plusDays(1);
        dto.checkOut = LocalDate.now().plusDays(2);

        Customer customer = new Customer();
        Room room = new Room();
        when(reservationRepository.existsOverlap(dto.roomId, dto.checkIn, dto.checkOut)).thenReturn(false);
        when(customerRepository.findById(dto.customerId)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(dto.roomId)).thenReturn(Optional.of(room));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Reservation> result = reservationService.createReservation(dto);

        assertTrue(result.isPresent());
        Reservation saved = result.get();
        assertSame(customer, saved.getCustomer());
        assertSame(room, saved.getRoom());
        assertEquals(dto.checkIn, saved.getCheckIn());
        assertEquals(dto.checkOut, saved.getCheckOut());
        verify(reservationRepository).save(any(Reservation.class));
    }
}
