package database;

import models.Model;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseManager {

    void Insert(Model objectToInsert) throws SQLException;

    List<? extends Model> SelectAll(Class<Model> object) throws SQLException;

    List<? extends Model> SelectAll(Class<Model> object, String condition) throws SQLException;

    Model Select(Class<Model> object, int id) throws SQLException;

    void Update(Model objectToUpdate) throws SQLException;

    void Delete(Class<Model> object, int id) throws SQLException;

    int GetLastID(Class<Model> object) throws SQLException;
}
