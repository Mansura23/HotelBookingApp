package org.ironhack.hotelbookingapp.exception;

public class CanNotCancelledException extends RuntimeException {
    public CanNotCancelledException(String message) {
        super(message);
    }
}
