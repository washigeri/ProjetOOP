package sample;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import database.DatabaseManager;
import database.IDatabaseManager;
import models.*;

public class Controller {
	
	private IDatabaseManager databaseManager = DatabaseManager.getInstance();
	
	public float GetSpendingsOverLastXDays(int numberOfDays)
	{
		try {
			List<Transaction> allTransactions = (List<Transaction>) databaseManager.SelectAll(Transaction.class);
			Date now = new Date();
			Date transactionDate;
			for(Transaction t : allTransactions) {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
}
