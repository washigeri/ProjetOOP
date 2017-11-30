package database;

import models.Category;
import models.Model;
import models.Transaction;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements IDatabaseManager {

    private static String path = "src/main/resources/";
    private static String fileName = "test.db";
    private static String url = "jdbc:sqlite:" + path + fileName;

    private static DatabaseManager instance = null;


    private Connection connection;
    private boolean isConnected;
    private DatabaseMetaData metaData;

    private DatabaseManager() {
        this.setConnected(false);
        this.setConnection(null);
        this.setMetaData(null);
        this.ConnectDatabase();
        this.CreateTables();
    }


    private void ConnectDatabase() {
        try {
            setConnection(DriverManager.getConnection(url));
            if (getConnection() != null) {
                setMetaData(getConnection().getMetaData());
                setConnected(true);
            } else
                setConnected(false);
        } catch (SQLException e) {
            e.printStackTrace();
            setConnected(false);
        }
    }


    private void CreateTables() {
        String[] sqlQueries = new String[3];
        sqlQueries[0] = "CREATE TABLE IF NOT EXISTS User(\n" +
                "id INTEGER PRIMARY KEY,\n" +
                "username text NOT NULL,\n" +
                "password text NOT NULL\n" +
                ");";
        sqlQueries[1] = "CREATE TABLE IF NOT EXISTS Operation(\n" +
                "id INTEGER PRIMARY KEY,\n" +
                "username_id INTEGER NOT NULL,\n" +
                "description TEXT,\n" +
                "amount REAL NOT NULL,\n" +
                "creation_date text NOT NULL,\n" +
                "start_date text,\n" +
                "end_date text,\n" +
                "frequency INTEGER,\n" +
                "category_id INTEGER NOT NULL\n" +
                ");";
        sqlQueries[2] = "CREATE TABLE IF NOT EXISTS Category(\n" +
                "id INTEGER PRIMARY KEY,\n" +
                "name text NOT NULL\n" +
                ");";
        for (String query :
                sqlQueries) {
            try {
                Statement statement = getConnection().createStatement();
                statement.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public ArrayList<Model> SelectAll(Class object) throws SQLException {
        ArrayList<Model> res = new ArrayList<>();
        String sqlQuery = "SELECT * FROM ";
        if(object == User.class)
            sqlQuery += "User";
        else if(object == Transaction.class)
            sqlQuery += "Operation";
        else if(object == Category.class)
            sqlQuery += "Category";
        else
            throw new SQLException("Unknow type name: +" + object.getSimpleName());
        Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sqlQuery);
        return null;
    }

    @Override
    public List<Model> SelectAll(Class<Model> object, String condition) {
        return null;
    }

    @Override
    public Model Select(Class object, int id) {
        return null;
    }

    @Override
    public void Update(Class object, int id, Object... parameters) {

    }

    @Override
    public void Delete(Class object, int id) {

    }
    @Override
    public void Insert(Class object, Object... parameters) throws SQLException {
        String sql = "INSERT INTO ";
        if (object == User.class)
            sql += "User(id, username, password) VALUES(?,?,?)";
        else if (object == Category.class)
            sql += "Category(id, name) VALUES(?,?)";
        else if (object == Transaction.class) {
            sql += "Operation(id, username_id, description, amount, creation_date," +
                    " start_date, end_date, frequency, category_id)" +
                    " VALUES(?,?,?,?,?,?,?,?,?)";
        } else
            throw new SQLException("This table does not exist.");
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        int paramIndex = 1;
        for (Object param : parameters
                ) {
            if (param instanceof String) {
                preparedStatement.setString(paramIndex, (String) param);
                paramIndex++;
            } else if (param instanceof Integer) {
                preparedStatement.setInt(paramIndex, (Integer) param);
                paramIndex++;
            } else if (param instanceof Double) {
                preparedStatement.setDouble(paramIndex, (Double) param);
                paramIndex++;
            } else if (param instanceof Float) {
                preparedStatement.setFloat(paramIndex, (Float) param);
                paramIndex++;
            } else if (param instanceof User) {
                preparedStatement.setInt(paramIndex, ((User) param).getId());
                paramIndex++;
            } else if (param instanceof Category) {
                preparedStatement.setInt(paramIndex, ((Category) param).getId());
                paramIndex++;
            } else {
                throw new SQLException("Unknown parameter: " + param.toString());
            }

        }
        preparedStatement.executeUpdate();
    }


    private Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    boolean isConnected() {
        return isConnected;
    }

    private void setConnected(boolean connected) {
        isConnected = connected;
    }

    private DatabaseMetaData getMetaData() {
        return metaData;
    }

    private void setMetaData(DatabaseMetaData metaData) {
        this.metaData = metaData;
    }

    static DatabaseManager getInstance() {
        if (instance == null) {
            setInstance(new DatabaseManager());
        }
        return instance;
    }

    private static void setInstance(DatabaseManager instance) {
        DatabaseManager.instance = instance;
    }

    public String toString() {
        String driverName = "";
        if (isConnected()) {

            try {
                driverName = this.getMetaData().getDriverName();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return "Database Manager " + driverName;
    }
}
