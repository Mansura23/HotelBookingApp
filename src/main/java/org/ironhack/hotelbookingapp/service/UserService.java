package org.ironhack.hotelbookingapp.service;

import jakarta.transaction.Transactional;
import org.ironhack.hotelbookingapp.dto.request.AdminUpdateRequestDto;
import org.ironhack.hotelbookingapp.dto.request.UpdateUserRequestDto;
import org.ironhack.hotelbookingapp.dto.request.UserRequestDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseDto;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.enums.Status;
import org.ironhack.hotelbookingapp.exception.UserNotFoundException;
import org.ironhack.hotelbookingapp.mapper.UserMapper;
import org.ironhack.hotelbookingapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        if(userRepository.findByEmail(userRequestDto.getEmail())!=null) {
            throw new RuntimeException("Email already exists");
        }
        User user = UserMapper.toEntity(userRequestDto);
        userRepository.save(user);
        return UserMapper.userToUserResponseDto(user);
    }

//    public UserResponseDto loginUser(UserRequestDto userRequestDto) {}
    @Transactional
    public  User findById(Long id) {
       return userRepository.findById(id)
               .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // USER
    @Transactional
    public UserResponseDto updateMe(Long userId,
                                    UpdateUserRequestDto dto) {
        User user=findById(userId);
        if(dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if(dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if(dto.getNumber() != null) {
            user.setNumber(dto.getNumber());
        }
        if(dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
        userRepository.save(user);
        return UserMapper.userToUserResponseDto(user);
    }
    // ADMIN
    @Transactional
    public UserResponseDto updateByAdmin(Long id,
                                         AdminUpdateRequestDto dto) {
        User user=findById(id);
        if(dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }

        if(dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        if(dto.getNumber() != null) {
            user.setNumber(dto.getNumber());
        }

        if(dto.getPassword() != null) {
            user.setPassword(dto.getPassword()); //encode
        }

        if(dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        if(dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }

        if(dto.getBalance() != null) {
            user.setBalance(dto.getBalance());
        }
        userRepository.save(user);
        return UserMapper.userToUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto softDeleteUserById(Long id) {
        User user = findById(id);
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
        return UserMapper.userToUserResponseDto(user);

    }

    @Transactional
    public UserResponseDto addBalance(Long userId, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        User user = findById(userId);
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        return UserMapper.userToUserResponseDto(user);
    }


}
