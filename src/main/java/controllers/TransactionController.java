package controllers;

import database.DatabaseManager;
import models.Category;
import models.Spending;
import models.Transaction;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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

    @SuppressWarnings("unchecked")
    public static boolean DeleteTransaction(int id) {
        try {
            databaseManager.Delete(Transaction.class, id);
            ArrayList<Spending> spendings = (ArrayList<Spending>) databaseManager.SelectAll(Spending.class,
                    String.format("operation_id = %d", id));
            for (Spending spending :
                    spendings) {
                databaseManager.Delete(Spending.class, spending.getId());
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean CreateNewCategory(String name) {
        try {
            int avalaibleID = databaseManager.GetLastID(Category.class);
            Category category = new Category(avalaibleID, name);
            databaseManager.Insert(category);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void CreateNewSpending(float amount, Category category, Date date, String description,
                                          Transaction transaction)
            throws SQLException {
        int availableID = databaseManager.GetLastID(Spending.class);
        Spending spending = new Spending(availableID, amount, description, date, category, transaction);
        databaseManager.Insert(spending);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Float> Get5BiggestSpendingByCategory() {
        try {
            HashMap<String, Float> sumMap = new HashMap<>();
            HashMap<String, Float> res = new HashMap<>();
            ArrayList<Spending> spendings = (ArrayList<Spending>) databaseManager.SelectAll(Spending.class);
            HashMap<String, List<Spending>> hashMap = new HashMap<>();
            for (Spending spending :
                    spendings) {
                if (!hashMap.containsKey(spending.getCategory().getName())) {
                    List<Spending> listValue = new ArrayList<>();
                    listValue.add(spending);
                    hashMap.put(spending.getCategory().getName(), listValue);
                } else {
                    hashMap.get(spending.getCategory().getName()).add(spending);
                }
            }
            for (String key :
                    hashMap.keySet()) {
                float sum = (float) hashMap.get(key).stream().mapToDouble(Spending::getAmount).sum();
                sumMap.put(key, sum);
            }
            Map<String, Float> sortedMap = sumMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                    .collect(Collectors.
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            int k = 0;
            for (Map.Entry<String, Float> entry :
                    sortedMap.entrySet()) {
                if (k < 5) {
                    res.put(entry.getKey(), entry.getValue());
                    k++;
                } else
                    break;
            }

            return res.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                    .collect(Collectors
                            .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashMap<>(5);
        }
    }

    public static void main(String[] args) {

        Map<String, Float> res = TransactionController.Get5BiggestSpendingByCategory();
        System.out.println(res);
    }

}
