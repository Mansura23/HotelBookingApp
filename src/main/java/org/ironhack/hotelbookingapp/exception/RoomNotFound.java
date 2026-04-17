package org.ironhack.hotelbookingapp.exception;

public class RoomNotFound extends RuntimeException {
    public RoomNotFound(String message) {
        super(message);
    }
}
