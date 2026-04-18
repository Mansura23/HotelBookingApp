package org.ironhack.hotelbookingapp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String country;

    private String address;

    private double rating;

    @OneToMany(mappedBy="hotel")
    private List<Room> rooms;
}
