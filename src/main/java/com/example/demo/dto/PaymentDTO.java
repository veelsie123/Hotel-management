package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PaymentDTO {
    @NotNull
    public Long reservationId;
    @NotNull
    @Min(0)
    public Double amount;
    public String method;
    public LocalDateTime paidAt;
}
