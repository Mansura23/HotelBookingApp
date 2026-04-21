package org.ironhack.hotelbookingapp.service;

import org.ironhack.hotelbookingapp.repository.RoomRepository;
import org.springframework.stereotype.Service;


@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


}
