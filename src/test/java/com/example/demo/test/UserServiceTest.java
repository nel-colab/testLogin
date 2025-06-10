package com.example.demo.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.JwtUtil;
import com.example.demo.exception.CustomValidationException;
import com.example.demo.model.Phone;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private JwtUtil jwtUtil;
    private UserService userService;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        userService = new UserService(userRepository, jwtUtil);
    }

    @Test
    void whenInvalidEmail_thenThrowException() {
        User user = new User();
        user.setEmail("mal_email");
        user.setPassword("abcdeF12"); // válida, pero aquí falla por email

        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> {
            userService.save(user);
        });
        assertEquals("Email inválido", exception.getMessage());
    }

    @Test
    void whenInvalidPassword_thenThrowException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("sinMayusculas1"); // inválida

        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> {
            userService.save(user);
        });
        assertEquals("Password inválida", exception.getMessage());
    }

    @Test
    void whenUserExists_thenThrowException() {
        User user = new User();
        user.setEmail("existente@example.com");
        user.setPassword("abcdeF12"); // válida

        when(userRepository.existsByEmail("existente@example.com")).thenReturn(true);

        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> {
            userService.save(user);
        });
        assertEquals("Usuario ya existe", exception.getMessage());
    }

    @Test
    void whenValidUser_thenSaveUser() {
        User user = new User();
        user.setEmail("nuevo@example.com");
        user.setPassword("abcdeF12");
        user.setName("Nombre");

        when(userRepository.existsByEmail("nuevo@example.com")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.save(user);

        assertNotNull(savedUser.getId());
        assertTrue(savedUser.getIsActive());
        assertNotNull(savedUser.getToken());
    }

    @Test
    void whenUserHasPhones_thenPhonesAreLinkedToUser() {
        User user = new User();
        user.setEmail("phoneuser@example.com");
        user.setPassword("abcdeF12"); // contraseña que cumple la regex
        user.setName("Usuario Con Teléfonos");

        Phone phone1 = new Phone();
        phone1.setNumber(123456789L);
        phone1.setCitycode(1);
        phone1.setContrycode("57");

        Phone phone2 = new Phone();
        phone2.setNumber(987654321L);
        phone2.setCitycode(2);
        phone2.setContrycode("58");

        List<Phone> phoneList = List.of(phone1, phone2);
        user.setPhones(phoneList);

        when(userRepository.existsByEmail("phoneuser@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Ejecutamos save(), entra en el bloque de phones y setea user en cada Phone
        User savedUser = userService.save(user);

        assertNotNull(savedUser.getPhones());
        assertEquals(2, savedUser.getPhones().size());
        for (Phone phone : savedUser.getPhones()) {
            assertEquals(savedUser, phone.getUser(), "El teléfono debería tener referenciado al usuario");
        }
    }

    @Test
    void whenValidToken_thenReturnUserWithNewToken() {
        User user = new User();
        user.setId("e5c6cf84-8860-4c00-91cd-22d3be28904e");
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("abcdeF12");
        user.setIsActive(true);
        user.setToken("token_viejo");
        user.setLastLogin(LocalDateTime.now());

        String validAuthHeader = "Bearer " + jwtUtil.generateJwtToken(user);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User loggedUser = userService.login(validAuthHeader);

        assertNotNull(loggedUser);
        assertEquals(user.getId(), loggedUser.getId());
        assertTrue(loggedUser.getIsActive());
        assertNotNull(loggedUser.getToken());
        assertNotEquals("token_viejo", loggedUser.getToken());
    }

    @Test
    void whenInvalidToken_thenThrowValidationException() {
        String invalidAuthHeader = "Bearer token_invalido";

        lenient().when(userRepository.findByToken("token_invalido")).thenReturn(Optional.empty());

        CustomValidationException ex = assertThrows(CustomValidationException.class, () -> {
            userService.login(invalidAuthHeader);
        });

        assertEquals("Token inválido o expirado", ex.getMessage());
    }

    @Test
    void whenNoBearerPrefix_thenThrowValidationException() {
        String invalidAuthHeader = "token_sin_bearer";

        CustomValidationException ex = assertThrows(CustomValidationException.class, () -> {
            userService.login(invalidAuthHeader);
        });

        assertEquals("Token inválido", ex.getMessage());
    }
}
