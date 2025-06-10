package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.exception.CustomValidationException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<Map<String, List<Map<String, Object>>>> handleCustomValidationException(CustomValidationException ex) {
        Map<String, List<Map<String, Object>>> body = Map.of(
            "error", List.of(
                Map.of(
                    "timestamp", LocalDateTime.now(),
                    "codigo", ex.getCodigo(),
                    "detail", ex.getMessage()
                )
            )
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Puedes agregar más ExceptionHandler si deseas manejar otros errores específicos
}