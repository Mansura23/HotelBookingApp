package org.ironhack.hotelbookingapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ironhack.hotelbookingapp.enums.RoomType;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="room_number",unique=true )
    private String roomNumber;

    @Column(name="price_per_night")
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @ManyToOne
    @JoinColumn(name="hotel_id")
    private Hotel hotel;

    private boolean available;

}
