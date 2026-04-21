package org.ironhack.hotelbookingapp.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.RoomType;

import java.math.BigDecimal;

@Data
public class RoomRequestDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String roomNumber;

    @DecimalMin("0.01")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal pricePerNight;

    @NotNull
    private RoomType type;

    @NotNull
    private Long hotelId;



}
