package org.ironhack.hotelbookingapp.dto.response;

import lombok.Data;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;

import java.math.BigDecimal;

@Data
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private Role role;
    private Status status;
    private BigDecimal balance;
}
