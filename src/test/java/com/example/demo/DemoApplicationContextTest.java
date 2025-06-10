package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(classes = DemoApplication.class)
class DemoApplicationContextTest {

	@Test
    public void applicationContextTest() {
        DemoApplication.main(new String[] {});
    }
}