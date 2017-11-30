package database;

import models.Model;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseManager {

    void Insert(Model objectToInsert) throws SQLException;

    List<? extends Model> SelectAll(Class<? extends Model> object) throws SQLException;

    List<? extends Model> SelectAll(Class<? extends Model> object, String condition) throws SQLException;

    Model Select(Class<? extends Model> object, int id) throws SQLException;

    void Update(Model objectToUpdate) throws SQLException;

    void Delete(Class<? extends Model> object, int id) throws SQLException;

    int GetLastID(Class<? extends Model> object) throws SQLException;
}
