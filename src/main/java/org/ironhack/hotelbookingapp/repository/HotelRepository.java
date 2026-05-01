package org.ironhack.hotelbookingapp.repository;

import org.ironhack.hotelbookingapp.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository  extends JpaRepository<Hotel,Long> {
    List<Hotel> findByCity(String city);
    boolean existsByName(String name);
}
