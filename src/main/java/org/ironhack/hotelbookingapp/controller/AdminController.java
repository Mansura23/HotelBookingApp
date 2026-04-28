package org.ironhack.hotelbookingapp.controller;

import org.ironhack.hotelbookingapp.dto.request.AdminUpdateRequestDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseDto;
import org.ironhack.hotelbookingapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // admin
    @PutMapping("/{id}")
    public UserResponseDto updateByAdmin(@PathVariable Long id,
                                         @RequestBody AdminUpdateRequestDto dto) {
        return userService.updateByAdmin(id, dto);
    }


    @DeleteMapping("/{id}")
    UserResponseDto deleteUser(@PathVariable Long id) {
        return userService.softDeleteUserById(id);
    }
    @PutMapping("{id}/balance")
    public UserResponseDto addBalance(@PathVariable Long id,
                                      @RequestParam BigDecimal balance) {
        return userService.addBalance(id, balance);
    }


}
