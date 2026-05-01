package org.ironhack.hotelbookingapp.repository;

import org.ironhack.hotelbookingapp.entity.Booking;
import org.ironhack.hotelbookingapp.entity.Room;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);

    List<Booking> findByRoom(Room room);

    boolean existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThanAndBookingStatusNot(
            Room room,
            LocalDate checkOutDate,
            LocalDate checkInDate,
            BookingStatus status
    );

    boolean existsByRoomAndIdNotAndCheckInDateLessThanAndCheckOutDateGreaterThanAndBookingStatusNot(
            Room room,
            Long id,
            LocalDate checkOutDate,
            LocalDate checkInDate,
            BookingStatus status
    );

    List<Booking> findByUserAndBookingStatus(User user, BookingStatus status);

    List<Booking> findByCheckInDateBetween(LocalDate start, LocalDate end);

    Optional<Booking> findByIdAndUser(Long id, User user);
}
