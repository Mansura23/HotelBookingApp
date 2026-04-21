package org.ironhack.hotelbookingapp.repository;

import org.ironhack.hotelbookingapp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

}
