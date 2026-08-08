package com.example.demo.service;

import com.example.demo.model.Payment;
import com.example.demo.model.Reservation;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void create_returnsEmptyWhenReservationIsNull() {
        Payment payment = new Payment();
        payment.setAmount(50.0);
        payment.setMethod("CARD");
        payment.setPaidAt(LocalDateTime.now());

        Optional<com.example.demo.model.Payment> result = paymentService.create(payment);

        assertTrue(result.isEmpty());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void create_returnsEmptyWhenReservationIdIsNull() {
        Reservation reservation = new Reservation();
        paymentWithReservation(reservation, 50.0);

        Optional<com.example.demo.model.Payment> result = paymentService.create(paymentWithReservation(reservation, 50.0));

        assertTrue(result.isEmpty());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void create_returnsEmptyWhenReservationNotFound() {
        Reservation reservation = new Reservation();
        reservation.setId(99L);

        Payment payment = paymentWithReservation(reservation, 50.0);
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<com.example.demo.model.Payment> result = paymentService.create(payment);

        assertTrue(result.isEmpty());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void create_savesPaymentWhenReservationFound() {
        Reservation transactionReservation = new Reservation();
        transactionReservation.setId(1L);

        Reservation storedReservation = new Reservation();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(storedReservation));

        Payment payment = paymentWithReservation(transactionReservation, 100.0);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<com.example.demo.model.Payment> result = paymentService.create(payment);

        assertTrue(result.isPresent());
        assertSame(payment, result.get());
        verify(paymentRepository).save(payment);
        assertSame(payment, storedReservation.getPayment());
    }

    private Payment paymentWithReservation(Reservation reservation, double amount) {
        Payment p = new Payment();
        p.setAmount(amount);
        p.setMethod("CARD");
        p.setPaidAt(LocalDateTime.now());
        p.setReservation(reservation);
        return p;
    }
}
