package org.ironhack.hotelbookingapp.mapper;

import org.ironhack.hotelbookingapp.dto.request.HotelRequestDto;
import org.ironhack.hotelbookingapp.dto.request.HotelRequestUpdateDto;
import org.ironhack.hotelbookingapp.dto.response.BookingResponseDto;
import org.ironhack.hotelbookingapp.dto.response.HotelResponseDto;
import org.ironhack.hotelbookingapp.dto.response.HotelResponseDtoForUser;
import org.ironhack.hotelbookingapp.dto.response.RoomResponseDto;
import org.ironhack.hotelbookingapp.entity.Hotel;

import java.util.List;
import java.util.stream.Collectors;

public class HotelMapper {

    private HotelMapper() {}

    public static Hotel toModel(HotelRequestDto requestDto) {
        if (requestDto == null) return null;
        Hotel hotel = new Hotel();
        hotel.setName(requestDto.getName());
        hotel.setDescription(requestDto.getDescription());
        hotel.setCountry(requestDto.getCountry());
        hotel.setCity(requestDto.getCity());
        hotel.setAddress(requestDto.getAddress());
        hotel.setZipCode(requestDto.getZipCode());
        hotel.setPhone(requestDto.getPhone());
        hotel.setRating(requestDto.getRating());
        return hotel;
    }

    public static void updateModel(HotelRequestUpdateDto updateDto, Hotel existingHotel) {
    if(updateDto == null) return;
    if(updateDto.getName() != null)  existingHotel.setName(updateDto.getName());
    if(updateDto.getDescription() != null)  existingHotel.setDescription(updateDto.getDescription());
    if (updateDto.getAddress() != null)  existingHotel.setAddress(updateDto.getAddress());
    if(updateDto.getZipCode() != null)  existingHotel.setZipCode(updateDto.getZipCode());
    if(updateDto.getPhone() != null)  existingHotel.setPhone(updateDto.getPhone());
    if(updateDto.getRating() != null)  existingHotel.setRating(updateDto.getRating());
    }

    public static HotelResponseDto toResponseDto(Hotel hotel) {
        if (hotel == null) return null;
        return new HotelResponseDto(
                hotel.getId(), hotel.getName(), hotel.getDescription(),
                hotel.getCountry(), hotel.getCity(), hotel.getAddress(),
                hotel.getZipCode(), hotel.getPhone(), hotel.getRating()
        );
    }

    public static List<HotelResponseDtoForUser> toResponseList(List<Hotel> hotels) {
        return hotels.stream().map(HotelMapper::toResponseDtoForUser).collect(Collectors.toList());
    }
    public  static HotelResponseDtoForUser  toResponseDtoForUser(Hotel hotel) {
        HotelResponseDtoForUser responseDto = new HotelResponseDtoForUser();
        responseDto.setId(hotel.getId());
        responseDto.setName(hotel.getName());
        responseDto.setCountry(hotel.getCountry());
        responseDto.setCity(hotel.getCity());
        responseDto.setDescription(hotel.getDescription());
        responseDto.setAddress(hotel.getAddress());
        responseDto.setZipCode(hotel.getZipCode());
        responseDto.setPhone(hotel.getPhone());
        responseDto.setRating(hotel.getRating());
        List<RoomResponseDto> roomsDtos =
                hotel.getRooms()
                        .stream()
                        .map(RoomMapper::toResponse)
                        .toList();
        responseDto.setRooms(roomsDtos);
        return responseDto;

    }


}
