package org.ironhack.hotelbookingapp.exception;

public class InvalidCheckInDateException extends RuntimeException {
    public InvalidCheckInDateException(String message) {
        super(message);
    }
}
