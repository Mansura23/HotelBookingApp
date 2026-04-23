package org.ironhack.hotelbookingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ironhack.hotelbookingapp.enums.RoomType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDto {
    private Long id;

    private String roomNumber;

    private BigDecimal pricePerNight;

    private RoomType type;

    private Long hotelId;
    private String hotelName;

    private boolean isAvailable;

}
