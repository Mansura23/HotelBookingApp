package org.ironhack.hotelbookingapp.exception;

public class CancelledCanNotUpDateException extends RuntimeException {
    public CancelledCanNotUpDateException(String message) {
        super(message);
    }
}
