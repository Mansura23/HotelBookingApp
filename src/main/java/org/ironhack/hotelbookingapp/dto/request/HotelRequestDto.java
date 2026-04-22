package org.ironhack.hotelbookingapp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelRequestDto {

    @NotBlank(message = "Hotel name is required")
    @Size(min = 2, max = 150, message = "Hotel name must be between 2 and 150 characters")
    private String name;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotBlank(message = "Country can't be blank")
    private String country;

    @NotBlank(message = "City can't be blank")
    private String city;

    @NotBlank(message = "Address can't be blank")
    private String address;

    private String zipCode;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Min(value = 1, message = "Rating must be at least 1.0")
    @Max(value = 5, message = "Rating must be at most 5.0")
    private Double rating;
}
