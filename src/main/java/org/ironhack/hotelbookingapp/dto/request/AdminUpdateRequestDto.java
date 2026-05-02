package org.ironhack.hotelbookingapp.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;

import java.math.BigDecimal;

@Data
public class AdminUpdateRequestDto {
    @Size(min = 3, max = 20)
    private String firstName;

    @Size(min = 3, max = 20)
    private String lastName;

    private String number;

    @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    private String password;

    private Role role;

    private Status status;

    @DecimalMin("0.01")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal balance;

}
