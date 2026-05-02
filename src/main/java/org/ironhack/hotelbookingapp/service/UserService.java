package org.ironhack.hotelbookingapp.service;

import jakarta.transaction.Transactional;
import org.ironhack.hotelbookingapp.dto.request.AdminUpdateRequestDto;
import org.ironhack.hotelbookingapp.dto.request.LoginRequest;
import org.ironhack.hotelbookingapp.dto.request.UpdateUserRequestDto;
import org.ironhack.hotelbookingapp.dto.request.UserRequestDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseForBooking;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.entity.UserPrincipal;
import org.ironhack.hotelbookingapp.enums.Status;
import org.ironhack.hotelbookingapp.exception.InvalidCredantialsException;
import org.ironhack.hotelbookingapp.exception.UserExistsException;
import org.ironhack.hotelbookingapp.exception.UserNotActiveException;
import org.ironhack.hotelbookingapp.exception.UserNotFoundException;
import org.ironhack.hotelbookingapp.mapper.UserMapper;
import org.ironhack.hotelbookingapp.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private  final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        if(userRepository.findByEmail(userRequestDto.getEmail())!=null) {
            throw new UserExistsException("User with email already exists");
        }
        User user = UserMapper.toEntity(userRequestDto);
        user.setPassword(Objects.requireNonNull(encoder.encode(user.getPassword())));
        userRepository.save(user);
        return UserMapper.userToUserResponseDto(user);
    }

//    public String loginUser(UserRequestDto userRequestDto) {
//        User user=UserMapper.toEntity(userRequestDto);
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
//        if (authentication.isAuthenticated()) {
//            User dbUser = userRepository.findByEmail(user.getEmail());
//            return jwtService.generateToken(dbUser.getEmail());
//        } else {
//            return "fail";
//        }
//
//    }


    public String loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            throw new InvalidCredantialsException("Invalid credentials");
        }

        if (user.getStatus() != Status.ACTIVE) {
            throw new UserNotActiveException("User account is inactive. Please contact support.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = new UserPrincipal(user);
            return jwtService.generateToken(userDetails);
        }

        throw new InvalidCredantialsException("Invalid credentials");
    }
    @Transactional
    public  User findById(Long id) {
       return userRepository.findById(id)
               .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    // USER
    public UserResponseDto updateMe(UpdateUserRequestDto dto, String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtService.extractUserName(token);
        User user = userRepository.findByEmail(email);
        if (user.getStatus()!=Status.ACTIVE) {
            throw new UserNotActiveException("User is not active");
        }
        if(dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if(dto.getLastName() != null) user.setLastName(dto.getLastName());
        if(dto.getNumber() != null) user.setNumber(dto.getNumber());

        if(dto.getPassword() != null) {
            user.setPassword(encoder.encode(dto.getPassword()));
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
            user.setPassword(encoder.encode(dto.getPassword()));
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
        if(user.getStatus()!=Status.ACTIVE) {
            throw new UserNotActiveException("User is not active");
        }
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        return UserMapper.userToUserResponseDto(user);
    }

    public List<UserResponseForBooking> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDtoForBooking)
                .toList();
    }

    public UserResponseForBooking getMyProfile() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return UserMapper.toResponseDtoForBooking(user);
    }




}
