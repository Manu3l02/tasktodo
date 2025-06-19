package com.example.tasktodo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.NOT_FOUND.value(),
            "error", "Not Found",
            "message", ex.getMessage(),
            "resource", ex.getResourceName(),
            "field", ex.getFieldName(),
            "value", ex.getFieldValue()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Aggiungere altri handler , es. per ValidationException, AccessDeniedException, ecc.
}
