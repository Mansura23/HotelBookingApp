package org.ironhack.hotelbookingapp.dto;


import lombok.Data;
import org.ironhack.hotelbookingapp.enums.RoomType;

import java.math.BigDecimal;

@Data
public class RoomRequestUpdateDto {
    private String roomNumber;

    private BigDecimal pricePerNight;

    private RoomType type;

    private Long hotelId;
}
