package org.ironhack.hotelbookingapp.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.PaymentMethod;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    @NotBlank(message = "booking_id is required")
    private Long bookingId;

    @NotBlank(message = "payment_method is required")
    private PaymentMethod paymentMethod;

}
