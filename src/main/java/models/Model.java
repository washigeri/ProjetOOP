package models;

import java.util.List;

public abstract class Model {
    private int id;

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public abstract List<Object> GetFields();
}
