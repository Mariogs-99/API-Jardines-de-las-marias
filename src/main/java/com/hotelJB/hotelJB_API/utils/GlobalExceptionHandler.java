package com.hotelJB.hotelJB_API.utils;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<MessageDTO> handleCustomException(CustomException ex) {
        String message = ex.getErrorType().getMessage() + ": " + ex.getEntityName();
        return new ResponseEntity<>(new MessageDTO(message), ex.getErrorType().getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(new MessageDTO("Internal Server Error"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageDTO> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDTO("Error: " + message));
    }
}
