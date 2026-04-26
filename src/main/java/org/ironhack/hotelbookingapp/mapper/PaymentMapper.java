package org.ironhack.hotelbookingapp.mapper;

import org.ironhack.hotelbookingapp.dto.request.PaymentRequestDto;
import org.ironhack.hotelbookingapp.dto.response.PaymentResponseDto;
import org.ironhack.hotelbookingapp.entity.Booking;
import org.ironhack.hotelbookingapp.entity.Payment;
import org.ironhack.hotelbookingapp.enums.Currency;
import org.ironhack.hotelbookingapp.enums.PaymentStatus;

public class PaymentMapper {
    public Payment toEntity(PaymentRequestDto request, Booking booking) {
        Payment payment = new Payment();

        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(request.getAmount());
        payment.setBooking(booking);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCurrency(Currency.AZN);

        return payment;
    }

    public PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getBooking().getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getPaidAt()
        );
    }
}
