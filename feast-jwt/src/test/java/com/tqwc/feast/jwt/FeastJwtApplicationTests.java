package com.tqwc.feast.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = FeastJwtAutoConfiguration.class)
@TestPropertySource(properties = "security.jwt.secret=01234567890123456789012345678901")
class FeastJwtApplicationTests {

    @Test
    void contextLoads() {
    }

}
