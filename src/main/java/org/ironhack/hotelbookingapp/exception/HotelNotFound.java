package org.ironhack.hotelbookingapp.exception;

public class HotelNotFound extends RuntimeException {
    public HotelNotFound(String message) {
        super(message);
    }

    public HotelNotFound(String resourceName,Long id) {
        super(resourceName + " not found with id:" + id);
    }
}
