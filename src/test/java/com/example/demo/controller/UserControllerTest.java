package com.example.demo.controller;

import com.example.demo.model.UserEntity;
import com.example.demo.service.UserService;
import com.example.demo.exception.CustomValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void testCreateUser_Success() {
        UserEntity inputUser = new UserEntity();
        inputUser.setEmail("test@example.com");
        inputUser.setPassword("Password123");
        inputUser.setName("Test User");
        inputUser.setPhones(Collections.emptyList());

        UserEntity savedUser = new UserEntity();
        savedUser.setId("1234");
        savedUser.setEmail(inputUser.getEmail());

        when(userService.save(inputUser)).thenReturn(savedUser);

        ResponseEntity<UserEntity> response = userController.createUser(inputUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedUser, response.getBody());
    }

    @Test
    public void testCreateUser_InvalidEmail() {
        UserEntity invalidUser = new UserEntity();
        invalidUser.setEmail("invalid-email");
        invalidUser.setPassword("Password123");

        when(userService.save(invalidUser)).thenThrow(new CustomValidationException(1001, "Email inválido"));

        CustomValidationException thrown = assertThrows(CustomValidationException.class, () -> {
            userController.createUser(invalidUser);
        });

        assertEquals(1001, thrown.getCodigo());
        assertEquals("Email inválido", thrown.getMessage());
    }

    @Test
    public void testLoginUser_Success() {
        String token = "Bearer valid.jwt.token";
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        when(userService.login(token)).thenReturn(user);

        ResponseEntity<?> response = userController.loginUser(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testLoginUser_MissingBearerPrefix() {
        String invalidToken = "InvalidToken";

        ResponseEntity<?> response = userController.loginUser(invalidToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testLoginUser_InvalidTokenException() {
        String token = "Bearer invalid.jwt.token";

        when(userService.login(token)).thenThrow(new CustomValidationException(1005, "Token inválido o expirado"));

        CustomValidationException thrown = assertThrows(CustomValidationException.class, () -> {
            userController.loginUser(token);
        });

        assertEquals(1005, thrown.getCodigo());
        assertEquals("Token inválido o expirado", thrown.getMessage());
    }
}