package database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    @Test
    void connectDatabase() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        System.out.println(databaseManager.toString());
        assertEquals(true, databaseManager.isConnected());

    }

}