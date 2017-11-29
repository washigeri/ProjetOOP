package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static String path = "src/main/resources/";

    public static void CreateDatabase(String filename){
        String url = "jdbc:sqlite:" + path + filename;
        try{
            Connection connection  = DriverManager.getConnection(url);
            if(connection != null){
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println(meta.getDriverName());
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        CreateDatabase("test.db");
    }
}
