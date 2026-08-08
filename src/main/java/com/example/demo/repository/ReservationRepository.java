package com.example.demo.repository;

import com.example.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	@org.springframework.data.jpa.repository.Query("SELECT CASE WHEN COUNT(r)>0 THEN true ELSE false END FROM Reservation r WHERE r.room.id = :roomId AND r.checkIn < :checkOut AND r.checkOut > :checkIn")
	boolean existsOverlap(Long roomId, java.time.LocalDate checkIn, java.time.LocalDate checkOut);
}
