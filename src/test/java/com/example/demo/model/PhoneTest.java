package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PhoneTest {

    @Test
    void gettersAndSettersWork() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setNumber(123456789L);
        phone.setCitycode(55);
        phone.setContrycode("34");

        // initially user is null
        assertNull(phone.getUser());

        // set and get user
        User user = new User();
        user.setId("user-1");
        phone.setUser(user);
        assertSame(user, phone.getUser());

        // verify other getters
        assertEquals(1L, phone.getId());
        assertEquals(123456789L, phone.getNumber());
        assertEquals(55, phone.getCitycode());
        assertEquals("34", phone.getContrycode());
    }

    @Test
    void toStringContainsAllFields() {
        Phone phone = new Phone();
        phone.setId(2L);
        phone.setNumber(222L);
        phone.setCitycode(2);
        phone.setContrycode("2");

        // without user
        String s1 = phone.toString();
        assertTrue(s1.contains("id=2"));
        assertTrue(s1.contains("number=222"));
        assertTrue(s1.contains("citycode=2"));
        assertTrue(s1.contains("contrycode='2'"));
        assertTrue(s1.contains("user=null"));

        // with user
        User user = new User();
        user.setId("user-xyz");
        phone.setUser(user);
        String s2 = phone.toString();
        assertTrue(s2.contains("user=user-xyz"));
    }
}
