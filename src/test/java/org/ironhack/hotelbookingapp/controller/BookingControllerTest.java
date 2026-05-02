package org.ironhack.hotelbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.hotelbookingapp.dto.response.BookingResponseDto;
import org.ironhack.hotelbookingapp.enums.BookingStatus;
import org.ironhack.hotelbookingapp.exception.BookingNotFound;
import org.ironhack.hotelbookingapp.exception.CanNotCancelledException;
import org.ironhack.hotelbookingapp.exception.RoomExistsException;
import org.ironhack.hotelbookingapp.exception.RoomNotFound;
import org.ironhack.hotelbookingapp.service.BookingService;
import org.ironhack.hotelbookingapp.service.JWTService;
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
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WithMockUser(username = "user@example.com", roles = {"USER"})
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private JWTService jwtService;

    // ───────── Helper ─────────

    private BookingResponseDto buildResponse(Long id, BookingStatus status) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(id);
        dto.setRoomId(1L);
        dto.setCheckInDate(LocalDate.now().plusDays(1));
        dto.setCheckOutDate(LocalDate.now().plusDays(3));
        dto.setTotalPrice(new BigDecimal("300.00"));
        dto.setBookingStatus(status);
        return dto;
    }

    private String buildBookingJson(String checkIn, String checkOut) {
        return """
                {
                    "roomId": 1,
                    "checkInDate": "%s",
                    "checkOutDate": "%s"
                }
                """.formatted(checkIn, checkOut);
    }

    // ───────── POST /bookings ─────────

    @Test
    void createBooking_validRequest_returns200() throws Exception {
        Mockito.when(bookingService.createBooking(any()))
                .thenReturn(buildResponse(1L, BookingStatus.PENDING));

        String checkIn = LocalDate.now().plusDays(1).toString();
        String checkOut = LocalDate.now().plusDays(3).toString();

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildBookingJson(checkIn, checkOut)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomId").value(1))
                .andExpect(jsonPath("$.bookingStatus").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(300.00));

        Mockito.verify(bookingService).createBooking(any());
    }

    @Test
    void createBooking_roomNotFound_returns404() throws Exception {
        Mockito.when(bookingService.createBooking(any()))
                .thenThrow(new RoomNotFound("Room not found"));

        String checkIn = LocalDate.now().plusDays(1).toString();
        String checkOut = LocalDate.now().plusDays(3).toString();

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildBookingJson(checkIn, checkOut)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBooking_roomAlreadyBooked_returns409() throws Exception {
        Mockito.when(bookingService.createBooking(any()))
                .thenThrow(new RoomExistsException("Room already booked for these dates"));

        String checkIn = LocalDate.now().plusDays(1).toString();
        String checkOut = LocalDate.now().plusDays(3).toString();

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildBookingJson(checkIn, checkOut)))
                .andExpect(status().isConflict());
    }

    @Test
    void createBooking_nullRoomId_returns400() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomId": null,
                                    "checkInDate": "%s",
                                    "checkOutDate": "%s"
                                }
                                """.formatted(
                                LocalDate.now().plusDays(1),
                                LocalDate.now().plusDays(3))))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(bookingService);
    }

    @Test
    void createBooking_pastCheckInDate_returns400() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildBookingJson(
                                LocalDate.now().minusDays(1).toString(),
                                LocalDate.now().plusDays(2).toString())))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(bookingService);
    }

    @Test
    void createBooking_pastCheckOutDate_returns400() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildBookingJson(
                                LocalDate.now().plusDays(1).toString(),
                                LocalDate.now().minusDays(1).toString())))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(bookingService);
    }

    // ───────── DELETE /bookings/{id} ─────────

    @Test
    void cancelBooking_validId_returns204() throws Exception {
        Mockito.doNothing().when(bookingService).cancel(1L);

        mockMvc.perform(delete("/bookings/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(bookingService).cancel(1L);
    }

    @Test
    void cancelBooking_notFound_returns404() throws Exception {
        Mockito.doThrow(new BookingNotFound("Not your booking"))
                .when(bookingService).cancel(9999L);

        mockMvc.perform(delete("/bookings/9999"))
                .andExpect(status().isNotFound());

        Mockito.verify(bookingService).cancel(9999L);
    }

    @Test
    void cancelBooking_tooLateToCancel_returns400() throws Exception {
        Mockito.doThrow(new CanNotCancelledException("Too late to cancel"))
                .when(bookingService).cancel(1L);

        mockMvc.perform(delete("/bookings/1"))
                .andExpect(status().isBadRequest());

        Mockito.verify(bookingService).cancel(1L);
    }
}