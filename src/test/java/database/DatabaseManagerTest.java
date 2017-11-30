package database;

import models.Category;
import models.Model;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseManagerTest {
    @Test
    void delete() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        try {
            databaseManager.Delete(Category.class, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void selectAll() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        ArrayList<Model> res = new ArrayList<>();
        try {
            res = databaseManager.SelectAll(Category.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(1, res.size());
        System.out.println(res.get(0));
    }

    @Test
    void insert() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        try {
            databaseManager.Insert(new Category(1, "kek"));
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