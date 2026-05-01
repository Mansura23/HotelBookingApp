package org.ironhack.hotelbookingapp.dto.response;

import jakarta.persistence.*;
import lombok.Data;
import org.ironhack.hotelbookingapp.entity.Room;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class BookingResponseDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalPrice;
    private Long roomId;
    private BookingStatus bookingStatus;

}
