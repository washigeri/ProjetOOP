package models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Spending extends Model {
    private float amount;
    private String description;
    private Date date;
    private Category category;
    private Transaction transaction;

    public Spending(int id, float amount, String description, Date date, Category category, Transaction transaction) {
        this.setId(id);
        this.setAmount(amount);
        this.setDescription(description);
        this.setDate(date);
        this.setCategory(category);
        this.setTransaction(transaction);
    }

    @Override
    public List<Object> GetFields() {
        ArrayList<Object> res = new ArrayList<>();
        res.add(this.getId());
        res.add(this.getAmount());
        res.add(this.getDescription());
        res.add(this.getDate());
        res.add(this.getCategory());
        res.add(this.getTransaction());
        return res;

    }

    public float getAmount() {
        return amount;
    }

    private void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
