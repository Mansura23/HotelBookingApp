package org.ironhack.hotelbookingapp.controller;

import org.ironhack.hotelbookingapp.dto.response.HotelResponseDtoForUser;
import org.ironhack.hotelbookingapp.service.HotelService;
import org.ironhack.hotelbookingapp.service.JWTService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test") // Test profilini aktiv edirik
public class AllHotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @MockitoBean
    private JWTService jwtService; // Güvənlik konfiqurasiyası üçün mütləqdir

    @Test
    void getAllHotels_returnsListOfHotels() throws Exception {
        // Mock data
        HotelResponseDtoForUser hotel1 = new HotelResponseDtoForUser(
                1L, "Hilton", "The best hotel", "Azerbaijan",
                "123AJ", "0553718744", "Gence", "asas", 4.5, List.of()
        );
        HotelResponseDtoForUser hotel2 = new HotelResponseDtoForUser(
                2L, "Flame", "The best hotel", "Azerbaijan",
                "1223AJ", "0553718744", "Gence", "sa", 4.5, List.of()
        );

        List<HotelResponseDtoForUser> hotels = List.of(hotel1, hotel2);

        // Mocking
        Mockito.when(hotelService.getAll()).thenReturn(hotels);

        // Perform & Expect
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Hilton"))
                .andExpect(jsonPath("$[1].name").value("Flame"));

        Mockito.verify(hotelService).getAll();
    }

    @Test
    void getHotelsByCity_returnsFilteredHotels() throws Exception {
        // Mock data
        HotelResponseDtoForUser hotel = new HotelResponseDtoForUser(
                1L, "Hilton", "The best hotel", "Azerbaijan",
                "123AJ", "0553718744", "Gence", "asas", 4.5, List.of()
        );

        // Mocking
        Mockito.when(hotelService.findHotelsByCity("Gence"))
                .thenReturn(List.of(hotel));

        // Perform & Expect
        mockMvc.perform(get("/hotels/city")
                        .param("city", "Gence"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Gence"))
                .andExpect(jsonPath("$[0].name").value("Hilton"));

        Mockito.verify(hotelService).findHotelsByCity("Gence");
    }
}