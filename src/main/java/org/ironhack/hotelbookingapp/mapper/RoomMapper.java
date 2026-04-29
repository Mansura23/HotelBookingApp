package org.ironhack.hotelbookingapp.mapper;

import org.ironhack.hotelbookingapp.dto.request.RoomRequestDto;
import org.ironhack.hotelbookingapp.dto.response.RoomResponseDto;
import org.ironhack.hotelbookingapp.entity.Hotel;
import org.ironhack.hotelbookingapp.entity.Room;

import java.util.List;

public class RoomMapper {

    public static Room toEntity(RoomRequestDto request, Hotel hotel) {
        Room room = new Room();

        room.setRoomNumber(request.getRoomNumber());
        room.setType(request.getType());
        room.setPricePerNight(request.getPricePerNight());
        room.setHotel(hotel);
        room.setAvailable(true);

        return room;
    }

    public static RoomResponseDto toResponse(Room room){
        RoomResponseDto response = new RoomResponseDto();

        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setType(room.getType());
        response.setPricePerNight(room.getPricePerNight());
        response.setAvailable(room.isAvailable());
        response.setHotelId(room.getHotel().getId());
        response.setHotelName(room.getHotel().getName());

        return response;
    }

    public static List<RoomResponseDto> toResponseList(List<Room> roomList){

        return roomList
                .stream()
                .map(RoomMapper::toResponse)
                .toList();

    }
}
