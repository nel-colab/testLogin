package com.example.demo.controller;

import com.example.demo.model.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestHeader(value = "Authorization", required = true) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userService.login(authHeader));
    }
}
