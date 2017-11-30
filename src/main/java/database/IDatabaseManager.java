package database;

import models.Model;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseManager {

    void Insert(Class<Model> object, Object... parameters) throws SQLException;

    List<Model> SelectAll(Class<Model> object) throws SQLException;

    List<Model> SelectAll(Class<Model> object, String condition) throws SQLException;

    Model Select(Class<Model> object, int id) throws SQLException;

    void Update(Class<Model> object, int id, Object... parameters) throws SQLException;

    void Delete(Class<Model> object, int id) throws SQLException;
}
