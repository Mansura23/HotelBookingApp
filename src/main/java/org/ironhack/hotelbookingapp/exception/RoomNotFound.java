package org.ironhack.hotelbookingapp.exception;

public class RoomNotFound extends RuntimeException {
    public RoomNotFound(String message) {
        super(message);
    }

    public RoomNotFound(String resourceName, Long id) {
        super(resourceName + " not found with id:" + id);
    }
}
