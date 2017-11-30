package models;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.ArrayList;
import java.util.List;

public class User extends Model {
    private final static String seed = "seed";
    private static StandardPBEStringEncryptor stringEncryptor = null;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        if (stringEncryptor == null) {
            stringEncryptor = new StandardPBEStringEncryptor();
            stringEncryptor.setPassword(seed);
        }
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

    String getPassword() {
        return stringEncryptor.decrypt(this.password);
    }

    void setPassword(String password) {
        this.password = stringEncryptor.encrypt(password);
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
