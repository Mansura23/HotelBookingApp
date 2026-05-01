package org.ironhack.hotelbookingapp.service;

import org.ironhack.hotelbookingapp.dto.request.RoomRequestDto;
import org.ironhack.hotelbookingapp.dto.request.RoomRequestUpdateDto;
import org.ironhack.hotelbookingapp.dto.response.RoomResponseDto;
import org.ironhack.hotelbookingapp.entity.Hotel;
import org.ironhack.hotelbookingapp.entity.Room;
import org.ironhack.hotelbookingapp.exception.HotelNotFound;
import org.ironhack.hotelbookingapp.exception.RoomExistsException;
import org.ironhack.hotelbookingapp.exception.RoomNotFound;
import org.ironhack.hotelbookingapp.mapper.RoomMapper;
import org.ironhack.hotelbookingapp.repository.HotelRepository;
import org.ironhack.hotelbookingapp.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(()->  new RoomNotFound("Room",id));

        return RoomMapper.toResponse(room);
    }

    public List<RoomResponseDto> findAll(){
        List<Room> rooms=roomRepository.findAll();

        return RoomMapper.toResponseList(rooms);
    }

    public RoomResponseDto create(RoomRequestDto request){
        Hotel hotel=hotelRepository.findById(request.getHotelId())
                .orElseThrow(()->  new HotelNotFound("Hotel",request.getHotelId()));

        if(roomRepository.existsByRoomNumber(request.getRoomNumber())){
            throw new RoomExistsException("Room number already exists");
        }

        Room room=RoomMapper.toEntity(request,hotel);

        Room savedRoom=roomRepository.save(room);

        return RoomMapper.toResponse(savedRoom);
    }

    @Transactional
    public RoomResponseDto update(Long id,RoomRequestUpdateDto request){
        Room room=roomRepository.findById(id)
                .orElseThrow(()->new RoomNotFound("Room",id));

        if(request.getHotelId()!=null){
            Hotel hotel=hotelRepository.findById(request.getHotelId())
                    .orElseThrow(()->  new HotelNotFound("Hotel",id));

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

    @Transactional
    public RoomResponseDto updateFull(Long id,RoomRequestDto request){
        Room room=roomRepository.findById(id)
                .orElseThrow(()->new RoomNotFound("Room",id));

        room.setType(request.getType());
        room.setPricePerNight(request.getPricePerNight());
        room.setRoomNumber(request.getRoomNumber());

        Hotel hotel=hotelRepository.findById(request.getHotelId())
                .orElseThrow(()->  new HotelNotFound("Hotel",id));
        room.setHotel(hotel);

        Room updatedRoom=roomRepository.save(room);

        return RoomMapper.toResponse(updatedRoom);
    }

    @Transactional
    public void delete(Long id){
        if(!roomRepository.existsById(id)){
            throw new RoomNotFound("Room",id);
        }

        roomRepository.deleteById(id);
    }

    public List<RoomResponseDto> findAllByHotelId(Long hotelId){
        List<Room> rooms=roomRepository.findByHotelId(hotelId);

        return RoomMapper.toResponseList(rooms);
    }

}
