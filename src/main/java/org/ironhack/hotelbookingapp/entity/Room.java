package org.ironhack.hotelbookingapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ironhack.hotelbookingapp.enums.RoomType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="room_number")
    private String roomNumber;

    @Column(name="price_per_night")
    private double pricePerNight;

    private RoomType type;

    @ManyToOne
    @JoinColumn(name="hotel_id")
    private Hotel hotel;

    private boolean available;

}
