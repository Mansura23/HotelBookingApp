package org.ironhack.hotelbookingapp.controller;

import org.ironhack.hotelbookingapp.dto.request.HotelRequestDto;
import org.ironhack.hotelbookingapp.dto.request.HotelRequestUpdateDto;
import org.ironhack.hotelbookingapp.dto.response.HotelResponseDto;
import org.ironhack.hotelbookingapp.exception.HotelNotFound;
import org.ironhack.hotelbookingapp.service.HotelService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(HotelController.class)
public class HotelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Test
    void getHotels_returnsEmpty() throws Exception {
        when(hotelService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    @Test
    void getAllHotels_returnsListOfHotels() throws Exception {
        List<HotelResponseDto>  hotels = List.of(
                new HotelResponseDto(1L,
                        "Hilton Baku",
                        "Luxury hotel with sea view and modern facilities",
                        "Azerbaijan",
                        "Baku",
                        "1B Azadlig Avenue",
                        "AZ1000",
                        "+994124935000",
                        4.7),
                new HotelResponseDto(
                        2L,
                        "Flame Towers",
                        "Luxury hotel with sea view and modern facilities",
                        "Azerbaijan",
                        "Baku",
                        "Haydar Aliyev 2G",
                        "AZ1300",
                        "+994124955050",
                        3.9
                ));

        when(hotelService.getAll()).thenReturn(hotels);

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Hilton Baku"))
                .andExpect(jsonPath("$[1].name").value("Flame Towers"));
    }

    @Test
    void getHotelById_returnsHotel() throws Exception {
        HotelResponseDto hotel=new HotelResponseDto(1L,
                "Hilton Baku",
                "Luxury hotel with sea view and modern facilities",
                "Azerbaijan",
                "Baku",
                "1B Azadlig Avenue",
                "AZ1000",
                "+994124935000",
                4.7);

        when(hotelService.getById(1L)).thenReturn(hotel);

        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Hilton Baku"))
                .andExpect(jsonPath("$.description").value("Luxury hotel with sea view and modern facilities"))
                .andExpect(jsonPath("$.country").value("Azerbaijan"))
                .andExpect(jsonPath("$.city").value("Baku"))
                .andExpect(jsonPath("$.address").value("1B Azadlig Avenue"))
                .andExpect(jsonPath("$.zipCode").value("AZ1000"))
                .andExpect(jsonPath("$.phone").value("+994124935000"))
                .andExpect(jsonPath("$.rating").value(4.7));

    }

    @Test
    void getHotelById_returns404() throws Exception {
        when(hotelService.getById(99L)).thenThrow(
                new HotelNotFound("Hotel",99L)
        );

        mockMvc.perform(get("/api/hotels/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Hotel not found with id:99"));
    }

    @Test
    void createHotel_validRequest_returns201() throws Exception {
        HotelResponseDto created=new HotelResponseDto(1L,
                "Hilton Baku",
                "Luxury hotel with sea view and modern facilities",
                "Azerbaijan",
                "Baku",
                "1B Azadlig Avenue",
                "AZ1000",
                "+994124935000",
                4.7);

        when(hotelService.create(any(HotelRequestDto.class))).thenReturn(created);

        String requestBody= """
                {
                  "name": "Hilton Baku",
                  "description": "Luxury hotel with sea view and modern facilities",
                  "country": "Azerbaijan",
                  "city": "Baku",
                  "address": "1B Azadlig Avenue",
                  "zipCode": "AZ1000",
                  "phone": "+994124935000",
                  "rating": 4
                }
                """;

        mockMvc.perform(post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hilton Baku"));
    }

    @Test
    void createHotel_invalidRequest_returns400() throws Exception{
        String requestBody= """
                {
                  "name": "",
                  "description": "Luxury hotel with sea view and modern facilities",
                  "country": "Azerbaijan",
                  "city": "Baku",
                  "address": "1B Azadlig Avenue",
                  "zipCode": "AZ1000",
                  "phone": "+994124935000",
                  "rating": 4
                }
                """;

        mockMvc.perform(post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void updateFullHotel_validRequest_returns200() throws Exception {
        HotelResponseDto updated=new HotelResponseDto(1L,
                "Hilton Baku",
                "Luxury hotel with sea view and modern facilities",
                "Azerbaijan",
                "Baku",
                "1B Azadlig Avenue",
                "AZ1000",
                "+994124935000",
                4.7);

        when(hotelService.fullUpdate(eq(1L),any(HotelRequestDto.class))).thenReturn(updated);

        String requestBody= """
                {
                  "name": "Hilton Baku",
                  "description": "Luxury hotel with sea view and modern facilities",
                  "country": "Azerbaijan",
                  "city": "Baku",
                  "address": "1B Azadlig Avenue",
                  "zipCode": "AZ1000",
                  "phone": "+994124935000",
                  "rating": 4
                }
                """;

        mockMvc.perform(put("/api/hotels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hilton Baku"));

    }

    @Test
    void updatePartialHotel_validRequest_returns200() throws Exception {
        HotelResponseDto updated=new HotelResponseDto(1L,
                "Hilton Baku",
                "Luxury hotel with sea view and modern facilities",
                "Azerbaijan",
                "Baku",
                "1B Azadlig Avenue",
                "AZ1000",
                "+994124935000",
                4.7);

        when(hotelService.partialUpdate(eq(1L),any(HotelRequestUpdateDto.class))).thenReturn(updated);

        String requestBody= """
                {
                  "address": "1B Azadlig Avenue",
                  "zipCode": "AZ1000",
                  "phone": "+994124935000"         
                }
                """;

        mockMvc.perform(patch("/api/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("1B Azadlig Avenue"))
                .andExpect(jsonPath("$.zipCode").value("AZ1000"))
                .andExpect(jsonPath("$.phone").value("+994124935000"));

    }

    @Test
    void updateHotel_invalidRequest_returns400() throws Exception {
        String requestBody= """
                {
                  "name": "a",
                  "description": "Luxury hotel with sea view and modern facilities",
                  "country": "Azerbaijan",
                  "city": "Baku",
                  "address": "1B Azadlig Avenue",
                  "zipCode": "AZ1000",
                  "phone": "+994124935000",
                  "rating": 4
                }
                """;

        mockMvc.perform(put("/api/hotels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void deleteHotel_existingId_returns204() throws Exception {
        doNothing().when(hotelService).delete(eq(1L));

        mockMvc.perform(delete("/api/hotels/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteHote_nonExistingId_returns404() throws Exception {
        doThrow(new HotelNotFound("Hotel",99L)).when(hotelService).delete(eq(99L));

        mockMvc.perform(delete("/api/hotels/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Hotel not found with id:99"));
    }
}
