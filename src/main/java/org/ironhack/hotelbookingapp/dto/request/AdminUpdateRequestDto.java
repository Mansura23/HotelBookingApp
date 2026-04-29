package org.ironhack.hotelbookingapp.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;

import java.math.BigDecimal;

@Data
public class AdminUpdateRequestDto {
    private String firstName;
    private String lastName;
    private String number;
    private String password;
    private Role role;
    private Status status;
    private BigDecimal balance;
}
