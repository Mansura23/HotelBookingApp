package org.ironhack.hotelbookingapp.controller;

import org.ironhack.hotelbookingapp.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class HotelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;


}
