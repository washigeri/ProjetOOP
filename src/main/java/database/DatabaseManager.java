package database;

import models.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseManager implements IDatabaseManager {

    static String path = "src/main/resources/";
    static String fileName = "test.db";
    private static String url = null;

    private static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    private static DatabaseManager instance = null;


    private Connection connection;
    private boolean isConnected;
    private DatabaseMetaData metaData;

    private DatabaseManager() {
        if (url == null)
            url = "jdbc:sqlite:" + path + fileName;
        this.setConnected(false);
        this.setConnection(null);
        this.setMetaData(null);
        this.ConnectDatabase();
        this.CreateTables();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            setInstance(new DatabaseManager());
        }
        return instance;
    }

    private static void setInstance(DatabaseManager instance) {
        DatabaseManager.instance = instance;

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
        String[] sqlQueries = new String[4];
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
                "name text UNIQUE NOT NULL\n" +
                ");";
        sqlQueries[3] = "CREATE TABLE IF NOT EXISTS Spending(\n" +
                "id INTEGER PRIMARY KEY,\n" +
                "amount REAL NOT NULL,\n" +
                "description text,\n" +
                "date text NOT NULL\n," +
                "category_id NOT NULL\n" +
                ");";
        try {
            this.ExecuteCreateDropQueries(sqlQueries);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void DropTables() {
        String[] sqlQueries = new String[4];
        sqlQueries[0] = "DROP TABLE IF EXISTS User";
        sqlQueries[1] = "DROP TABLE IF EXISTS Category";
        sqlQueries[2] = "DROP TABLE IF EXISTS Operation";
        sqlQueries[3] = "DROP TABLE IF EXISTS Spending";
        try {
            this.ExecuteCreateDropQueries(sqlQueries);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void ExecuteCreateDropQueries(String[] sqlQueries) throws SQLException {
        for (String query :
                sqlQueries) {

            Statement statement = getConnection().createStatement();
            statement.execute(query);

        }
    }

    @Override
    public ArrayList<? extends Model> SelectAll(Class<? extends Model> object) throws SQLException {
        String sqlQuery = BuildSelectQueryString(object);
        return DoSelect(object, sqlQuery);
    }

    @Override
    public ArrayList<? extends Model> SelectAll(Class<? extends Model> object, String condition) throws SQLException {
        String sqlQuery = BuildSelectQueryString(object);
        sqlQuery += "WHERE ";
        sqlQuery += condition;
        return DoSelect(object, sqlQuery);
    }

    @Override
    public Model Select(Class<? extends Model> object, int id) throws SQLException {
        String condition = String.format("id = %d", id);
        List<? extends Model> resQuery = SelectAll(object, condition);
        if (resQuery != null && resQuery.size() == 1) {
            return resQuery.get(0);
        } else
            throw new SQLException("Error in Select, couldn't find object with id : " + id);
    }

    @Override
    public void Update(Model objectToUpdate) throws SQLException {
        String tableName;
        String sqlQuery = "UPDATE %s SET ";
        List<Object> parameters;
        if (objectToUpdate.getClass() == User.class) {
            tableName = "User";
            parameters = objectToUpdate.GetFields();
            sqlQuery += "username = ?, password = ?";
        } else if (objectToUpdate.getClass() == Transaction.class) {
            tableName = "Operation";
            parameters = objectToUpdate.GetFields();
            sqlQuery += "username_id = ?, description = ?, amount = ?," +
                    "creation_date = ?, start_date = ?, end_date = ?, " +
                    "frequency = ?, category_id = ?";
        } else if (objectToUpdate.getClass() == Category.class) {
            tableName = "Category";
            parameters = objectToUpdate.GetFields();
            sqlQuery += "name = ?";
        } else if (objectToUpdate.getClass() == Spending.class) {
            tableName = "Spending";
            parameters = objectToUpdate.GetFields();
            sqlQuery += "amount = ?, description = ?, date = ?, category_id = ?";
        } else
            throw new SQLException("Unknown type name: +" + objectToUpdate.getClass().getSimpleName());
        sqlQuery = String.format(sqlQuery, tableName);
        PreparedStatement preparedStatement = this.getConnection().prepareStatement(sqlQuery);
        this.SetParamsInQuery(preparedStatement, parameters);
        preparedStatement.executeUpdate();
    }

    @Override
    public void Delete(Class<? extends Model> object, int id) throws SQLException {
        String tableName = this.MatchTableToClass(object);
        String sqlQuery = String.format("DELETE FROM %s WHERE id = ?", tableName);
        PreparedStatement preparedStatement = this.getConnection().prepareStatement(sqlQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void Insert(Model objectToInsert) throws SQLException {
        String sql = "INSERT INTO ";
        if (objectToInsert.getClass() == User.class)
            sql += "User(id, username, password) VALUES(?,?,?)";
        else if (objectToInsert.getClass() == Category.class)
            sql += "Category(id, name) VALUES(?,?)";
        else if (objectToInsert.getClass() == Transaction.class) {
            sql += "Operation(id, username_id, description, amount, creation_date," +
                    " start_date, end_date, frequency, category_id)" +
                    " VALUES(?,?,?,?,?,?,?,?,?)";
        } else if (objectToInsert.getClass() == Spending.class)
            sql += "Spending(id, amount, description, date, category_id) VALUES(?,?,?,?,?)";
        else
            throw new SQLException("This table does not exist.");
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        List<Object> parameters = objectToInsert.GetFields();
        this.SetParamsInQuery(preparedStatement, parameters);
        preparedStatement.executeUpdate();
    }

    @Override
    public int GetLastID(Class<? extends Model> object) throws SQLException {
        ArrayList<Integer> idList = new ArrayList<>();
        String queryString = "SELECT id FROM ";
        queryString += this.MatchTableToClass(object);
        Statement statement = this.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(queryString);
        while (rs.next()) {
            idList.add(rs.getInt("id"));
        }
        if (idList.size() == 0)
            return 0;
        else {
            return 1 + Collections.max(idList);
        }
    }

    private String BuildSelectQueryString(Class<? extends Model> object) throws SQLException {
        String sqlQuery = "SELECT * FROM ";
        sqlQuery += this.MatchTableToClass(object);
        return sqlQuery;
    }

    private String MatchTableToClass(Class<? extends Model> object) throws SQLException {
        String tableName;
        if (object == User.class)
            tableName = "User";
        else if (object == Transaction.class)
            tableName = "Operation";
        else if (object == Category.class)
            tableName = "Category";
        else if (object == Spending.class)
            tableName = "Spending";
        else
            throw new SQLException("Unknown type name: +" + object.getSimpleName());
        return tableName;
    }

    private void SetParamsInQuery(PreparedStatement preparedStatement, List<Object> parameters) throws SQLException {
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
            } else if (param instanceof java.util.Date) {
                preparedStatement.setString(paramIndex, df.format((java.util.Date) param));
                paramIndex++;
            } else {
                throw new SQLException("Unknown parameter: " + param.toString());
            }
        }
    }

    private ArrayList<? extends Model> DoSelect(Class<? extends Model> object, String sqlQuery) throws SQLException {
        ArrayList<Model> res = new ArrayList<>();
        Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sqlQuery);
        while (rs.next()) {
            Model result = null;
            if (object == User.class) {
                result = new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"));
            } else if (object == Category.class) {
                result = new Category(rs.getInt("id"), rs.getString("name"));
            } else if (object == Transaction.class) {

                try {
                    result = new Transaction(rs.getInt("id"), (User) this.Select(User.class, rs.getInt("username_id")),
                            rs.getString("description"), rs.getFloat("amount"), df.parse(rs.getString("creation_date")),
                            df.parse(rs.getString("start_date")),
                            df.parse(rs.getString("end_date")), rs.getInt("frequency"), (Category) this.Select(Category.class, rs.getInt("category_id")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (object == Spending.class) {
                try {
                    result = new Spending(rs.getInt("id"), rs.getFloat("amount"),
                            rs.getString("description"), df.parse(rs.getString("date")),
                            (Category) this.Select(Category.class, rs.getInt("category_id")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else
                throw new SQLException("Result of unknown type in SelectAll");
            res.add(result);
        }
        return res;
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

    @Override
    protected void finalize() throws Throwable {
        if (this.isConnected()) {
            this.getConnection().close();
        }
        super.finalize();
    }
}
