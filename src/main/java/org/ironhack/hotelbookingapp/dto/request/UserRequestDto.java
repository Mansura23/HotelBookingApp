package org.ironhack.hotelbookingapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class UserRequestDto {
    @NotBlank(message = "FirstName is required")
    private String firstName;
    @NotBlank(message = "LastName is required")
    private String lastName;
    @Email(message = "Email format must be correct.")
    private String email;
    @NotBlank(message = "Number is required")
    private String number;
    @Size(min = 6, max = 12)
    private String password;


}
