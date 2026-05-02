package org.ironhack.hotelbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.hotelbookingapp.dto.response.RoomResponseDto;
import org.ironhack.hotelbookingapp.exception.HotelNotFound;
import org.ironhack.hotelbookingapp.exception.RoomExistsException;
import org.ironhack.hotelbookingapp.exception.RoomNotFound;
import org.ironhack.hotelbookingapp.service.JWTService;
import org.ironhack.hotelbookingapp.service.RoomService;
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
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private JWTService jwtService;

    // ───────── Helper ─────────

    private RoomResponseDto buildResponse(Long id, String roomNumber) {
        RoomResponseDto dto = new RoomResponseDto();
        dto.setId(id);
        dto.setRoomNumber(roomNumber);
        dto.setPricePerNight(new BigDecimal("150.0"));
        dto.setHotelId(1L);
        return dto;
    }

    // ───────── CREATE ─────────

    @Test
    void createRoom_returnsCreated() throws Exception {
        Mockito.when(roomService.create(any())).thenReturn(buildResponse(1L, "101"));

        mockMvc.perform(post("/admin/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomNumber": "101",
                                    "type": "SINGLE",
                                    "pricePerNight": 150.0,
                                    "hotelId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomNumber").value("101"));

        Mockito.verify(roomService).create(any());
    }

    @Test
    void createRoom_duplicateRoomNumber_returnsConflict() throws Exception {
        Mockito.when(roomService.create(any()))
                .thenThrow(new RoomExistsException("Room number already exists"));

        mockMvc.perform(post("/admin/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomNumber": "101",
                                    "type": "SINGLE",
                                    "pricePerNight": 150.0,
                                    "hotelId": 1
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void createRoom_hotelNotFound_returns404() throws Exception {
        Mockito.when(roomService.create(any()))
                .thenThrow(new HotelNotFound("Hotel", 99L));

        mockMvc.perform(post("/admin/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomNumber": "101",
                                    "type": "SINGLE",
                                    "pricePerNight": 150.0,
                                    "hotelId": 99
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // ───────── FIND ALL ─────────

    @Test
    void findAllRooms_returnsOk() throws Exception {
        Mockito.when(roomService.findAll())
                .thenReturn(List.of(buildResponse(1L, "101"), buildResponse(2L, "102")));

        mockMvc.perform(get("/admin/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].roomNumber").value("101"))
                .andExpect(jsonPath("$[1].roomNumber").value("102"));

        Mockito.verify(roomService).findAll();
    }

    @Test
    void findAllRooms_emptyList_returnsOk() throws Exception {
        Mockito.when(roomService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/admin/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ───────── FIND BY ID ─────────

    @Test
    void findRoomById_returnsRoom() throws Exception {
        Mockito.when(roomService.findById(1L)).thenReturn(buildResponse(1L, "101"));

        mockMvc.perform(get("/admin/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomNumber").value("101"));

        Mockito.verify(roomService).findById(1L);
    }

    @Test
    void findRoomById_notFound_returns404() throws Exception {
        Mockito.when(roomService.findById(9999L))
                .thenThrow(new RoomNotFound("Room", 9999L));

        mockMvc.perform(get("/admin/rooms/9999"))
                .andExpect(status().isNotFound());

        Mockito.verify(roomService).findById(9999L);
    }

    // ───────── FIND BY HOTEL ID ─────────

    @Test
    void findRoomsByHotelId_returnsRooms() throws Exception {
        Mockito.when(roomService.findAllByHotelId(1L))
                .thenReturn(List.of(buildResponse(1L, "101"), buildResponse(2L, "102")));

        mockMvc.perform(get("/admin/rooms/hotel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        Mockito.verify(roomService).findAllByHotelId(1L);
    }

    @Test
    void findRoomsByHotelId_emptyList_returnsOk() throws Exception {
        Mockito.when(roomService.findAllByHotelId(99L)).thenReturn(List.of());

        mockMvc.perform(get("/admin/rooms/hotel/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ───────── PARTIAL UPDATE ─────────

    @Test
    void partialUpdateRoom_returnsUpdated() throws Exception {
        RoomResponseDto response = buildResponse(1L, "101");
        response.setPricePerNight(new BigDecimal("200.0"));

        Mockito.when(roomService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(patch("/admin/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomNumber": "101",
                                    "pricePerNight": 200.0
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pricePerNight").value(200.0));

        Mockito.verify(roomService).update(eq(1L), any());
    }

    @Test
    void partialUpdateRoom_notFound_returns404() throws Exception {
        Mockito.when(roomService.update(eq(9999L), any()))
                .thenThrow(new RoomNotFound("Room", 9999L));

        mockMvc.perform(patch("/admin/rooms/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomNumber": "101",
                                    "pricePerNight": 200.0
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // ───────── FULL UPDATE ─────────

    @Test
    void fullUpdateRoom_returnsUpdated() throws Exception {
        RoomResponseDto response = buildResponse(1L, "202");
        response.setPricePerNight(new BigDecimal("300.0"));

        Mockito.when(roomService.updateFull(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/admin/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomNumber": "202",
                                    "type": "DOUBLE",
                                    "pricePerNight": 300.0,
                                    "hotelId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("202"))
                .andExpect(jsonPath("$.pricePerNight").value(300.0));

        Mockito.verify(roomService).updateFull(eq(1L), any());
    }

    @Test
    void fullUpdateRoom_notFound_returns404() throws Exception {
        Mockito.when(roomService.updateFull(eq(9999L), any()))
                .thenThrow(new RoomNotFound("Room", 9999L));

        mockMvc.perform(put("/admin/rooms/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomNumber": "202",
                                    "type": "DOUBLE",
                                    "pricePerNight": 300.0,
                                    "hotelId": 1
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void fullUpdateRoom_hotelNotFound_returns404() throws Exception {
        Mockito.when(roomService.updateFull(eq(1L), any()))
                .thenThrow(new HotelNotFound("Hotel", 99L));

        mockMvc.perform(put("/admin/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomNumber": "202",
                                    "type": "DOUBLE",
                                    "pricePerNight": 300.0,
                                    "hotelId": 99
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // ───────── DELETE ─────────

    @Test
    void deleteRoom_returnsNoContent() throws Exception {
        Mockito.doNothing().when(roomService).delete(1L);

        mockMvc.perform(delete("/admin/rooms/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(roomService).delete(1L);
    }

    @Test
    void deleteRoom_notFound_returns404() throws Exception {
        Mockito.doThrow(new RoomNotFound("Room", 9999L))
                .when(roomService).delete(9999L);

        mockMvc.perform(delete("/admin/rooms/9999"))
                .andExpect(status().isNotFound());

        Mockito.verify(roomService).delete(9999L);
    }
}