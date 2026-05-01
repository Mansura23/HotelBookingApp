package org.ironhack.hotelbookingapp.repository;

import org.ironhack.hotelbookingapp.entity.Booking;
import org.ironhack.hotelbookingapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    List<Payment> findByBooking(Booking booking);
}
