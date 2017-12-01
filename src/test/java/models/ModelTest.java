package models;

import org.junit.jupiter.api.Test;

class ModelTest {
    @Test
    void toStringTest() {
        Category category = new Category(1, "testToString");
        System.out.println(category.toString());
    }

}