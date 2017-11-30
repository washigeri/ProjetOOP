package models;

import java.util.ArrayList;
import java.util.List;

public class Category extends Model {
    private String name;

    public Category(int id, String name) {
        this.setId(id);
        this.setName(name);
    }


    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Object> GetFields() {
        ArrayList<Object> res = new ArrayList<>();
        res.add(getId());
        res.add(getName());
        return res;
    }
}
