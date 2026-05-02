package org.ironhack.hotelbookingapp.dto.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ironhack.hotelbookingapp.entity.Room;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponseDtoForUser {

    private Long id;
    private String name;
    private String description;
    private String country;
    private String zipCode;
    private String phone;
    private String city;
    private String address;
    private double rating;
    private List<RoomResponseDto> rooms;

}
