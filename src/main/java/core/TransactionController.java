package core;

import database.DatabaseManager;
import models.Category;
import models.Spending;
import models.Transaction;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionController {
    private static DatabaseManager databaseManager = DatabaseManager.getInstance();

    public static List<Category> GetAllCategories() {
        try {
            return (List<Category>) databaseManager.SelectAll(Category.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean CreateNewTransaction(float amount, Category category, int frequency, String description,
                                               User user, Date startDate, Date endDate) {
        try {
            int availableID = databaseManager.GetLastID(Transaction.class);
            Transaction transaction = new Transaction(availableID, user, description, amount, new Date(), startDate, endDate,
                    frequency, category);
            databaseManager.Insert(transaction);
            if (frequency > 0) {
                int inBetweenDays = Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();
                DateTime startDateJ = new DateTime(startDate);
                for (int i = 0; i < inBetweenDays; i += frequency) {
                    DateTime newDate = startDateJ.plusDays(i * frequency);
                    TransactionController.CreateNewSpending(transaction.getAmount(), transaction.getCategory(), newDate.toDate(),
                            transaction.getDescription()
                            , transaction);
                }
                return true;
            } else {
                TransactionController.CreateNewSpending(transaction.getAmount(), transaction.getCategory(),
                        transaction.getCreationDate(), transaction.getDescription(), transaction);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void DeleteTransaction(int id) {
        try {
            databaseManager.Delete(Transaction.class, id);
            ArrayList<Spending> spendings = (ArrayList<Spending>) databaseManager.SelectAll(Spending.class,
                    String.format("operation_id = %d", id));
            for (Spending spending :
                    spendings) {
                databaseManager.Delete(Spending.class, spending.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void CreateNewSpending(float amount, Category category, Date date, String description, Transaction transaction)
            throws SQLException {
        int availableID = databaseManager.GetLastID(Spending.class);
        Spending spending = new Spending(availableID, amount, description, date, category, transaction);
        databaseManager.Insert(spending);
    }


}
