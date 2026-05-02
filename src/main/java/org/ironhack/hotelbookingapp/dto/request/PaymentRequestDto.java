package org.ironhack.hotelbookingapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.PaymentMethod;

@Data
public class PaymentRequestDto {
    @NotNull
    private Long bookingId;

    @NotNull
    private PaymentMethod paymentMethod;

}
