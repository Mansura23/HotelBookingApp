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

    @ExceptionHandler(CancelledCanNotUpDateException.class)
    public ResponseEntity<ErrorResponseDto> handleCancelledCanNotUpDate(CancelledCanNotUpDateException ex,HttpServletRequest request){
        ErrorResponseDto response=new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                "Cancelled resource cannot be updated.",
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(CanNotChangeDurationException.class)
    public ResponseEntity<ErrorResponseDto> handleCanNotChangeDuration(CanNotChangeDurationException ex,HttpServletRequest request){
        ErrorResponseDto response=new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                "Duration cannot be changed.",
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidCheckInDateException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCheckInDate(InvalidCheckInDateException ex,HttpServletRequest request){
        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid check-in date.",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidCredantialsException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentials(InvalidCredantialsException ex,HttpServletRequest request){
        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid credentials.",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidDate(InvalidDateException ex,HttpServletRequest request){
        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid date provided.",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RoomExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleRoomExists(RoomExistsException ex, HttpServletRequest request){
        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                "Room already exists.",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserExistsException(UserExistsException ex, HttpServletRequest request){
        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                "User already exists.",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request){
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "User not found",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotActiveException(UserNotActiveException ex, HttpServletRequest request){
        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.FORBIDDEN.value(),
                "User account is not active.",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
