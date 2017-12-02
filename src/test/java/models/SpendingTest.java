package models;

import org.junit.jupiter.api.Test;

import java.util.Date;

class SpendingTest {
    @Test
    void toStringTest() {
        Spending spending = new Spending(0, 15.59f, "", new Date(),
                new Category(1, "test"),
                null);
        System.out.println(spending);
    }

}