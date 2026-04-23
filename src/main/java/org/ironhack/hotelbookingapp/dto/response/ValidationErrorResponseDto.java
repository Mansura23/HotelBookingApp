package org.ironhack.hotelbookingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponseDto {
    private int status;
    private List<String> errors;
    private String message;
    private String path;
    private LocalDateTime timestamp;

}
