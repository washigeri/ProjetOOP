package controllers;

import database.DatabaseManager;
import database.IDatabaseManager;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import models.Spending;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Controller {

	private IDatabaseManager databaseManager = DatabaseManager.getInstance();

	public static final long MILLISINADAY = 86400000;

	@SuppressWarnings("unchecked")
	public List<Spending> GetSpendingsOverTheLastDays(int numberOfDays) {
		List<Spending> res = new ArrayList<Spending>();
		try {
			List<Spending> allSpendings = (List<Spending>) databaseManager.SelectAll(Spending.class);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			long now = c.getTimeInMillis();
			for (Spending spending : allSpendings) {
				if (now - spending.getDate().getTime() <= numberOfDays * MILLISINADAY) {
					res.add(spending);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	private int hasExtraWeek(int year) {
		int p1 = year + (year / 4) - (year / 100) + (year / 400);
		p1 = p1 % 7;
		if (p1 == 4) {
			return 1;
		} else {
			int p2 = (year - 1) + ((year - 1) / 4) - ((year - 1) / 100) + ((year - 1) / 400);
			if (p2 == 3) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Spending> GetSpendingsOverTheLastWeeks(int numberOfWeeks) {
		List<Spending> res = new ArrayList<Spending>();
		try {
			List<Spending> allSpendings = (List<Spending>) databaseManager.SelectAll(Spending.class);
			Calendar today = Calendar.getInstance();
			System.out.println("year = " + Calendar.YEAR + "   MONTH = " + Calendar.MONTH);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			Calendar c = Calendar.getInstance();
			for (Spending spending : allSpendings) {
				c.setTime(spending.getDate());
				if (today.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
					if (today.get(Calendar.WEEK_OF_YEAR) - c.get(Calendar.WEEK_OF_YEAR) <= numberOfWeeks) {
						res.add(spending);
					}
				} else if (today.get(Calendar.YEAR) > c.get(Calendar.YEAR)) {
					int weeksCount = today.get(Calendar.WEEK_OF_YEAR);
					for (int i = c.get(Calendar.YEAR); i < today.get(Calendar.YEAR); i++) {
						weeksCount += 52 + hasExtraWeek(i);
					}
					if (weeksCount - c.get(Calendar.WEEK_OF_YEAR) <= numberOfWeeks) {
						res.add(spending);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Spending> GetSpendingsOverTheLastMonths(int numberOfMonths) {
		List<Spending> res = new ArrayList<Spending>();
		try {
			List<Spending> allSpendings = (List<Spending>) databaseManager.SelectAll(Spending.class);
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			Calendar c = Calendar.getInstance();
			for (Spending spending : allSpendings) {
				c.setTime(spending.getDate());
				if (today.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
					if (today.get(Calendar.MONTH) - c.get(Calendar.MONTH) <= numberOfMonths) {
						res.add(spending);
					}
				} else if (today.get(Calendar.YEAR) > c.get(Calendar.YEAR)) {
					if (today.get(Calendar.MONTH) + 12 * (today.get(Calendar.YEAR) - c.get(Calendar.YEAR))
							- c.get(Calendar.MONTH) <= numberOfMonths) {
						res.add(spending);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Spending> GetSpendingsOverTheLastYears(int numberOfYears) {
		List<Spending> res = new ArrayList<Spending>();
		try {
			List<Spending> allSpendings = (List<Spending>) databaseManager.SelectAll(Spending.class);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			Calendar c = Calendar.getInstance();
			for (Spending spending : allSpendings) {
				c.setTime(spending.getDate());
				if (currentYear - c.get(Calendar.YEAR) <= numberOfYears) {
					res.add(spending);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public float GetAmountSpentOverTheLastDays(int numberOfDays) {
		float res = 0f;
		List<Spending> allSpendings = GetSpendingsOverTheLastDays(numberOfDays);
		for (Spending spending : allSpendings) {
			res += spending.getAmount();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public float GetSpendingsByCategoryOverTheLastDays(int numberOfDays, int categoryID) {
		float res = 0f;
		try {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			long now = c.getTimeInMillis();
			List<Spending> allSpendings = (List<Spending>) databaseManager.SelectAll(Spending.class,
					"category_id = " + categoryID);
			for (Spending spending : allSpendings) {
				if (now - spending.getDate().getTime() <= numberOfDays * MILLISINADAY) {
					res += spending.getAmount();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public LineChart<Number, Number> ShowGraphOfSpendingsOverTheLastDays(int numberOfDays) {
		float[] spendingsPerDay = new float[numberOfDays];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long today = c.getTimeInMillis();
		List<Spending> allSpendings = GetSpendingsOverTheLastDays(numberOfDays);
		for (Spending spending : allSpendings) {
			int day = (int) Math.abs((today - spending.getDate().getTime()) / MILLISINADAY);
			spendingsPerDay[numberOfDays - 1 - day] += spending.getAmount();
		}
		NumberAxis xAxis = new NumberAxis(1, numberOfDays + 1, 1);
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Nombre de jours");
		yAxis.setLabel("DÃ©penses");
		LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		lineChart.setTitle("Evolution des dÃ©penses au cours des " + numberOfDays + "derniers jours");
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		float totalSpendings = 0f;
		for (int i = 0; i < numberOfDays; i++) {
			totalSpendings += spendingsPerDay[i];
			series.getData().add(new XYChart.Data<Number, Number>(i, totalSpendings));
		}
		lineChart.getData().add(series);
		return lineChart;
	}

	// private List<Spending> GetSpendingsByMonthAndDateFromList(List<Spending>
	// spendings, int month, int year) {
	// List<Spending> matchingSpendings = new ArrayList<Spending>();
	// Calendar c = Calendar.getInstance();
	// int i = 0;
	// for (Spending spending : spendings) {
	// c.setTime(spending.getDate());
	// if (c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month) {
	// matchingSpendings.add(spending);
	// spendings.remove(i);
	// } else {
	// i++;
	// }
	// }
	// return matchingSpendings;
	// }
	//
	// private int GetFirstMonthSuperiorToMin(List<Spending> spendings, int year,
	// int min) {
	// Calendar c = Calendar.getInstance();
	// int res = 12;
	// if (spendings != null && !spendings.isEmpty()) {
	// for (Spending spending : spendings) {
	// c.setTime(spending.getDate());
	// if (c.get(Calendar.YEAR) == year) {
	// if (c.get(Calendar.MONTH) < res && c.get(Calendar.MONTH) >= min) {
	// res = c.get(Calendar.MONTH);
	// }
	// } else if (c.get(Calendar.YEAR) > year) {
	//
	// }
	// }
	// }
	// return res;
	// }

	private Date GetFirstMonthAndYearSuperiorToDate(List<Spending> spendings, int month, int year) {
		Date res = null;
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1, 0, 0, 0);
		Date min = c.getTime();
		for (Spending spending : spendings) {
			if (res == null) {
				if (spending.getDate().compareTo(min) >= 0) {
					res = spending.getDate();
				}
			} else {
				if (spending.getDate().compareTo(min) > 0 && spending.getDate().compareTo(res) < 0) {
					res = spending.getDate();
				}
			}
		}
		return res;
	}

	public LineChart<Number, Number> ShowGraphOfSpendingsOverTheLastMonths(int numberOfMonths) {
		float[] spendingsPerMonths = new float[numberOfMonths + 1];
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		List<Spending> allSpendings = GetSpendingsOverTheLastMonths(numberOfMonths);
		int minYear = today.get(Calendar.YEAR);
		int isSameYear = today.get(Calendar.MONTH) - numberOfMonths;
		if (isSameYear < 0) {
			minYear -= Math.ceil((numberOfMonths - today.get(Calendar.MONTH)) / 12.0);
		}
		int minMonth = (today.get(Calendar.MONTH) - numberOfMonths) % 12;
		while (minMonth < 0) {
			minMonth += 12;
		}
		Calendar spendingDate = Calendar.getInstance();
		Date firstDate = GetFirstMonthAndYearSuperiorToDate(allSpendings, minYear, minMonth);
		if (firstDate == null) {
			return null;
		} else {
			Calendar minDate = Calendar.getInstance();
			minDate.setTime(firstDate);
			int firstMonth = minDate.get(Calendar.MONTH);
			int spendingsSize = allSpendings.size();
			Spending spending;
			for (int i = minYear; i <= today.get(Calendar.YEAR); i++) {
				for (int j = 0; j < spendingsSize; j++) {
					spending = allSpendings.get(j);
					spendingDate.setTime(spending.getDate());
					if (spendingDate.get(Calendar.YEAR) == i) {
						spendingsPerMonths[12 * (i - minYear) + spendingDate.get(Calendar.MONTH)
								- firstMonth] += spending.getAmount();
						allSpendings.remove(j);
						j--;
						spendingsSize--;
					}
				}
			}
			NumberAxis xAxis = new NumberAxis(0, numberOfMonths + 2, 1);
			NumberAxis yAxis = new NumberAxis();
			xAxis.setLabel("Nombre de mois");
			yAxis.setLabel("Dépenses");
			LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
			lineChart.setTitle("Evolution des dépenses au cours des " + numberOfMonths + " derniers mois");
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			float totalSpendings = 0f;
			for (int i = 0; i <= numberOfMonths; i++) {
				totalSpendings += spendingsPerMonths[i];
				series.getData().add(new XYChart.Data<Number, Number>(i + 1, totalSpendings));
			}
			lineChart.getData().add(series);
			return lineChart;
		}
	}

	private Date GetFirstDateSuperiorYear(List<Spending> spendings, int year) {
		return GetFirstMonthAndYearSuperiorToDate(spendings, 0, year);
	}

	public LineChart<Number, Number> ShowGraphOfSpendingsOverTheLastYears(int numberOfYears) {
		float[] spendingsPerYears = new float[numberOfYears + 1];
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		List<Spending> allSpendings = GetSpendingsOverTheLastYears(numberOfYears);
		Date firstDate = GetFirstDateSuperiorYear(allSpendings, currentYear - numberOfYears);
		if (firstDate == null) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(firstDate);
			int firstYear = cal.get(Calendar.YEAR);
			for (Spending spending : allSpendings) {
				cal.setTime(spending.getDate());
				if(cal.get(Calendar.YEAR) >= currentYear - numberOfYears) {
					spendingsPerYears[cal.get(Calendar.YEAR) - firstYear] += spending.getAmount();
				}
			}
			NumberAxis xAxis = new NumberAxis(0, numberOfYears + 2, 1);
			NumberAxis yAxis = new NumberAxis();
			xAxis.setLabel("Nombre de mois");
			yAxis.setLabel("Dépenses");
			LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
			lineChart.setTitle("Evolution des dépenses au cours des " + numberOfYears + " dernière(s) année(s)");
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			float totalSpendings = 0f;
			for (int i = 0; i <= numberOfYears; i++) {
				totalSpendings += spendingsPerYears[i];
				series.getData().add(new XYChart.Data<Number, Number>(i + 1, totalSpendings));
			}
			lineChart.getData().add(series);
			return lineChart;
		}
	}

	public static void main(String[] args) {
		Controller controller = new Controller();
		System.out.println(controller.GetSpendingsOverTheLastYears(1).size());
	}

}
