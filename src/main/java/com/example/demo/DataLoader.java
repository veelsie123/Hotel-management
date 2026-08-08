package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      CustomerRepository customerRepository,
                      RoomRepository roomRepository,
                      ReservationRepository reservationRepository,
                      PaymentRepository paymentRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", passwordEncoder.encode("changeme"), "ADMIN"));
            userRepository.save(new User("reception", passwordEncoder.encode("changeme"), "STAFF"));
        }

        if (customerRepository.count() == 0) {
            Customer c1 = customerRepository.save(new Customer("John", "Doe", "john@example.com", "555-0100"));
            Customer c2 = customerRepository.save(new Customer("Jane", "Smith", "jane@example.com", "555-0101"));

            Room r1 = roomRepository.save(new Room("101", "SINGLE", 75.0));
            Room r2 = roomRepository.save(new Room("102", "DOUBLE", 120.0));

            Reservation res1 = reservationRepository.save(new Reservation(c1, r1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3)));
            Reservation res2 = reservationRepository.save(new Reservation(c2, r2, LocalDate.now().plusDays(5), LocalDate.now().plusDays(7)));

            Payment p1 = new Payment(r1.getPricePerNight() * 2, "CARD", LocalDateTime.now(), res1);
            p1.setReservation(res1);
            res1.setPayment(p1);
            paymentRepository.save(p1);

            Payment p2 = new Payment(r2.getPricePerNight() * 2, "CASH", LocalDateTime.now(), res2);
            p2.setReservation(res2);
            res2.setPayment(p2);
            paymentRepository.save(p2);
        }

        System.out.println("DataLoader complete: users=" + userRepository.count() + 
                ", customers=" + customerRepository.count() + 
                ", rooms=" + roomRepository.count() + 
                ", reservations=" + reservationRepository.count() + 
                ", payments=" + paymentRepository.count());
    }
}
