package org.ironhack.hotelbookingapp.controller;


import org.ironhack.hotelbookingapp.dto.request.RoomRequestDto;
import org.ironhack.hotelbookingapp.dto.response.RoomResponseDto;
import org.ironhack.hotelbookingapp.enums.RoomType;
import org.ironhack.hotelbookingapp.exception.RoomNotFound;
import org.ironhack.hotelbookingapp.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
public class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Test
    void getAllRooms_emptyList_returnsEmptyArray() throws Exception {
        when(roomService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/admin/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllRooms_returnsListOfRooms() throws Exception{
        List<RoomResponseDto> rooms = List.of(
                new RoomResponseDto(1L,"1A",new BigDecimal("193.4"), RoomType.DOUBLE,1L,"Hilton",true),
                new RoomResponseDto(2L,"2A",new BigDecimal("100.99"), RoomType.SINGLE,1L,"Hilton",true)
        );
        when(roomService.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].roomNumber").value("1A"))
                .andExpect(jsonPath("$[1].roomNumber").value("2A"))
                .andExpect(jsonPath("$[0].hotelName").value("Hilton"))
                .andExpect(jsonPath("$[1].hotelName").value("Hilton"));

    }

    @Test
    void getRoomById_returnRoom() throws Exception{
        RoomResponseDto room = new RoomResponseDto(1L,"1A",new BigDecimal("193.4"), RoomType.DOUBLE,1L,"Hilton",true);
        when(roomService.findById(1L)).thenReturn(room);

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roomNumber").value("1A"))
                .andExpect(jsonPath("$.pricePerNight").value("193.4"))
                .andExpect(jsonPath("$.type").value("DOUBLE"))
                .andExpect(jsonPath("$.hotelName").value("Hilton"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void getRoomById_returnsRoomNotFound404() throws Exception{
        when(roomService.findById(99L))
                .thenThrow(new RoomNotFound("Room",99L));

        mockMvc.perform(get("/api/rooms/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value( "Room not found with id:99"));
    }

    @Test
    void createRoom_validRequest_returns201() throws Exception{
        RoomResponseDto created = new RoomResponseDto(1L,"1A",new BigDecimal("193.4"), RoomType.DOUBLE,1L,"Hilton",true);
        when(roomService.create(any(RoomRequestDto.class))).thenReturn(created);

        String requestBody= """
               {
                "roomNumber":"1A",
                "pricePerNight":193.4,
                "type":"DOUBLE",
                "hotelId":1
               }
                """;

        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roomNumber").value("1A"))
                .andExpect(jsonPath("$.hotelId").value(1));
    }

    @Test
    void createRoom_invalidRequest_returns400() throws Exception{
        String requestBody= """
                {
                "roomNumber":"",
                "pricePerNight":193.400,
                "type":"DOUBLE",
                "hotelId":1
                }
                """;

        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void updateFullRoom_validRequest_returnsUpdatedRoom() throws Exception{
        RoomResponseDto updated = new RoomResponseDto(1L,"1A",new BigDecimal("200.34"), RoomType.DOUBLE,1L,"Hilton",true);
        when(roomService.updateFull(eq(1L),any())).thenReturn(updated);

        String requestBody= """
                {
                "roomNumber":"1A",
                "pricePerNight":200.34,
                "type":"DOUBLE",
                "hotelId":1
                }
                """;

        mockMvc.perform(put("/api/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pricePerNight").value(200.34));
    }

    @Test
    void updatePartialRoom_validRequest_returnsUpdatedRoom() throws Exception{
        RoomResponseDto updated = new RoomResponseDto(1L,"1A",new BigDecimal("200.34"), RoomType.DOUBLE,1L,"Hilton",true);
        when(roomService.update(eq(1L),any())).thenReturn(updated);

        String requestBody= """
                {
                "roomNumber":"1A"
                }
                """;

        mockMvc.perform(patch("/api/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("1A"));
    }

    @Test
    void updateRoom_notFound_returns404() throws Exception {

        when(roomService.updateFull(eq(99L), any()))
                .thenThrow(new RoomNotFound("Room", 99L));

        String requestBody = """
    {
        "roomNumber":"1A",
        "pricePerNight":200.34,
        "type":"DOUBLE",
        "hotelId":1
    }
    """;

        mockMvc.perform(put("/api/rooms/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteRoom_existingId_returns204() throws Exception{
        doNothing().when(roomService).delete(1L);

        mockMvc.perform(delete("/api/rooms/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRoom_invalidId_returns404() throws Exception{
        doThrow(new  RoomNotFound("Room",99L)).when(roomService).delete(99L);

        mockMvc.perform(delete("/api/rooms/99"))
                .andExpect(status().isNotFound());
    }
}
