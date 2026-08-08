package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservationDTO {
    @NotNull
    public Long customerId;
    @NotNull
    public Long roomId;
    @NotNull
    public LocalDate checkIn;
    @NotNull
    public LocalDate checkOut;
}
