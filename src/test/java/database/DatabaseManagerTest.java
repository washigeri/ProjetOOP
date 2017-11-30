package database;

import models.Category;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    @Test
    void insert() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        try {
            databaseManager.insert(Category.class, 1, "kek");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void connectDatabase() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        System.out.println(databaseManager.toString());
        assertEquals(true, databaseManager.isConnected());

    }

}