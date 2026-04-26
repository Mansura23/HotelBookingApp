package org.ironhack.hotelbookingapp.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.PaymentMethod;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    @NotNull
    private Long bookingId;

    @DecimalMin("0.01")
    @Digits(integer = 6, fraction = 2)
    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;

}
