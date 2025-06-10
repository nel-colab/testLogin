package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import com.example.demo.JwtUtil;
import com.example.demo.exception.CustomValidationException;
import com.example.demo.model.PhoneEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PASSWORD_REGEX = Pattern
            .compile("^(?=(?:[^A-Z]*[A-Z][^A-Z]*$))(?=(?:[^0-9]*[0-9][^0-9]*[0-9][^0-9]*$))[a-zA-Z0-9]{8,12}$");

    private static final String SECRET = "miClaveSecretaMuySeguraYDeLongitudAdecuada123456";

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME_MS = 3600000;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public UserEntity save(UserEntity user) {
        // Validar email
        if (user.getEmail() == null || !EMAIL_REGEX.matcher(user.getEmail()).matches()) {
            throw new CustomValidationException(1001, "Email inválido");
        }

        // Validar password
        if (user.getPassword() == null || !PASSWORD_REGEX.matcher(user.getPassword()).matches()) {
            throw new CustomValidationException(1002, "Password inválida");
        }

        // Validar si el usuario ya existe
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomValidationException(1003, "Usuario ya existe");
        }

        // Encriptar la contraseña antes de guardar

        // Setear campos adicionales
        user.setId(UUID.randomUUID().toString());
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(true);
        user.setToken(generateJwtToken(user));

        // Asignar relación con teléfonos
        if (user.getPhones() != null) {
            for (PhoneEntity phone : user.getPhones()) {
                phone.setUser(user);
            }
        }

        // Guardar usuario
        return userRepository.save(user);
    }

    public UserEntity login(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomValidationException(1004, "Token inválido");
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
        } catch (JwtException e) {
            throw new CustomValidationException(1005, "Token inválido o expirado");
        }

        String email = claims.get("email", String.class);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomValidationException(1006, "Usuario no encontrado"));

        user.setLastLogin(LocalDateTime.now());
        String newToken = generateJwtToken(user);
        user.setToken(newToken);
        userRepository.save(user);

        return user;
    }

    private String generateJwtToken(UserEntity user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .signWith(SECRET_KEY)
                .compact();
    }
}
