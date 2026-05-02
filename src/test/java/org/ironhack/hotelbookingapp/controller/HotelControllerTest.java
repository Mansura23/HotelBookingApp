package org.ironhack.hotelbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.hotelbookingapp.dto.response.HotelResponseDto;
import org.ironhack.hotelbookingapp.exception.HotelExistsException;
import org.ironhack.hotelbookingapp.exception.HotelNotFound;
import org.ironhack.hotelbookingapp.service.HotelService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private HotelService hotelService;

    @MockitoBean
    private JWTService jwtService;

    // ───────── CREATE ─────────

    @Test
    void createHotel_returnsCreated() throws Exception {
        HotelResponseDto response = new HotelResponseDto();
        response.setId(1L);
        response.setName("Grand Hotel");
        response.setCity("Baku");
        response.setRating(4.7);

        Mockito.when(hotelService.create(any())).thenReturn(response);

        mockMvc.perform(post("/admin/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Grand Hotel",
                                    "description": "Luxury hotel",
                                    "country": "Azerbaijan",
                                    "city": "Baku",
                                    "address": "Babek prospect",
                                    "zipCode": "AZ1000",
                                    "phone": "+994501234567",
                                    "rating": 4.7
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Grand Hotel"))
                .andExpect(jsonPath("$.city").value("Baku"));

        Mockito.verify(hotelService).create(any());
    }

    @Test
    void createHotel_alreadyExists_returnsConflict() throws Exception {
        Mockito.when(hotelService.create(any()))
                .thenThrow(new HotelExistsException("Hotel already exists"));

        mockMvc.perform(post("/admin/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Grand Hotel",
                                    "description": "Luxury hotel",
                                    "country": "Azerbaijan",
                                    "city": "Baku",
                                    "address": "Babek prospect",
                                    "zipCode": "AZ1000",
                                    "phone": "+994501234567",
                                    "rating": 4.7
                                }
                                """))
                .andExpect(status().isConflict());
    }

    // ───────── GET BY ID ─────────

    @Test
    void getHotelById_returnsHotel() throws Exception {
        HotelResponseDto response = new HotelResponseDto();
        response.setId(1L);
        response.setName("Grand Hotel");
        response.setCity("Baku");
        response.setRating(4.7);

        Mockito.when(hotelService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/admin/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Grand Hotel"))
                .andExpect(jsonPath("$.city").value("Baku"));

        Mockito.verify(hotelService).getById(1L);
    }

    @Test
    void getHotelById_notFound_returns404() throws Exception {
        Mockito.when(hotelService.getById(9999L))
                .thenThrow(new HotelNotFound("Hotel not found"));

        mockMvc.perform(get("/admin/hotels/9999"))
                .andExpect(status().isNotFound());

        Mockito.verify(hotelService).getById(9999L);
    }

    // ───────── FULL UPDATE ─────────

    @Test
    void fullUpdateHotel_returnsUpdated() throws Exception {
        HotelResponseDto response = new HotelResponseDto();
        response.setId(1L);
        response.setName("Updated Hotel");
        response.setCity("Ganja");
        response.setRating(4.9);

        Mockito.when(hotelService.fullUpdate(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/admin/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated Hotel",
                                    "description": "Updated description",
                                    "country": "Azerbaijan",
                                    "city": "Ganja",
                                    "address": "New address",
                                    "zipCode": "AZ2000",
                                    "phone": "+994559999999",
                                    "rating": 4.9
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Hotel"))
                .andExpect(jsonPath("$.city").value("Ganja"));

        Mockito.verify(hotelService).fullUpdate(eq(1L), any());
    }

    @Test
    void fullUpdateHotel_notFound_returns404() throws Exception {
        Mockito.when(hotelService.fullUpdate(eq(9999L), any()))
                .thenThrow(new HotelNotFound("Hotel not found"));

        mockMvc.perform(put("/admin/hotels/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Test",
                                    "description": "Test",
                                    "country": "Azerbaijan",
                                    "city": "Baku",
                                    "address": "Test",
                                    "zipCode": "AZ0000",
                                    "phone": "+994500000000",
                                    "rating": 4.0
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // ───────── PARTIAL UPDATE ─────────

    @Test
    void partialUpdateHotel_returnsUpdated() throws Exception {
        HotelResponseDto response = new HotelResponseDto();
        response.setId(1L);
        response.setName("Grand Hotel");
        response.setCity("Baku");
        response.setRating(3.5);

        Mockito.when(hotelService.partialUpdate(eq(1L), any())).thenReturn(response);

        mockMvc.perform(patch("/admin/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "rating": 3.5
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(3.5));

        Mockito.verify(hotelService).partialUpdate(eq(1L), any());
    }

    @Test
    void partialUpdateHotel_notFound_returns404() throws Exception {
        Mockito.when(hotelService.partialUpdate(eq(9999L), any()))
                .thenThrow(new HotelNotFound("Hotel not found"));

        mockMvc.perform(patch("/admin/hotels/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "rating": 3.5
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // ───────── DELETE ─────────

    @Test
    void deleteHotel_returnsNoContent() throws Exception {
        Mockito.doNothing().when(hotelService).delete(1L);

        mockMvc.perform(delete("/admin/hotels/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(hotelService).delete(1L);
    }

    @Test
    void deleteHotel_notFound_returns404() throws Exception {
        Mockito.doThrow(new HotelNotFound("Hotel not found"))
                .when(hotelService).delete(9999L);

        mockMvc.perform(delete("/admin/hotels/9999"))
                .andExpect(status().isNotFound());

        Mockito.verify(hotelService).delete(9999L);
    }
}