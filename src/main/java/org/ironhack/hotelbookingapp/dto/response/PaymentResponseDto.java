package org.ironhack.hotelbookingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ironhack.hotelbookingapp.enums.Currency;
import org.ironhack.hotelbookingapp.enums.PaymentMethod;
import org.ironhack.hotelbookingapp.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private Currency currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
