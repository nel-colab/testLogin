package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.exception.CustomValidationException;

import java.util.List;
import java.util.Map;


public class GlobalExceptionHandlerTest {
    @Test
    void handleCustomValidationExceptionReturnsExpectedResponse() {
        // Arrange
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        CustomValidationException exception = new CustomValidationException(1001, "Email inválido");

        // Act
        ResponseEntity<Map<String, List<Map<String, Object>>>> response = handler.handleCustomValidationException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, List<Map<String, Object>>> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("error"));

        List<Map<String, Object>> errorList = body.get("error");
        assertEquals(1, errorList.size());

        Map<String, Object> error = errorList.get(0);
        assertEquals(1001, error.get("codigo"));
        assertEquals("Email inválido", error.get("detail"));
        assertNotNull(error.get("timestamp"));
    }
}
