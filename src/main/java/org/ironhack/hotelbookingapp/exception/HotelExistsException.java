package org.ironhack.hotelbookingapp.exception;

public class HotelExistsException extends RuntimeException {
    public HotelExistsException(String message) {
        super(message);
    }
}
