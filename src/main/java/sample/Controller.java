package sample;

import java.util.Date;
import java.util.List;

import database.DatabaseManager;
import database.IDatabaseManager;
import models.*;

public class Controller {
	
	private IDatabaseManager databaseManager = DatabaseManager.getInstance();
	
	public float GetSpendingsOverLastXDays(int numberOfDays)
	{
		Date now = new Date();
		List<Transaction> allTransactions = databaseManager.SelectAll(Transaction.class);
	}
	
	
}
