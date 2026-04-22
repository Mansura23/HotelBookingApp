package org.ironhack.hotelbookingapp.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.RoomType;

import java.math.BigDecimal;

@Data
public class RoomRequestUpdateDto {
    private String roomNumber;


    @DecimalMin("0.01")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal pricePerNight;

    private RoomType type;

    @Positive
    private Long hotelId;
}
