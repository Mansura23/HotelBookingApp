package org.ironhack.hotelbookingapp.controller;

import jakarta.validation.Valid;
import org.ironhack.hotelbookingapp.dto.request.UpdateUserRequestDto;
import org.ironhack.hotelbookingapp.dto.request.UserRequestDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseDto;
import org.ironhack.hotelbookingapp.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    UserResponseDto createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.registerUser(userRequestDto);
    }

    @PutMapping("/me/{id}")
    public UserResponseDto updateMe(@PathVariable Long id,
                                    @RequestBody UpdateUserRequestDto dto) {
        return userService.updateMe(id, dto);
    }

}
