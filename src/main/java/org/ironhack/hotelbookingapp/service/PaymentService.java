package org.ironhack.hotelbookingapp.service;

import jakarta.transaction.Transactional;
import org.ironhack.hotelbookingapp.dto.request.PaymentRequestDto;
import org.ironhack.hotelbookingapp.dto.response.PaymentResponseDto;
import org.ironhack.hotelbookingapp.entity.Booking;
import org.ironhack.hotelbookingapp.entity.Payment;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.enums.BookingStatus;
import org.ironhack.hotelbookingapp.enums.Currency;
import org.ironhack.hotelbookingapp.enums.PaymentMethod;
import org.ironhack.hotelbookingapp.enums.PaymentStatus;
import org.ironhack.hotelbookingapp.exception.BookingNotFound;
import org.ironhack.hotelbookingapp.exception.BookingPayedException;
import org.ironhack.hotelbookingapp.exception.InsufficientBalanceException;
import org.ironhack.hotelbookingapp.mapper.PaymentMapper;
import org.ironhack.hotelbookingapp.repository.BookingRepository;
import org.ironhack.hotelbookingapp.repository.PaymentRepository;
import org.ironhack.hotelbookingapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public PaymentResponseDto pay(PaymentRequestDto dto) {

        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user = userRepository.findByEmail(email);

        Booking booking = bookingRepository.findByIdAndUser(dto.getBookingId(), user)
                .orElseThrow(() -> new BookingNotFound("Booking not yours"));

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            throw new BookingPayedException("Already paid");
        }

        BigDecimal amount = booking.getTotalPrice();

        if (user.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        user.setBalance(user.getBalance().subtract(amount));

        Payment payment = PaymentMapper.toEntity(dto,booking);
        payment.setUpdatedAt((null));
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setAmount(amount);

        paymentRepository.save(payment);

        booking.setBookingStatus(BookingStatus.CONFIRMED);

        userRepository.save(user);
        bookingRepository.save(booking);

        return PaymentMapper.toDto(payment);
    }

    @Transactional
    public void refund(Booking booking) {

        User user = booking.getUser();
        BigDecimal amount = booking.getTotalPrice();

        user.setBalance(user.getBalance().add(amount));

        Payment refund = new Payment();
        refund.setBooking(booking);
        refund.setAmount(amount);
        refund.setStatus(PaymentStatus.REFUNDED);
        refund.setUpdatedAt(LocalDateTime.now());
        refund.setCreatedAt(null);
        refund.setCurrency(Currency.AZN);
        refund.setPaymentMethod(PaymentMethod.CARD);

        paymentRepository.save(refund);
        userRepository.save(user);
    }




}
