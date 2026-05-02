package org.ironhack.hotelbookingapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironhack.hotelbookingapp.dto.request.PaymentRequestDto;
import org.ironhack.hotelbookingapp.dto.response.PaymentResponseDto;
import org.ironhack.hotelbookingapp.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponseDto pay(@Valid @RequestBody PaymentRequestDto dto) {
        return paymentService.pay(dto);
    }
}
