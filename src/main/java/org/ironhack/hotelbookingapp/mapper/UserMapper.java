package org.ironhack.hotelbookingapp.mapper;
import org.ironhack.hotelbookingapp.dto.request.UserRequestDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseDto;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;

import java.math.BigDecimal;

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

}
