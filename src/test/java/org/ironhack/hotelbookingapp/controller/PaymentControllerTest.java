package org.ironhack.hotelbookingapp.controller;

import org.ironhack.hotelbookingapp.dto.response.PaymentResponseDto;
import org.ironhack.hotelbookingapp.enums.Currency;
import org.ironhack.hotelbookingapp.enums.PaymentMethod;
import org.ironhack.hotelbookingapp.enums.PaymentStatus;
import org.ironhack.hotelbookingapp.exception.BookingNotFound;
import org.ironhack.hotelbookingapp.exception.BookingPayedException;
import org.ironhack.hotelbookingapp.exception.InsufficientBalanceException;
import org.ironhack.hotelbookingapp.service.JWTService;
import org.ironhack.hotelbookingapp.service.PaymentService;
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
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WithMockUser(username = "user@example.com", roles = {"USER"})
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private JWTService jwtService;

    // ───────── Helper ─────────

    private PaymentResponseDto buildResponse() {
        return new PaymentResponseDto(
                1L,
                1L,
                new BigDecimal("300.00"),
                Currency.AZN,
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                LocalDateTime.now(),
                null
        );
    }

    private String buildPaymentJson(Long bookingId, String method) {
        return """
                {
                    "bookingId": %d,
                    "paymentMethod": "%s"
                }
                """.formatted(bookingId, method);
    }

    // ───────── POST /payments ─────────

    @Test
    void pay_validRequest_returns200() throws Exception {
        Mockito.when(paymentService.pay(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildPaymentJson(1L, "CARD")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.amount").value(300.00))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.paymentMethod").value("CARD"));

        Mockito.verify(paymentService).pay(any());
    }

    @Test
    void pay_bookingNotFound_returns404() throws Exception {
        Mockito.when(paymentService.pay(any()))
                .thenThrow(new BookingNotFound("Booking not yours"));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildPaymentJson(9999L, "CARD")))
                .andExpect(status().isNotFound());
    }

    @Test
    void pay_alreadyPaid_returns400() throws Exception {
        Mockito.when(paymentService.pay(any()))
                .thenThrow(new BookingPayedException("Already paid"));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildPaymentJson(1L, "CARD")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pay_insufficientBalance_returns400() throws Exception {
        Mockito.when(paymentService.pay(any()))
                .thenThrow(new InsufficientBalanceException("Insufficient balance"));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildPaymentJson(1L, "CARD")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pay_nullBookingId_returns400() throws Exception {
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "bookingId": null,
                                    "paymentMethod": "CARD"
                                }
                                """))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(paymentService);
    }

    @Test
    void pay_nullPaymentMethod_returns400() throws Exception {
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "bookingId": 1,
                                    "paymentMethod": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(paymentService);
    }
}