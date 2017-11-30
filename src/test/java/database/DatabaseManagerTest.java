package database;

import models.Category;
import models.Model;
import models.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseManagerTest {
    DatabaseManager databaseManager;

    @AfterAll
    static void destroyDB() {

        try {
            Files.delete(Paths.get(DatabaseManager.path + DatabaseManager.fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setDb() {
        DatabaseManager.path = "src/test/java/database/";
        DatabaseManager.fileName = "testdb.db";
        databaseManager = DatabaseManager.getInstance();
    }

    @Test
    void delete() {
        try {
            databaseManager.Delete(Category.class, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getLastID() {
        try {
            assertEquals(databaseManager.GetLastID(Category.class), 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    void insert() {
        try {
            databaseManager.Insert(new Category(1, "test"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void connectDatabase() {
        System.out.println(databaseManager.toString());
        assertEquals(true, databaseManager.isConnected());

    }

    @Test
    void selectAll() {
        ArrayList<? extends Model> res = new ArrayList<>();
        try {
            res = databaseManager.SelectAll(Category.class);
            List<Transaction> transactions = (List<Transaction>) databaseManager.SelectAll(Transaction.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(1, res.size());
        System.out.println(res.get(0));
    }

    @AfterEach
    void resetManager() {
        this.databaseManager = null;
    }
}