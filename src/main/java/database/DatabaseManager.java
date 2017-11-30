package database;

import java.sql.*;

import models.*;

public class DatabaseManager {

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
        sqlQueries[1] = "CREATE TABLE IF NOT EXISTS Transaction(\n" +
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

    public void insert(Class object, Object... parameters) throws SQLException {
        String sql = "INSERT INTO ";
        if (object == User.class)
            sql += "User(id, username, password) VALUES(?,?,?)";
        else if (object == Category.class)
            sql += "Category(id, name) VALUES(?,?)";
        else if (object == Transaction.class) {
            sql += "Transaction(id, username_id, description, amount, creation_date," +
                    " start_date, end_date, frequency, category_id)" +
                    " VALUES(?,?,?,?,?,?,?,?,?)";
        } else
            throw new SQLException("This table does not exist.");
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        for (Object param: parameters
             ) {
            
        }
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
}
