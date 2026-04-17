package org.ironhack.hotelbookingapp.exception;

public class HotelNotFound extends RuntimeException {
    public HotelNotFound(String message) {
        super(message);
    }
}
