package org.ironhack.hotelbookingapp.service;

import org.ironhack.hotelbookingapp.dto.RoomRequestDto;
import org.ironhack.hotelbookingapp.dto.RoomRequestUpdateDto;
import org.ironhack.hotelbookingapp.dto.RoomResponseDto;
import org.ironhack.hotelbookingapp.entity.Hotel;
import org.ironhack.hotelbookingapp.entity.Room;
import org.ironhack.hotelbookingapp.mapper.RoomMapper;
import org.ironhack.hotelbookingapp.repository.HotelRepository;
import org.ironhack.hotelbookingapp.repository.RoomRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository,  HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    public RoomResponseDto findById(Long id){
        Room room=roomRepository.findById(id)
                .orElseThrow(()->  new RuntimeException("Room not found"));

        return RoomMapper.toResponse(room);
    }

    public List<RoomResponseDto> findAll(){
        List<Room> rooms=roomRepository.findAll();

        return RoomMapper.toResponseList(rooms);
    }

    public RoomResponseDto create(RoomRequestDto request){
        Hotel hotel=hotelRepository.findById(request.getHotelId())
                .orElseThrow(()->  new RuntimeException("Hotel not found"));

        Room room=RoomMapper.toEntity(request,hotel);

        Room savedRoom=roomRepository.save(room);

        return RoomMapper.toResponse(savedRoom);
    }

    public RoomResponseDto update(Long id,RoomRequestUpdateDto request){
        Room room=roomRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Room not found"));

        if(request.getHotelId()!=null){
            Hotel hotel=hotelRepository.findById(request.getHotelId())
                    .orElseThrow(()->  new RuntimeException("Hotel not found"));

            room.setHotel(hotel);
        }

        if(request.getRoomNumber()!=null){
            room.setRoomNumber(request.getRoomNumber());
        }

        if(request.getType()!=null){
            room.setType(request.getType());
        }

        if(request.getPricePerNight()!=null){
            room.setPricePerNight(request.getPricePerNight());
        }

        Room updatedRoom=roomRepository.save(room);


        return RoomMapper.toResponse(updatedRoom);
    }

    public void delete(Long id){
        if(!roomRepository.existsById(id)){
            throw new RuntimeException("Room not found");
        }

        roomRepository.deleteById(id);
    }

}
