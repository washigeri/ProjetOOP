package models;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public static String mapFrequencyToHuman(int frequency) {
        StringBuilder res = new StringBuilder();
        int semaines = frequency / 7;
        int jours = frequency % 7;
        if (semaines > 0) {
            res.append(String.format("%d semaine%s", semaines, (semaines > 1) ? "s" : ""));
        }
        if (semaines > 0 && jours > 0)
            res.append(" et ");
        if (jours > 0) {
            res.append(String.format("%d jour%s", jours, (jours > 1) ? "s" : ""));
        }

        return res.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction créée le ").append(new SimpleDateFormat("dd/MM/YYYY")
                .format(creationDate)).append(" du ")
                .append(new SimpleDateFormat("dd/MM/YYYY")
                        .format(startDate))
                .append(" au ")
                .append(new SimpleDateFormat("dd/MM/YYYY")
                        .format(endDate)).append(" pour un montant de ")
                .append(amount).append(".");
        return sb.toString();
    }

    public String toString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Créée le : ")
                .append(new SimpleDateFormat("dd/MM/YY").format(creationDate))
                .append(" à ")
                .append(new SimpleDateFormat("hh:mm").format(creationDate))
                .append(" ");
        if (this.getDescription() != null && !Objects.equals(this.getDescription(), "")) {
            sb.append("-- ")
                    .append(StringUtils.abbreviate(this.getDescription(), 15))
                    .append(" -- ");
        }
        sb.append("D: ")
                .append(new SimpleDateFormat("dd/MM/yy").format(startDate))
                .append(" F: ")
                .append(new SimpleDateFormat("dd/MM/yy").format(endDate))
                .append(" Catégorie: ")
                .append(this.getCategory().getName());
        if (this.getFrequency() != 0) {
            sb.append(" Fréquence: ")
                    .append(Transaction.mapFrequencyToHuman(this.getFrequency()));
        }
        sb.append(" Montant: ")
                .append(String.format("%.2f", this.getAmount()))
        ;
        return sb.toString();
    }

}
