package org.ironhack.hotelbookingapp.dto.response;

import lombok.Data;
import org.ironhack.hotelbookingapp.entity.Booking;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserResponseForBooking {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private Role role;
    private Status status;
    private BigDecimal balance;
    List<BookingResponseDto> bookings;
}
