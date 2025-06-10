package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DemoApplication.class)
class DemoApplicationContextTest {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		// Verifica carga básica del contexto
		assertNotNull(context);
	}

	@Test
	void mainBeanExists() {
		// Verifica el bean de la clase principal
		assertTrue(context.containsBean("demoApplication"));
	}
}