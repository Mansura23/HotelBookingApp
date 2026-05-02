package org.ironhack.hotelbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.hotelbookingapp.dto.response.UserResponseDto;
import org.ironhack.hotelbookingapp.dto.response.UserResponseForBooking;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;
import org.ironhack.hotelbookingapp.exception.UserNotFoundException;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTService jwtService;

    // ───────── Helper ─────────

    private UserResponseDto buildUserResponse(Long id, String firstName, Status status) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setFirstName(firstName);
        dto.setLastName("Mammadov");
        dto.setEmail("user@example.com");
        dto.setNumber("+994501234567");
        dto.setRole(Role.USER);
        dto.setStatus(status);
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

    private String buildAdminUpdateJson(String firstName, String role, String status) {
        return """
                {
                    "firstName": "%s",
                    "lastName": "Mammadov",
                    "number": "+994501234567",
                    "password": "secret1",
                    "role": "%s",
                    "status": "%s",
                    "balance": "100.00"
                }
                """.formatted(firstName, role, status);
    }

    // ───────── PUT /admin/{id} ─────────

    @Test
    void updateByAdmin_validRequest_returns200() throws Exception {
        Mockito.when(userService.updateByAdmin(eq(1L), any()))
                .thenReturn(buildUserResponse(1L, "Anar", Status.ACTIVE));

        mockMvc.perform(put("/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildAdminUpdateJson("Anar", "USER", "ACTIVE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Anar"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        Mockito.verify(userService).updateByAdmin(eq(1L), any());
    }

    @Test
    void updateByAdmin_userNotFound_returns404() throws Exception {
        Mockito.when(userService.updateByAdmin(eq(9999L), any()))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/admin/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildAdminUpdateJson("Anar", "USER", "ACTIVE")))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateByAdmin_blankFirstName_returns400() throws Exception {
        String invalidBody = """
                {
                    "firstName": "",
                    "lastName": "Mammadov",
                    "number": "+994501234567",
                    "password": "secret1",
                    "role": "USER",
                    "status": "ACTIVE",
                    "balance": "100.00"
                }
                """;

        mockMvc.perform(put("/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(userService);
    }

    // ───────── DELETE /admin/{id} ─────────

    @Test
    void deleteUser_existingId_returns200() throws Exception {
        Mockito.when(userService.softDeleteUserById(1L))
                .thenReturn(buildUserResponse(1L, "Anar", Status.INACTIVE));

        mockMvc.perform(delete("/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));

        Mockito.verify(userService).softDeleteUserById(1L);
    }

    @Test
    void deleteUser_notFound_returns404() throws Exception {
        Mockito.when(userService.softDeleteUserById(9999L))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(delete("/admin/9999"))
                .andExpect(status().isNotFound());

        Mockito.verify(userService).softDeleteUserById(9999L);
    }

    // ───────── PUT /admin/{id}/balance ─────────

    @Test
    void addBalance_validAmount_returns200() throws Exception {
        UserResponseDto response = buildUserResponse(1L, "Anar", Status.ACTIVE);
        response.setBalance(new BigDecimal("250.00"));

        Mockito.when(userService.addBalance(eq(1L), eq(new BigDecimal("150.00"))))
                .thenReturn(response);

        mockMvc.perform(put("/admin/1/balance")
                        .param("balance", "150.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(250.00));

        Mockito.verify(userService).addBalance(eq(1L), eq(new BigDecimal("150.00")));
    }

    @Test
    void addBalance_userNotFound_returns404() throws Exception {
        Mockito.when(userService.addBalance(eq(9999L), any()))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/admin/9999/balance")
                        .param("balance", "150.00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addBalance_negativeAmount_returns500() throws Exception {
        Mockito.when(userService.addBalance(eq(1L), eq(new BigDecimal("-50.00"))))
                .thenThrow(new RuntimeException("Amount must be positive"));

        mockMvc.perform(put("/admin/1/balance")
                        .param("balance", "-50.00"))
                .andExpect(status().isInternalServerError());
    }

    // ───────── GET /admin ─────────

    @Test
    void getAllUsers_returnsOk() throws Exception {
        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(
                        buildUserForBooking(1L, "Anar"),
                        buildUserForBooking(2L, "Leyla")
                ));

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Anar"))
                .andExpect(jsonPath("$[1].firstName").value("Leyla"));

        Mockito.verify(userService).getAllUsers();
    }

    @Test
    void getAllUsers_emptyList_returnsOk() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
