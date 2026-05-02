package org.ironhack.hotelbookingapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ironhack.hotelbookingapp.enums.PaymentMethod;

@Data
public class PaymentRequestDto {
    @NotBlank(message = "booking_id is required")
    private Long bookingId;

    @NotBlank(message = "payment_method is required")
    private PaymentMethod paymentMethod;

}
