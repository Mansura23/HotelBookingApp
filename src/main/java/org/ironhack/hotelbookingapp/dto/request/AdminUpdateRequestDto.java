package org.ironhack.hotelbookingapp.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;

import java.math.BigDecimal;

@Data
public class AdminUpdateRequestDto {
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotBlank(message = "Phone number cannot be empty")
    private String number;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    private String password;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @NotNull(message = "Status cannot be null")
    private Status status;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin("0.01")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal balance;

}
