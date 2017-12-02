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

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Object field :
                this.GetFields()) {
            if (field != null) {
                res.append(field.toString()).append(" -- ");
            }
        }
        return res.toString();
    }
}
