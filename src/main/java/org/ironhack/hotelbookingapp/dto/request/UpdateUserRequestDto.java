package org.ironhack.hotelbookingapp.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String firstName;
    private String lastName;
    private String number;
    @Size(min = 6, max = 12)
    private String password;
}
