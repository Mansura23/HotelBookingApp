package org.ironhack.hotelbookingapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDto {
    private Long id;
    private String name;
    private String description;
    private String country;
    private String city;
    private String address;
    private String zipCode;
    private String phone;
    private Double rating;
}
