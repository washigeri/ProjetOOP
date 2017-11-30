package models;

public class User extends Model {
    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        setId(id);
        setPassword(password);
        setUsername(username);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
