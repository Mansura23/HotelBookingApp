package org.ironhack.hotelbookingapp.dto.request;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.RoomType;

import java.math.BigDecimal;

@Data
public class RoomRequestDto {
    @NotBlank(message = "RoomNumber should not be empty")
    @Size(min = 1, max = 50)
    @Column(unique=true)
    private String roomNumber;

    @DecimalMin("0.01")
    @Digits(integer = 6, fraction = 2)
    @NotNull(message = "Price should not be null")
    private BigDecimal pricePerNight;

    @NotNull(message = "Type must not be null")
    private RoomType type;

    @NotNull(message = "HotelId must nor be null")
    private Long hotelId;


}
