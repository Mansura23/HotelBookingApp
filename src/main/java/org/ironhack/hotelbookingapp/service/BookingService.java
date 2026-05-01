package org.ironhack.hotelbookingapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ironhack.hotelbookingapp.dto.request.BookingRequestDto;
import org.ironhack.hotelbookingapp.dto.response.BookingResponseDto;
import org.ironhack.hotelbookingapp.entity.Booking;
import org.ironhack.hotelbookingapp.entity.Room;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.entity.UserPrincipal;
import org.ironhack.hotelbookingapp.enums.BookingStatus;
import org.ironhack.hotelbookingapp.enums.Status;
import org.ironhack.hotelbookingapp.exception.*;
import org.ironhack.hotelbookingapp.mapper.BookingMapper;
import org.ironhack.hotelbookingapp.repository.BookingRepository;
import org.ironhack.hotelbookingapp.repository.RoomRepository;
import org.ironhack.hotelbookingapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email);

        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }
        /// //
        if (user.getStatus() != Status.ACTIVE) {
            throw new UserNotActiveException("User is not active");
        }
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RoomNotFound("Room not found"));


        if (dto.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidCheckInDateException("Invalid check-in date");
        }
        long days = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());

        if (days <= 0) {
            throw new InvalidDateException("Invalid date range");
        }
        boolean conflict = bookingRepository
                .existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThanAndBookingStatusNot(
                        room,
                        dto.getCheckOutDate(),
                        dto.getCheckInDate(),
                        BookingStatus.CANCELLED
                );

        if (conflict) {
            throw new RoomExistsException("Room already booked for these dates");
        }

        BigDecimal totalPrice = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(days));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.PENDING);

        Booking booking1=bookingRepository.save(booking);

        return BookingMapper.fromBooking(booking1);
    }

    public BookingResponseDto updateBookingUser(Long id, BookingRequestDto dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Booking booking = bookingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new BookingNotFound("Booking not found for this user"));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new CancelledCanNotUpDateException("Cancelled booking cannot be updated");
        }

        if (dto.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidCheckInDateException("Invalid check-in date");
        }

        long newDays = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        if (newDays <= 0) {
            throw new InvalidDateException("Invalid date range");
        }

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {

            if (LocalDate.now().plusDays(1).isAfter(booking.getCheckInDate())) {
                throw new InvalidDateException("Too late to update confirmed booking");
            }
            long oldDays = ChronoUnit.DAYS.between(
                    booking.getCheckInDate(),
                    booking.getCheckOutDate()
            );

            if (oldDays != newDays) {
                throw new CanNotChangeDurationException("You cannot change duration of confirmed booking");
            }
        }
        boolean conflict = bookingRepository
                .existsByRoomAndIdNotAndCheckInDateLessThanAndCheckOutDateGreaterThanAndBookingStatusNot(
                        booking.getRoom(),
                        booking.getId(),
                        dto.getCheckOutDate(),
                        dto.getCheckInDate(),
                        BookingStatus.CANCELLED
                );

        if (conflict) {
            throw new RoomExistsException("Room already booked for these dates");
        }

        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());

        BigDecimal newPrice = booking.getRoom().getPricePerNight()
                .multiply(BigDecimal.valueOf(newDays));

        booking.setTotalPrice(newPrice);

        bookingRepository.save(booking);

        return BookingMapper.fromBooking(booking);
    }
    @Transactional
    public void cancel(Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        Booking booking = bookingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new BookingNotFound("Not your booking"));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }

        // PENDING →  cancel
        if (booking.getBookingStatus() == BookingStatus.PENDING) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return;
        }

        // CONFIRMED → refund check
        if (LocalDate.now().plusDays(1).isAfter(booking.getCheckInDate())) {
            throw new CanNotCancelledException("Too late to cancel");
        }

        //  refund
        paymentService.refund(booking);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }


}