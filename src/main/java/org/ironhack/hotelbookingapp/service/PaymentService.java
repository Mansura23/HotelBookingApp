package org.ironhack.hotelbookingapp.service;

import jakarta.transaction.Transactional;
import org.ironhack.hotelbookingapp.dto.request.PaymentRequestDto;
import org.ironhack.hotelbookingapp.dto.response.PaymentResponseDto;
import org.ironhack.hotelbookingapp.entity.Booking;
import org.ironhack.hotelbookingapp.entity.Payment;
import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.enums.BookingStatus;
import org.ironhack.hotelbookingapp.enums.PaymentStatus;
import org.ironhack.hotelbookingapp.mapper.PaymentMapper;
import org.ironhack.hotelbookingapp.repository.BookingRepository;
import org.ironhack.hotelbookingapp.repository.PaymentRepository;
import org.ironhack.hotelbookingapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        Booking booking = bookingRepository.findByIdAndUser(dto.getBookingId(), user)
                .orElseThrow(() -> new RuntimeException("Booking not yours"));

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            throw new RuntimeException("Already paid");
        }

        BigDecimal amount = booking.getTotalPrice();

        if (user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance().subtract(amount));

        Payment payment = PaymentMapper.toEntity(dto,booking);
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
        refund.setStatus(PaymentStatus.SUCCESS);
        refund.setPaidAt(LocalDateTime.now());

        paymentRepository.save(refund);
        userRepository.save(user);
    }




}
