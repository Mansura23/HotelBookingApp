package org.ironhack.hotelbookingapp.controller;

import org.ironhack.hotelbookingapp.dto.request.HotelRequestDto;
import org.ironhack.hotelbookingapp.dto.request.HotelRequestUpdateDto;
import org.ironhack.hotelbookingapp.dto.response.HotelResponseDto;
import org.ironhack.hotelbookingapp.exception.GlobalExceptionHandler;
import org.ironhack.hotelbookingapp.exception.HotelNotFound;
import org.ironhack.hotelbookingapp.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
@AutoConfigureMockMvc(addFilters = false) // security OFF
@Import(GlobalExceptionHandler.class)     // exception handler daxil edilir
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    // ✅ GET BY ID
    @Test
    void getHotelById_success() throws Exception {

        HotelResponseDto hotel = new HotelResponseDto(
                1L,
                "Hilton Baku",
                "Luxury hotel",
                "Azerbaijan",
                "Baku",
                "1B Azadlig Avenue",
                "AZ1000",
                "+994124935000",
                4.5
        );

        when(hotelService.getById(1L)).thenReturn(hotel);

        mockMvc.perform(get("/admin/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hilton Baku"))
                .andExpect(jsonPath("$.city").value("Baku"));
    }

    // ❌ NOT FOUND
    @Test
    void getHotelById_notFound() throws Exception {

        when(hotelService.getById(99L))
                .thenThrow(new HotelNotFound("Hotel not found with id:99"));

        mockMvc.perform(get("/admin/hotels/99"))
                .andExpect(status().isNotFound());
    }

    // ✅ CREATE
    @Test
    void createHotel_success() throws Exception {

        HotelResponseDto created = new HotelResponseDto(
                1L,
                "Hilton Baku",
                "Luxury hotel",
                "Azerbaijan",
                "Baku",
                "1B Azadlig Avenue",
                "AZ1000",
                "+994124935000",
                4.5
        );

        when(hotelService.create(any(HotelRequestDto.class)))
                .thenReturn(created);

        String body = """
                {
                  "name": "Hilton Baku",
                  "description": "Luxury hotel",
                  "country": "Azerbaijan",
                  "city": "Baku",
                  "address": "1B Azadlig Avenue",
                  "zipCode": "AZ1000",
                  "phone": "+994124935000",
                  "rating": 4.5
                }
                """;

        mockMvc.perform(post("/admin/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ❌ VALIDATION
    @Test
    void createHotel_validationFail() throws Exception {

        String body = """
                {
                  "name": "",
                  "description": "Luxury hotel",
                  "country": "Azerbaijan",
                  "city": "Baku",
                  "address": "addr",
                  "zipCode": "AZ1000",
                  "phone": "+994",
                  "rating": 4
                }
                """;

        mockMvc.perform(post("/admin/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ✅ FULL UPDATE
    @Test
    void updateHotel_success() throws Exception {

        HotelResponseDto updated = new HotelResponseDto(
                1L,
                "Updated Hotel",
                "Updated desc",
                "Azerbaijan",
                "Baku",
                "New address",
                "AZ1000",
                "+994",
                4.2
        );

        when(hotelService.fullUpdate(eq(1L), any(HotelRequestDto.class)))
                .thenReturn(updated);

        String body = """
                {
                  "name": "Updated Hotel",
                  "description": "Updated desc",
                  "country": "Azerbaijan",
                  "city": "Baku",
                  "address": "New address",
                  "zipCode": "AZ1000",
                  "phone": "+994",
                  "rating": 4.2
                }
                """;

        mockMvc.perform(put("/admin/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Hotel"));
    }

    // ✅ PARTIAL UPDATE
    @Test
    void partialUpdate_success() throws Exception {

        HotelResponseDto updated = new HotelResponseDto(
                1L,
                "Hilton Baku",
                "desc",
                "Azerbaijan",
                "Baku",
                "Updated Address",
                "AZ1000",
                "+994",
                4.5
        );

        when(hotelService.partialUpdate(eq(1L), any(HotelRequestUpdateDto.class)))
                .thenReturn(updated);

        String body = """
                {
                  "address": "Updated Address"
                }
                """;

        mockMvc.perform(patch("/admin/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Updated Address"));
    }

    // ✅ DELETE
    @Test
    void deleteHotel_success() throws Exception {

        doNothing().when(hotelService).delete(1L);

        mockMvc.perform(delete("/admin/hotels/1"))
                .andExpect(status().isNoContent());
    }

    // ❌ DELETE NOT FOUND
    @Test
    void deleteHotel_notFound() throws Exception {

        doThrow(new HotelNotFound("Hotel not found with id:99"))
                .when(hotelService).delete(99L);

        mockMvc.perform(delete("/admin/hotels/99"))
                .andExpect(status().isNotFound());
    }
}