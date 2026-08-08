package com.example.demo.service;

import com.example.demo.model.Payment;
import com.example.demo.model.Reservation;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Payment> listAll() { return paymentRepository.findAll(); }

    public Optional<Payment> getById(Long id) { return paymentRepository.findById(id); }

    public Optional<Payment> create(Payment p) {
        // ensure reservation exists
        Reservation res = p.getReservation();
        if (res == null || res.getId() == null) {
            return Optional.empty();
        }
        return reservationRepository.findById(res.getId()).map(found -> {
            p.setReservation(found);
            found.setPayment(p);
            return paymentRepository.save(p);
        });
    }

    public void delete(Long id) { paymentRepository.deleteById(id); }
}
