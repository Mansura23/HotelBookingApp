package org.ironhack.hotelbookingapp.repository;

import org.ironhack.hotelbookingapp.entity.Room;
import org.ironhack.hotelbookingapp.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByPricePerNightBetween(double min,double max);

    List<Room> findByType(RoomType roomType);

    List<Room> findByHotelId(Long hotelId);

    boolean existsByAvailableAndType(boolean available, RoomType roomType);

    List<Room> findByAvailableTrue();

    List<Room> findByHotelIdAndAvailableTrue(Long hotelId);

    List<Room> findByHotelIdAndTypeAndAvailableTrue(Long hotelId,RoomType roomType);
}
