package org.ironhack.hotelbookingapp.controller;

import lombok.RequiredArgsConstructor;
import org.ironhack.hotelbookingapp.dto.request.BookingRequestDto;
import org.ironhack.hotelbookingapp.dto.response.BookingResponseDto;
import org.ironhack.hotelbookingapp.service.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto create(@RequestBody BookingRequestDto dto) {
        return bookingService.createBooking(dto);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        bookingService.cancel(id);
    }
}