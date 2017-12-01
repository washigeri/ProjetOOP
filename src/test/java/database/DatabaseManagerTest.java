package database;

import models.*;
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
    static void destroyDBFile() {
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
            databaseManager.Insert(new Category(1, "test_category"));
            databaseManager.Delete(Category.class, 1);
            assertEquals(databaseManager.SelectAll(Category.class).size(), 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getLastID() {
        try {
            assertEquals(databaseManager.GetLastID(User.class), 0);
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
    void insert() {
        try {
            databaseManager.Insert(new Category(1, "test"));
            assertEquals(databaseManager.SelectAll(Transaction.class).size(), 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void selectAll() {
        ArrayList<? extends Model> res = new ArrayList<>();
        try {
            res = databaseManager.SelectAll(Spending.class);
            List<Spending> transactions = (List<Spending>) databaseManager.SelectAll(Spending.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(0, res.size());
    }

    @AfterEach
    void resetManager() {
        this.databaseManager.DropTables();
        //this.databaseManager = null;

    }
}