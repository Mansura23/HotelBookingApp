package org.ironhack.hotelbookingapp.mapper;
import org.ironhack.hotelbookingapp.dto.request.UserRequestDto;
import org.ironhack.hotelbookingapp.dto.response.BookingResponseDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseForBooking;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setRole(Role.USER);
        user.setBalance(BigDecimal.ZERO);
        user.setNumber(userRequestDto.getNumber());
        user.setPassword(userRequestDto.getPassword());
        user.setStatus(Status.ACTIVE);

        return user;
    }

    public static UserResponseDto userToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setNumber(user.getNumber());
        userResponseDto.setStatus(user.getStatus());
        userResponseDto.setBalance(user.getBalance());

        return userResponseDto;
    }
    public static List<UserResponseForBooking> toResponseList(List<User> users) {
        return users.stream().map(UserMapper::toResponseDtoForBooking).collect(Collectors.toList());
    }

    public static UserResponseForBooking toResponseDtoForBooking(User user) {
       UserResponseForBooking userResponseForBooking = new UserResponseForBooking();
       userResponseForBooking.setId(user.getId());
        userResponseForBooking.setFirstName(user.getFirstName());
        userResponseForBooking.setLastName(user.getLastName());
        userResponseForBooking.setEmail(user.getEmail());
        userResponseForBooking.setRole(user.getRole());
        userResponseForBooking.setNumber(user.getNumber());
        userResponseForBooking.setBalance(user.getBalance());
        userResponseForBooking.setStatus(user.getStatus());
        List<BookingResponseDto> bookingDtos =
                user.getBookings()
                        .stream()
                        .map(BookingMapper::fromBooking)
                        .toList();

        userResponseForBooking.setBookings(bookingDtos);
        return userResponseForBooking;
    }

}
