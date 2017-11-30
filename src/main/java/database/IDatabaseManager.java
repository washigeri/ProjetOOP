package database;

import java.sql.ResultSet;

public interface IDatabaseManager {
    void ConnectDatabase();

    void CreateTables();

    void Insert(Class object, Object... parameters);

    Object[] SelectAll(Class object);

    Object[] SelectAll(Class object, String condition);

    Object Select(Class object, int id);

    void Update(Class object,int id,  Object... parameters);

    void Delete(Class object, int id);
}
