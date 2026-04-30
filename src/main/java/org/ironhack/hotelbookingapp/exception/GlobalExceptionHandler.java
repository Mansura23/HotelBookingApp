package org.ironhack.hotelbookingapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.ironhack.hotelbookingapp.dto.response.ErrorResponseDto;
import org.ironhack.hotelbookingapp.dto.response.ValidationErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RoomNotFound.class)
    public ResponseEntity<ErrorResponseDto> handleRoomNotFound(RoomNotFound ex, HttpServletRequest request){
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Room not found",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HotelNotFound.class)
    public ResponseEntity<ErrorResponseDto> handleHotelNotFound(HotelNotFound ex, HttpServletRequest request){
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Hotel not found",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex,HttpServletRequest request){
        List<String> errors=ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ValidationErrorResponseDto errorResponse = new ValidationErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                "Validation failed",
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request){
        ErrorResponseDto response=new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotUserNotFound(UserNotFoundException ex, HttpServletRequest request){
        ErrorResponseDto response=new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "User not found",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotActive(UserNotActiveException ex, HttpServletRequest request){
        ErrorResponseDto response=new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "User not active",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserExists(UserExistsException ex, HttpServletRequest request){
        ErrorResponseDto response=new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "User already exists",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }





    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex,HttpServletRequest request){
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                "An unexpected error occurred",
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
