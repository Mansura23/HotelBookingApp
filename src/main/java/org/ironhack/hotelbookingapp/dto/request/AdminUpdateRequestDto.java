package org.ironhack.hotelbookingapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;

import java.math.BigDecimal;

@Data
public class AdminUpdateRequestDto {

    @NotBlank(message = "FirstName is required.")
    private String firstName;

    @NotBlank(message = "LastName is required.")
    private String lastName;

    @NotBlank(message = "Number is required")
    private String number;

    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters.")
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Qualify role.")
    private Role role;

    @NotBlank(message = "Status is required.")
    private Status status;

    @NotBlank
    private BigDecimal balance;
}
