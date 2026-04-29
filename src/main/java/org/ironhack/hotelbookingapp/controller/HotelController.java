package org.ironhack.hotelbookingapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironhack.hotelbookingapp.dto.request.HotelRequestDto;
import org.ironhack.hotelbookingapp.dto.request.HotelRequestUpdateDto;
import org.ironhack.hotelbookingapp.dto.response.HotelResponseDto;
import org.ironhack.hotelbookingapp.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelResponseDto createHotel(@Valid @RequestBody HotelRequestDto requestDto) {
        return hotelService.create(requestDto);
    }

    @PutMapping("/{id}")
    public HotelResponseDto fullUpdateHotel(@PathVariable Long id,
                                            @Valid @RequestBody HotelRequestDto requestDto) {
        return hotelService.fullUpdate(id, requestDto);
    }

    @PatchMapping("/{id}")
    public HotelResponseDto partialUpdateHotel(@PathVariable Long id,
                                               @Valid @RequestBody HotelRequestUpdateDto updateDto) {
        return hotelService.partialUpdate(id, updateDto);
    }

    @GetMapping("/{id}")
    public HotelResponseDto getHotelById(@PathVariable Long id) {
        return hotelService.getById(id);
    }

    @GetMapping
    public List<HotelResponseDto> getAllHotels() {
        return hotelService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@PathVariable Long id) {
        hotelService.delete(id);
    }
}
