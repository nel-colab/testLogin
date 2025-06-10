package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void gettersAndSettersWork() {
        UserEntity user = new UserEntity();
        user.setId("abc-123");
        user.setName("Nombre");
        user.setEmail("user@example.com");
        user.setPassword("AbcdE12");  // Aquí solo testeamos getter/setter
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setLastLogin(now.plusDays(1));
        user.setToken("tok");
        user.setIsActive(true);

        assertEquals("abc-123", user.getId());
        assertEquals("Nombre", user.getName());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("AbcdE12", user.getPassword());
        assertEquals(now, user.getCreated());
        assertEquals(now.plusDays(1), user.getLastLogin());
        assertEquals("tok", user.getToken());
        assertTrue(user.getIsActive());

        // phones por defecto es null
        assertNull(user.getPhones());
    }

    @Test
    void toStringContainsFields() {
        UserEntity u = new UserEntity();
        u.setId("xyz");
        u.setName("Test");
        u.setEmail("t@example.com");
        u.setPassword("AbcdE12");
        u.setIsActive(true);

        String s = u.toString();
        assertTrue(s.contains("id='xyz'"));
        assertTrue(s.contains("name='Test'"));
        assertTrue(s.contains("email='t@example.com'"));
        // Para la contraseña solo que exista, no mostramos contenido real
        assertTrue(s.contains("password='[PROTECTED]'"));
        assertTrue(s.contains("isActive=true"));
    }

    @Test
    void phonesAssignmentAndUserLink() {
        UserEntity u = new UserEntity();
        u.setId("user1");
        PhoneEntity p1 = new PhoneEntity();
        p1.setId(1L);
        p1.setNumber(123456L);
        p1.setCitycode(1);
        p1.setContrycode("57");
        // En el modelo Phone, no se asigna usuario automáticamente (ojo)
        p1.setUser(u);

        PhoneEntity p2 = new PhoneEntity();
        p2.setId(2L);
        p2.setNumber(654321L);
        p2.setCitycode(2);
        p2.setContrycode("58");
        p2.setUser(u);

        List<PhoneEntity> phoneList = new ArrayList<>();
        phoneList.add(p1);
        phoneList.add(p2);
        u.setPhones(phoneList);

        assertNotNull(u.getPhones());
        assertEquals(2, u.getPhones().size());

        for (PhoneEntity phone : u.getPhones()) {
            assertSame(u, phone.getUser());
        }
    }

    @Test
    void phonesListCanBeNullOrEmpty() {
        UserEntity u = new UserEntity();

        // phones inicialmente null
        assertNull(u.getPhones());

        // Asignar lista vacía
        u.setPhones(new ArrayList<>());
        assertNotNull(u.getPhones());
        assertTrue(u.getPhones().isEmpty());
    }
}
