package controllers;

import database.DatabaseManager;
import database.IDatabaseManager;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import models.Category;
import models.Spending;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SpendingController {

    public static final long MILLISINADAY = 86400000;
    private static IDatabaseManager databaseManager = DatabaseManager.getInstance();

    @SuppressWarnings("unchecked")
    public static List<Spending> GetSpendingsOverTheLastDays(int numberOfDays) {
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

    private static int hasExtraWeek(int year) {
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
    public static List<Spending> GetSpendingsOverTheLastWeeks(int numberOfWeeks) {
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
    public static List<Spending> GetSpendingsOverTheLastMonths(int numberOfMonths) {
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
    public static List<Spending> GetSpendingsOverTheLastYears(int numberOfYears) {
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

    @SuppressWarnings("unchecked")
	public static HashMap<Integer, Float> GetSpendingsByCategoryDuringPeriodOfYear(int period, int periodIndex, int year){
    	HashMap<Integer, Float> res = new HashMap<Integer, Float>();
    	try {
    		List<Category> allCategories = TransactionController.GetAllCategories();
    		for(Category category : allCategories) {
    			res.put(category.getId(), 0f);
    		}
			List<Spending> allSpendings = (List<Spending>) databaseManager.SelectAll(Spending.class);
			Calendar spendingCal = Calendar.getInstance();
            spendingCal.setTime(new Date());
            int currentYear = spendingCal.get(Calendar.YEAR);
            int currentPeriod = (periodIndex == Calendar.MONTH) ? spendingCal.get(Calendar.MONTH)
                    : spendingCal.get(Calendar.WEEK_OF_YEAR);
            boolean isCurrentPeriod = (currentYear == year && currentPeriod == period);
            for(Spending spending : allSpendings)
            {
            	spendingCal.setTime(spending.getDate());
                if (spendingCal.get(Calendar.YEAR) == year && spendingCal.get(periodIndex) == period) {
                    if (!isCurrentPeriod) {
                        res.put(spending.getCategory().getId(), res.get(spending.getCategory().getId()) + spending.getAmount());
                    } else {
                        if (spendingCal.getTimeInMillis() <= System.currentTimeMillis()) {
                            res.put(spending.getCategory().getId(), res.get(spending.getCategory().getId()) + spending.getAmount());
                        }
                    }
                }
            }
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return res;
    }
    
    
    public static float GetAmountSpentOverTheLastDays(int numberOfDays) {
        float res = 0f;
        List<Spending> allSpendings = GetSpendingsOverTheLastDays(numberOfDays);
        for (Spending spending : allSpendings) {
            res += spending.getAmount();
        }
        return res;
    }

    public static float GetAmountSpentOverTheLastWeek() {
        List<Spending> spendings = GetSpendingsOverTheLastWeeks(0);
        float res = 0f;
        for (Spending spending : spendings) {
            res += spending.getAmount();
        }
        return res;
    }

    public static float GetAmountSpentOverTheLastMonth() {
        List<Spending> spendings = GetSpendingsOverTheLastMonths(0);
        float res = 0f;
        for (Spending spending : spendings) {
            res += spending.getAmount();
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public static List<Spending> GetSpendingsDuringPeriodOfYear(int period, int periodIndex, int year) {
        List<Spending> res = new ArrayList<Spending>();
        try {
            List<Spending> allSpendings = (List<Spending>) databaseManager.SelectAll(Spending.class);
            Calendar spendingCal = Calendar.getInstance();
            spendingCal.setTime(new Date());
            int currentYear = spendingCal.get(Calendar.YEAR);
            int currentPeriod = (periodIndex == Calendar.MONTH) ? spendingCal.get(Calendar.MONTH)
                    : spendingCal.get(Calendar.WEEK_OF_YEAR);
            boolean isCurrentPeriod = (currentYear == year && currentPeriod == period);
            for (Spending spending : allSpendings) {
                spendingCal.setTime(spending.getDate());
                if (spendingCal.get(Calendar.YEAR) == year && spendingCal.get(periodIndex) == period) {
                    if (!isCurrentPeriod) {
                        res.add(spending);
                    } else {
                        if (spendingCal.getTimeInMillis() <= System.currentTimeMillis()) {
                            res.add(spending);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static XYChart.Series<Number, Number> GetSeriesOfSpendingsDuringPeriodOfYear(int period, int periodIndex, int year) {
		int size;
		int weekOrMonth;
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		today.set(Calendar.YEAR, year);
		if (periodIndex == Calendar.MONTH) {
			size = 31;
			weekOrMonth = Calendar.DAY_OF_MONTH;
			today.set(periodIndex, period);
		} else if (periodIndex == Calendar.WEEK_OF_YEAR || periodIndex == Calendar.WEEK_OF_MONTH) {
			size = 7;
			weekOrMonth = Calendar.DAY_OF_WEEK;
			today.set(periodIndex, period);
		} else {
			size = 0;
			weekOrMonth = 0;
		}
		float[] spendingsPerDays = new float[size];

		Calendar spendingCal = Calendar.getInstance();
		List<Spending> allSpendings = GetSpendingsDuringPeriodOfYear(period, periodIndex, year);
		for (Spending spending : allSpendings) {
			spendingCal.setTime(spending.getDate());
			if (spendingCal.get(Calendar.YEAR) == year && spendingCal.get(periodIndex) == period) {
				spendingsPerDays[spendingCal.get(weekOrMonth) - 1] += spending.getAmount();
			}
		}
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		float totalSpendings = 0f;
		int seriesLength = (year == Calendar.getInstance().get(Calendar.YEAR) && period == Calendar.getInstance().get(periodIndex))?Calendar.getInstance().get(weekOrMonth):size;
		for (int i = 0; i < seriesLength; i++) {
			totalSpendings += spendingsPerDays[i];
			series.getData().add(new XYChart.Data<Number, Number>(i + 1, totalSpendings));
		}
		return series;
	}

    private static int GetTotalSpendings(XYChart.Series<Number, Number> serie) {
        float res = 0;
        for (Data<Number, Number> data : serie.getData()) {
            res = data.getYValue().floatValue();
        }
        return (int) (res * 1.1);

    }

	public static LineChart<Number, Number> GetChartOfSpendingsDuringPeriodOfYear(int period, int periodIndex, int year)
	{
		int size;
		String title;
		if (periodIndex == Calendar.MONTH) {
			size = 31;
			title = "Evolution des dépenses au cours du mois " + period + " de " + year;
		} else if (periodIndex == Calendar.WEEK_OF_YEAR || periodIndex == Calendar.WEEK_OF_MONTH) {
			size = 7;
			title = "Evolution des dépenses au cours de la semaine " + period + " de " + year;
		} else {
			size = 0;
			title = "";
		}
		XYChart.Series<Number, Number> serie = GetSeriesOfSpendingsDuringPeriodOfYear(period, periodIndex, year);
		NumberAxis xAxis = new NumberAxis(0, size + 1, 1);
		int totalSpendings = GetTotalSpendings(serie);
		NumberAxis yAxis = new NumberAxis(0, totalSpendings, totalSpendings / 10);
		xAxis.setLabel("Nombre de jours");
		yAxis.setLabel("Dépenses");
		LineChart<Number, Number> res = new LineChart<Number, Number>(xAxis, yAxis);
		res.setTitle(title);
		res.getData().add(serie);
		return res;
	}
    
	public static LineChart<Number, Number> AddSpendingsDuringPeriodOfYearToLineChart(LineChart<Number, Number> lineChart, int period, int periodIndex, int year)
	{
		XYChart.Series<Number, Number> serieToAdd = GetSeriesOfSpendingsDuringPeriodOfYear(period, periodIndex, year);
		if(lineChart.getData().size() == 2) {
			lineChart.getData().remove(1);
		}
		lineChart.getData().add(serieToAdd);
		return lineChart;
	}
	
}
