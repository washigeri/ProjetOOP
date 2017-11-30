package models;

import java.util.ArrayList;
import java.util.List;

public class User extends Model {
    private String username;
    private String password;

    public User(int id, String username, String password) {
        setId(id);
        setPassword(password);
        setUsername(username);
    }


    private String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<Object> GetFields() {
        List<Object> res = new ArrayList<>();
        res.add(getId());
        res.add(getUsername());
        res.add(getPassword());
        return res;
    }
}
