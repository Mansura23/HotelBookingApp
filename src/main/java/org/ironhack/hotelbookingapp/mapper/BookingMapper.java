package org.ironhack.hotelbookingapp.mapper;

import org.ironhack.hotelbookingapp.dto.response.BookingResponseDto;
import org.ironhack.hotelbookingapp.entity.Booking;

public class BookingMapper {
    public static BookingResponseDto fromBooking(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setBookingStatus(booking.getBookingStatus());
        bookingResponseDto.setCheckOutDate(booking.getCheckOutDate());
        bookingResponseDto.setCheckInDate(booking.getCheckInDate());
        bookingResponseDto.setTotalPrice(booking.getTotalPrice());
        bookingResponseDto.setRoomId(booking.getRoom().getId());
        return bookingResponseDto;
    }
}
