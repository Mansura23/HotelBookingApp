package org.ironhack.hotelbookingapp.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    @NotBlank(message = "FirstName is required.")
    private String firstName;

    @NotBlank(message = "LastName is required.")
    private String lastName;

    @NotBlank(message = "Number is required")
    private String number;

    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters.")
    @NotBlank(message = "Password is required")
    private String password;
}
