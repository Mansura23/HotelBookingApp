package org.ironhack.hotelbookingapp.service;

import org.ironhack.hotelbookingapp.dto.request.HotelRequestDto;
import org.ironhack.hotelbookingapp.dto.request.HotelRequestUpdateDto;
import org.ironhack.hotelbookingapp.dto.response.HotelResponseDto;
import org.ironhack.hotelbookingapp.dto.response.HotelResponseDtoForUser;
import org.ironhack.hotelbookingapp.entity.Hotel;
import org.ironhack.hotelbookingapp.exception.HotelExistsException;
import org.ironhack.hotelbookingapp.exception.HotelNotFound;
import org.ironhack.hotelbookingapp.mapper.HotelMapper;
import org.ironhack.hotelbookingapp.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel findByIdOrThrow(long id) {
        return hotelRepository.findById(id)
                .orElseThrow(()-> new HotelNotFound("Hotel with id " + id + " not found"));
    }

    public HotelResponseDto create(HotelRequestDto requestDto) {
        Hotel hotel = HotelMapper.toModel(requestDto);
        if(hotelRepository.existsByName(requestDto.getName())) {
            throw new HotelExistsException("Hotel with id " + hotel.getId() + " already exists");
        }
        Hotel savedHotel = hotelRepository.save(hotel);
        return HotelMapper.toResponseDto(savedHotel);
    }

    public HotelResponseDto fullUpdate(Long id, HotelRequestDto requestDto) {
        Hotel hotel = findByIdOrThrow(id);

        hotel.setName(requestDto.getName());
        hotel.setDescription(requestDto.getDescription());
        hotel.setCountry(requestDto.getCountry());
        hotel.setCity(requestDto.getCity());
        hotel.setAddress(requestDto.getAddress());
        hotel.setZipCode(requestDto.getZipCode());
        hotel.setPhone(requestDto.getPhone());
        hotel.setRating(requestDto.getRating());

        Hotel updatedHotel = hotelRepository.save(hotel);
        return HotelMapper.toResponseDto(updatedHotel);
    }

    public HotelResponseDto partialUpdate(Long id, HotelRequestUpdateDto updateDto) {
        Hotel hotel = findByIdOrThrow(id);

        HotelMapper.updateModel(updateDto, hotel);

        Hotel updatedHotel = hotelRepository.save(hotel);
        return HotelMapper.toResponseDto(updatedHotel);
    }

    public HotelResponseDto getById(Long id) {
        Hotel hotel = findByIdOrThrow(id);
        return HotelMapper.toResponseDto(hotel);
    }

    public List<HotelResponseDtoForUser> getAll(){
        return HotelMapper.toResponseList(hotelRepository.findAll());
    }

    public void delete(Long id) {
        Hotel hotel = findByIdOrThrow(id);
        hotelRepository.delete(hotel);
    }
}
