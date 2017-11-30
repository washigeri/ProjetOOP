package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    void getPassword() {
        User user = new User(1, "test", "pwd");
        assertEquals("pwd", user.getPassword());
    }

    @Test
    void setPassword() {
        User user = new User(1, "test", "pwd");
        assertEquals("pwd", user.getPassword());
        user.setPassword("pwd2");
        assertEquals("pwd2", user.getPassword());
    }

}