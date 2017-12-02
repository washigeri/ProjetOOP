 package views;

 import controllers.Controller;
 import controllers.SpendingController;
 import controllers.TransactionController;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.geometry.Side;
 import javafx.scene.Node;
 import javafx.scene.chart.LineChart;
 import javafx.scene.chart.PieChart;
 import javafx.scene.control.*;
 import javafx.scene.layout.HBox;
 import javafx.scene.layout.VBox;
 import javafx.scene.text.Text;
 import javafx.scene.text.TextAlignment;
 import models.Category;
 import models.Spending;
 import models.Transaction;

 import java.time.ZoneId;
 import java.util.*;

public class TabHandler {

    public static ViewHandler viewHandler;

    public static void RefreshTab(Tab selectedTab) {
        TabHandler.ExecuteCorrespondingTabLoad(selectedTab);
    }

    public static void TabChangeHandler() {
        viewHandler.tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    TabHandler.ExecuteCorrespondingTabLoad(newValue);
                }
        );
    }

    private static void ExecuteCorrespondingTabLoad(Tab selectedTab) {
        switch (selectedTab.getId()) {
            case "Tab_Resume":
                TabHandler.LoadSummaryTab();
                break;
            case "Tab_AddTransaction":
                TabHandler.LoadAddTransactionTab();
                break;
            case "Tab_Suivi":
                LoadSuiviTab();
                break;
            case "Tab_Historique":
                TabHandler.LoadHistoryTab();
                break;
        }
    }

    private static void LoadAddTransactionTab() {
        viewHandler.ChoiceBox_AddTransaction_Categorie.getItems().clear();
        ArrayList<Category> categories = (ArrayList<Category>) TransactionController.GetAllCategories();
        for (Category category :
                categories) {
            viewHandler.ChoiceBox_AddTransaction_Categorie.getItems().add(
                    new IntStringPair(category.getId(), category.getName()));
        }
        viewHandler.ChoiceBox_AddTransaction_Categorie.getSelectionModel().selectFirst();
        viewHandler.DatePicker_AddTransaction_Start.setValue(new Date()
                .toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
        viewHandler.ChoiceBox_AddTransaction_Repetition.getItems().clear();
        viewHandler.ChoiceBox_AddTransaction_Repetition.getItems().addAll("Jours", "Semaines");
        viewHandler.ChoiceBox_AddTransaction_Repetition.getSelectionModel().selectFirst();
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) TransactionController.GetAllActiveTransactions();
        ListView<HBox> listView = viewHandler.TransactionList;
        listView.getItems().clear();
        for (Transaction transaction :
                transactions) {
            Text text = new Text(transaction.toString());
            Button button = new Button("Supprimer");
            button.setOnAction(event -> TransactionController.DeleteTransaction(transaction.getId()));
            HBox hBox = new HBox();
            hBox.getChildren().addAll(text, button);
            listView.getItems().add(hBox);
        }
    }

    public static void Start() {
        LoadSummaryTab();
    }

    private static void LoadSummaryTab() {
        viewHandler.Text_Resume_Hebdo.setText("$ " + Controller.GetAmountSpentOverTheLastWeek());
        viewHandler.Text_Resume_Mensuel.setText("$ " + Controller.GetAmountSpentOverTheLastMonth());
        viewHandler.Anchor_Resume_LineChart.getChildren().clear();
        LineChart<Number, Number> x = SpendingController.GetChartOfSpendingsDuringPeriodOfYear(Calendar.getInstance().get(Calendar.MONTH), Calendar.MONTH, Calendar.getInstance().get(Calendar.YEAR));
        x.setPrefWidth(viewHandler.Anchor_Resume_LineChart.getWidth());
        x.setPrefHeight(viewHandler.Anchor_Resume_LineChart.getHeight());
        viewHandler.Anchor_Resume_LineChart.getChildren().add(x);
    }

    private static void LoadSuiviTab() {
    	//viewHandler.Text_Suivi_Hebdo.setText("$ " + Controller.GetAmountSpentOverTheLastWeek());
    	viewHandler.Text_Suivi_Mensuel.setText("$ " + Controller.GetAmountSpentOverTheLastMonth());
    	viewHandler.Anchor_Suivi_LineChart.getChildren().clear();
    	LineChart<Number, Number> x = SpendingController.GetChartOfSpendingsDuringPeriodOfYear(Calendar.getInstance().get(Calendar.MONTH), Calendar.MONTH, Calendar.getInstance().get(Calendar.YEAR));
    	x.setPrefWidth(viewHandler.Anchor_Resume_LineChart.getWidth());
    	x.setPrefHeight(viewHandler.Anchor_Resume_LineChart.getHeight());
    	viewHandler.Anchor_Suivi_LineChart.getChildren().add(x);
    	List<Integer> yearsFrom1970 = new ArrayList<Integer>();
    	for(int i=Calendar.getInstance().get(Calendar.YEAR); i > 1969; i--) {
    		yearsFrom1970.add(i);
    	}
    	List<IntStringPair> months = new ArrayList<IntStringPair>();
    	months.add(new IntStringPair(0, "Janvier"));
    	months.add(new IntStringPair(1, "Février"));
    	months.add(new IntStringPair(2, "Mars"));
    	months.add(new IntStringPair(3, "Avril"));
    	months.add(new IntStringPair(4, "Mai"));
    	months.add(new IntStringPair(5, "Juin"));
    	months.add(new IntStringPair(6, "Juillet"));
    	months.add(new IntStringPair(7, "Août"));
    	months.add(new IntStringPair(8, "Septembre"));
    	months.add(new IntStringPair(9, "Octobre"));
    	months.add(new IntStringPair(10, "Novembre"));
    	months.add(new IntStringPair(11, "Décembre"));
    	viewHandler.ComboBox_Suivi_Compare_Year.getItems().clear();
    	viewHandler.ComboBox_Suivi_Compare_Year.getItems().addAll(yearsFrom1970);
    	viewHandler.ComboBox_Suivi_Compare_Month.getItems().clear();
    	viewHandler.ComboBox_Suivi_Compare_Month.getItems().addAll(months);
    	int previousMonth;
    	int previousMonthsYear;
    	Calendar today = Calendar.getInstance();
    	if(today.get(Calendar.MONTH) == 0) {
    		previousMonthsYear = today.get(Calendar.YEAR) - 1;
    		previousMonth = 11;
    	}
    	else {
    		previousMonthsYear = today.get(Calendar.YEAR);
    		previousMonth = today.get(Calendar.MONTH) - 1;
    	}
    	viewHandler.ComboBox_Suivi_Compare_Year.getSelectionModel().select(today.get(Calendar.YEAR) - previousMonthsYear);
    	viewHandler.ComboBox_Suivi_Compare_Month.getSelectionModel().select(previousMonth);
    	PieChart pieChart = new PieChart();
    	HashMap<Integer, Float> spendingsByCategory = SpendingController.GetSpendingsByCategoryDuringPeriodOfYear(Calendar.getInstance().get(Calendar.MONTH), Calendar.MONTH, Calendar.getInstance().get(Calendar.YEAR));
    	ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
    	for(Map.Entry<Integer, Float> entry : spendingsByCategory.entrySet()) {
    		data.add(new PieChart.Data(entry.getKey().toString(), entry.getValue()));
    	}
    	pieChart.setData(data);
    	pieChart.setLegendSide(Side.LEFT);
    	viewHandler.Anchor_Suivi_Category_PieChart.getChildren().clear();
    	viewHandler.Anchor_Suivi_Category_PieChart.getChildren().add(pieChart);
    }

    private static void LoadHistoryTab() {
        HashMap<String, Float> values = (HashMap<String, Float>) TransactionController.Get5BiggestSpendingByCategory();
        VBox vBox = viewHandler.VBox_Top5;
        ObservableList<Node> titledPanes = vBox.getChildren();
        titledPanes.clear();
        if (values.size() <= 5) {
            for (Map.Entry<String, Float> entry :
                    values.entrySet()) {
                TitledPane titledPane = new TitledPane(entry.getKey(), new Label("$" +
                        String.format("%.2f", entry.getValue())));
                titledPanes.add(titledPane);
            }
            for (int i = 0; i < 5 - values.size(); i++)
                titledPanes.add(new TitledPane());
        }
        ListView<Text> listView = viewHandler.ListView_Transactions;
        ArrayList<Spending> spendings = (ArrayList<Spending>) TransactionController.GetPreviousSpendings();
        listView.getItems().clear();
        for (Spending spending :
                spendings) {
            Text text = new Text();
            text.setText(spending.toString());
            text.textAlignmentProperty().setValue(TextAlignment.CENTER);
            listView.getItems().add(text);
        }
    }


}

class IntStringPair {
    private final Integer key;
    private final String value;

    IntStringPair(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    int getKey() {
        return key;
    }

    @Override
    public String toString() {
        return value;
    }
}
