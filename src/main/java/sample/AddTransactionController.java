package sample;

import database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Category;
import models.Spending;
import models.Transaction;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.SQLException;
import java.util.Date;

public class AddTransactionController {

	private static DatabaseManager databaseManager = DatabaseManager.getInstance();

	@FXML
	public TextField TextField_AddTransaction_Montant;

	@FXML
	public TextField TextField_AddTransaction_Repetition;

	@FXML
	public TextArea TextArea_AddTransaction_Description;

	@FXML
	public Button Button_AddTransaction_AddTransaction;

	@FXML
	public Button Button_AddTransaction_AddCategorie;

	@FXML
	public ChoiceBox<String> ChoiceBox_AddTransaction_Categorie;

	@FXML
	public ChoiceBox<String> ChoiceBox_AddTransaction_Repetition;

	@FXML
	public DatePicker DatePicker_AddTransaction_Start;

	@FXML
	public DatePicker DatePicker_AddTransaction_End;

	public AddTransactionController() {

	}

	private static void CreateNewSpending(float amount, Category category, Date date, String description,
                                          Transaction transaction) throws SQLException {
        int availableID = databaseManager.GetLastID(Spending.class);
		Spending spending = new Spending(availableID, amount, description, date, category, transaction);
		databaseManager.Insert(spending);
	}

	public static boolean CreateNewTransaction(float amount, Category category, int frequency, String description,
                                               User user, Date startDate, Date endDate) {
        try {
			int availableID = databaseManager.GetLastID(Transaction.class);
			Transaction transaction = new Transaction(availableID, user, description, amount, new Date(), startDate,
					endDate, frequency, category);
			databaseManager.Insert(transaction);
			if (frequency > 0) {
				int inBetweenDays = Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();
				DateTime startDateJ = new DateTime(startDate);
				for (int i = 0; i < inBetweenDays; i += frequency) {
					DateTime newDate = startDateJ.plusDays(i * frequency);
					CreateNewSpending(transaction.getAmount(), transaction.getCategory(), newDate.toDate(),
							transaction.getDescription(), transaction);
				}
				return true;
			} else {
				CreateNewSpending(transaction.getAmount(), transaction.getCategory(), transaction.getCreationDate(),
						transaction.getDescription(), transaction);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

    @FXML
    private void handleAddTransactionButtonAction() {
        // Button was clicked, do something...
        System.out.println("Button AddTransaction Clicked");
        System.out.println(TextField_AddTransaction_Montant.getText());
        System.out.println(TextField_AddTransaction_Repetition.getText());
    }

    @FXML
    private void handleAddCategoryButtonAction() {
        // Button was clicked, do something...
        System.out.println("Button AddCategory Clicked");

    }
}
