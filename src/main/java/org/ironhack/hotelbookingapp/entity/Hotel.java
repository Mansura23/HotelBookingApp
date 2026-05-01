package org.ironhack.hotelbookingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String country;

    @Column(name="zip_code")
    private String zipCode;

    private String phone;

    private String city;

    private String address;

    private double rating;

    @OneToMany(mappedBy="hotel", cascade=CascadeType.ALL,orphanRemoval=true)
    @JsonIgnore
    private List<Room> rooms;
}
