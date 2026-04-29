package org.ironhack.hotelbookingapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class UserRequestDto {
    private String firstName;
    private String lastName;
    @Email(message = "Email format must be correct.")
    private String email;
    private String number;
    @Size(min = 6, max = 12)
    private String password;


}
