package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static String path = "src/main/resources/";
    private static String fileName = "test.db";
    private static String url = "jdbc:sqlite:" + path + fileName;

    private static DatabaseManager instance = null;


    private Connection connection;
    private boolean isConnected;
    private DatabaseMetaData metaData;

    private DatabaseManager() {
        this.ConnectDatabase();
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public static void setInstance(DatabaseManager instance) {
        DatabaseManager.instance = instance;
    }

    private void ConnectDatabase() {
        try {
            setConnection(DriverManager.getConnection(url));
            if (getConnection() != null) {
                setMetaData(getConnection().getMetaData());
                setConnected(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public DatabaseMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(DatabaseMetaData metaData) {
        this.metaData = metaData;
    }
}
