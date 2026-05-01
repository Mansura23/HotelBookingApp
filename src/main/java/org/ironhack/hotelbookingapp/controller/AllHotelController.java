package org.ironhack.hotelbookingapp.controller;

import org.ironhack.hotelbookingapp.dto.response.HotelResponseDtoForUser;
import org.ironhack.hotelbookingapp.service.HotelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class AllHotelController {
    private  final HotelService hotelService;

    public AllHotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<HotelResponseDtoForUser> getAllHotels() {
        return hotelService.getAll();
    }


}
