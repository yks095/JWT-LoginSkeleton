package me.kiseok.jwtskeleton.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TestPropertiesTest {

    @Autowired
    TestProperties testProperties;

    @DisplayName("TestProperties 테스트")
    @Test
    void test_properties()  {
        assertEquals(testProperties.getEmail(), "test@email.com");
        assertEquals(testProperties.getPassword(), "testPassword");
        assertEquals(testProperties.getName(), "testName");
        assertEquals(testProperties.getPicture(), "testPicture");
    }
}
