package org.ironhack.hotelbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.hotelbookingapp.dto.response.UserResponseDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseForBooking;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;
import org.ironhack.hotelbookingapp.exception.InvalidCredantialsException;
import org.ironhack.hotelbookingapp.exception.UserExistsException;
import org.ironhack.hotelbookingapp.exception.UserNotActiveException;
import org.ironhack.hotelbookingapp.service.JWTService;
import org.ironhack.hotelbookingapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WithMockUser(username = "user@example.com", roles = {"USER"})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTService jwtService;

    // ───────── Helper ─────────

    private UserResponseDto buildUserResponse(Long id, String firstName) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setFirstName(firstName);
        dto.setLastName("Mammadov");
        dto.setEmail("user@example.com");
        dto.setNumber("+994501234567");
        dto.setRole(Role.USER);
        dto.setStatus(Status.ACTIVE);
        dto.setBalance(new BigDecimal("100.00"));
        return dto;
    }

    private UserResponseForBooking buildUserForBooking(Long id, String firstName) {
        UserResponseForBooking dto = new UserResponseForBooking();
        dto.setId(id);
        dto.setFirstName(firstName);
        dto.setLastName("Mammadov");
        dto.setEmail("user@example.com");
        return dto;
    }

    // ───────── POST /users/register ─────────

    @Test
    void register_validRequest_returns200() throws Exception {
        Mockito.when(userService.registerUser(any()))
                .thenReturn(buildUserResponse(1L, "Anar"));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Anar",
                                    "lastName": "Mammadov",
                                    "email": "user@example.com",
                                    "number": "+994501234567",
                                    "password": "secret1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Anar"))
                .andExpect(jsonPath("$.email").value("user@example.com"));

        Mockito.verify(userService).registerUser(any());
    }

    @Test
    void register_duplicateEmail_returns409() throws Exception {
        Mockito.when(userService.registerUser(any()))
                .thenThrow(new UserExistsException("User with email already exists"));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Anar",
                                    "lastName": "Mammadov",
                                    "email": "user@example.com",
                                    "number": "+994501234567",
                                    "password": "secret1"
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void register_blankFirstName_returns400() throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "",
                                    "lastName": "Mammadov",
                                    "email": "user@example.com",
                                    "number": "+994501234567",
                                    "password": "secret1"
                                }
                                """))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void register_invalidEmailFormat_returns400() throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Anar",
                                    "lastName": "Mammadov",
                                    "email": "not-an-email",
                                    "number": "+994501234567",
                                    "password": "secret1"
                                }
                                """))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(userService);
    }

    // ───────── POST /users/login ─────────

    @Test
    void login_validCredentials_returnsToken() throws Exception {
        Mockito.when(userService.loginUser(any())).thenReturn("mocked.jwt.token");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "user@example.com",
                                    "password": "secret1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked.jwt.token"));

        Mockito.verify(userService).loginUser(any());
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception {
        Mockito.when(userService.loginUser(any()))
                .thenThrow(new InvalidCredantialsException("Invalid credentials"));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "user@example.com",
                                    "password": "wrongpass"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_inactiveUser_returns403() throws Exception {
        Mockito.when(userService.loginUser(any()))
                .thenThrow(new UserNotActiveException("User account is inactive."));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "user@example.com",
                                    "password": "secret1"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void login_invalidEmailFormat_returns400() throws Exception {
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "not-an-email",
                                    "password": "secret1"
                                }
                                """))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(userService);
    }

    // ───────── PUT /users/me ─────────

    @Test
    void updateMe_validRequest_returns200() throws Exception {
        Mockito.when(userService.updateMe(any(), eq("Bearer mocked.jwt.token")))
                .thenReturn(buildUserResponse(1L, "Leyla"));

        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer mocked.jwt.token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Leyla",
                                    "lastName": "Mammadova",
                                    "number": "+994559998877",
                                    "password": "newpass1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Leyla"));

        Mockito.verify(userService).updateMe(any(), eq("Bearer mocked.jwt.token"));
    }

    @Test
    void updateMe_inactiveUser_returns403() throws Exception {
        Mockito.when(userService.updateMe(any(), any()))
                .thenThrow(new UserNotActiveException("User is not active"));

        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer mocked.jwt.token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Leyla",
                                    "lastName": "Mammadova",
                                    "number": "+994559998877",
                                    "password": "newpass1"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateMe_blankFirstName_returns400() throws Exception {
        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer mocked.jwt.token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "",
                                    "lastName": "Mammadova",
                                    "number": "+994559998877",
                                    "password": "newpass1"
                                }
                                """))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(userService);
    }

    // ───────── GET /users/me ─────────

    @Test
    void getMyProfile_returnsOk() throws Exception {
        Mockito.when(userService.getMyProfile())
                .thenReturn(buildUserForBooking(1L, "Anar"));

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Anar"))
                .andExpect(jsonPath("$.email").value("user@example.com"));

        Mockito.verify(userService).getMyProfile();
    }
}