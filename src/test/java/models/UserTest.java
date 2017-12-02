package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    void getPassword1() {
        User user = new User(1, "test", "test_pwd");
        assertEquals(user.getPassword(), "test_pwd");
    }

    @Test
    void setPassword1() {
    }


}