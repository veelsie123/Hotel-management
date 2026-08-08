package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoomDTO {

    @NotBlank
    public String number;

    @NotBlank
    public String type;

    @NotNull
    @Min(0)
    public Double pricePerNight;
}
