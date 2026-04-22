package org.ironhack.hotelbookingapp.controller;

import jakarta.validation.Valid;
import org.ironhack.hotelbookingapp.dto.RoomRequestDto;
import org.ironhack.hotelbookingapp.dto.RoomRequestUpdateDto;
import org.ironhack.hotelbookingapp.dto.RoomResponseDto;
import org.ironhack.hotelbookingapp.repository.RoomRepository;
import org.ironhack.hotelbookingapp.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomResponseDto>  create(@Valid @RequestBody RoomRequestDto request){

        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.create(request));
    }

    @GetMapping
    public List<RoomResponseDto> findAll(){
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    public RoomResponseDto findById(@PathVariable Long id){
        return roomService.findById(id);
    }

    @GetMapping("/hotel/{hotelId}")
    public List<RoomResponseDto> findByHotelId(@PathVariable Long hotelId){
        return roomService.findAllByHotelId(hotelId);
    }

    @PatchMapping("/{id}")
    public RoomResponseDto update(@PathVariable Long id, @Valid @RequestBody RoomRequestUpdateDto request){
        return roomService.update(id,request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
