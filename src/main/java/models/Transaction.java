package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction extends Model {
    private User user;
    private String description;
    private float amount;
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    private int frequency;
    private Category category;

    public Transaction(int id, User user, String description, float amount, Date creationDate,
                       Date startDate, Date endDate, int frequency, Category category) {
        this.setId(id);
        this.setUser(user);
        this.setDescription(description);
        this.setAmount(amount);
        this.setCreationDate(creationDate);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setFrequency(frequency);
        this.setCategory(category);
    }


    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    private void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    private void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    private void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    private void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getFrequency() {
        return frequency;
    }

    private void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Category getCategory() {
        return category;
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public List<Object> GetFields() {
        ArrayList<Object> res = new ArrayList<>();
        res.add(getId());
        res.add(getUser());
        res.add(getDescription());
        res.add(getAmount());
        res.add(getCreationDate());
        res.add(getStartDate());
        res.add(getEndDate());
        res.add(getFrequency());
        res.add(getCategory());
        return res;
    }
}
