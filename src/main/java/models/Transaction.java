package models;

import java.time.Instant;
import java.util.Date;

public class Transaction {
    private int id;
    private User user;
    private String description;
    private float amount;
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    private int frequency;
    private Category category;

    public Transaction(int id, User user, String description, float amount,
                       Date startDate, Date endDate, int frequency, Category category) {
        this.setId(id);
        this.setUser(user);
        this.setDescription(description);
        this.setAmount(amount);
        this.setCreationDate(Date.from(Instant.now()));
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setFrequency(frequency);
        this.setCategory(category);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
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
}
