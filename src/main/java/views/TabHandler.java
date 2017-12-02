package views;

import controllers.SpendingController;
import controllers.ThreshController;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.Category;
import models.Spending;
import models.Transaction;
 import controllers.SpendingController;
 import controllers.TransactionController;
import database.DatabaseManager;
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
import utils.IntStringPair;

import java.sql.SQLException;
import java.time.ZoneId;
 import java.util.*;
import java.time.ZoneId;
import java.util.*;

public class TabHandler {

    public static ViewHandler viewHandler;

    static void RefreshTab(Tab selectedTab) {
        TabHandler.ExecuteCorrespondingTabLoad(selectedTab);
    }

    public static void TabChangeHandler() {
        viewHandler.tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> TabHandler.ExecuteCorrespondingTabLoad(newValue)
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
        Alert alertDeleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertDeleteConfirmation.setTitle("Confirmation de suppression");
        alertDeleteConfirmation.setHeaderText(null);
        alertDeleteConfirmation.setContentText("Voulez-vous vraiment\nsupprimer cette transaction ?");
        ButtonType buttonYes = new ButtonType("Oui");
        ButtonType buttonNo = new ButtonType("Non");
        alertDeleteConfirmation.getButtonTypes().setAll(buttonYes, buttonNo);
        Alert alertDeleteSuccess = new Alert(Alert.AlertType.INFORMATION);
        alertDeleteSuccess.setTitle("Information");
        alertDeleteSuccess.setHeaderText(null);
        alertDeleteSuccess.setContentText("Suppression réussie");
        Alert alertDeleteFailure = new Alert(Alert.AlertType.ERROR);
        alertDeleteFailure.setTitle("Erreur");
        alertDeleteFailure.setHeaderText(null);
        alertDeleteFailure.setContentText("Une erreur est survenue lors de la suppression");
        for (Transaction transaction :
                transactions) {
            Text text = new Text(transaction.toString2());
            Button button = new Button("Supprimer");
            button.setOnAction(event -> {
                Optional<ButtonType> result = alertDeleteConfirmation.showAndWait();
                boolean res;
                if (result.isPresent() && result.get() == buttonYes) {
                    res = TransactionController.DeleteTransaction(transaction.getId());
                    RefreshTab(viewHandler.tabPane.getSelectionModel().getSelectedItem());
                    if (res)
                        alertDeleteSuccess.showAndWait();
                    else
                        alertDeleteFailure.showAndWait();
                } else if (result.isPresent() && result.get() == buttonNo)
                    RefreshTab(viewHandler.tabPane.getSelectionModel().getSelectedItem());

            });
            HBox hBox = new HBox();
            hBox.getChildren().addAll(text, button);
            listView.getItems().add(hBox);
        }
    }

    public static void Start() {
        viewHandler.updateThreshValueInMenu();
        LoadSummaryTab();
    }

    private static void SetThreshDisplay() {
        viewHandler.Text_Resume_Seuil.setText("$" + String.format("%.2f", ThreshController.getThresh()));
        Rectangle rectangle = viewHandler.Rectangle_Resume_Seuil;
        float spending = SpendingController.GetAmountSpentOverTheLastMonth();
        float thresh = ThreshController.getThresh();
        float percent = (spending / thresh) * 100f;
        if (percent >= 75 && percent < 100) {
            rectangle.fillProperty().setValue(Color.ORANGE);
        } else if (percent >= 100) {
            rectangle.fillProperty().setValue(Color.RED);
        } else {
            rectangle.fillProperty().setValue(Color.GREEN);
        }
    }

    private static void LoadSummaryTab() {
        TabHandler.SetThreshDisplay();
        viewHandler.Text_Resume_Hebdo.setText("$" + SpendingController.GetAmountSpentOverTheLastWeek());
        viewHandler.Text_Resume_Mensuel.setText("$" + SpendingController.GetAmountSpentOverTheLastMonth());
        viewHandler.Anchor_Resume_LineChart.getChildren().clear();
        LineChart<String, Number> x = SpendingController.GetChartOfSpendingsDuringPeriodOfYear(Calendar.getInstance().get(Calendar.MONTH), Calendar.MONTH, Calendar.getInstance().get(Calendar.YEAR));
        x.setPrefWidth(viewHandler.Anchor_Resume_LineChart.getWidth());
        x.setPrefHeight(viewHandler.Anchor_Resume_LineChart.getHeight());
        viewHandler.Anchor_Resume_LineChart.getChildren().add(x);
    }

    private static void LoadSuiviTab() {
        viewHandler.Button_Suivi_Week.disableProperty().setValue(false);
        viewHandler.Button_Suivi_Month.disableProperty().setValue(true);
    	viewHandler.Button_Suivi_Compare.visibleProperty().setValue(true);
    	viewHandler.ComboBox_Suivi_Compare_Month.visibleProperty().setValue(true);
    	viewHandler.ComboBox_Suivi_Compare_Year.visibleProperty().setValue(true);
    	viewHandler.Text_Suivi_Mensuel.setText("$ " + SpendingController.GetAmountSpentOverTheLastMonth());
    	viewHandler.Anchor_Suivi_LineChart.getChildren().clear();
    	LineChart<String, Number> x = SpendingController.GetChartOfSpendingsDuringPeriodOfYear(Calendar.getInstance().get(Calendar.MONTH), Calendar.MONTH, Calendar.getInstance().get(Calendar.YEAR));
    	x.setPrefWidth(viewHandler.Anchor_Suivi_LineChart.getWidth());
    	x.setPrefHeight(viewHandler.Anchor_Suivi_LineChart.getHeight());
    	viewHandler.Anchor_Suivi_LineChart.getChildren().add(x);
    	List<Integer> yearsFrom1970 = new ArrayList<Integer>();
    	for(int i=Calendar.getInstance().get(Calendar.YEAR); i > 1969; i--) {
    		yearsFrom1970.add(i);
    	}
    	viewHandler.ComboBox_Suivi_Compare_Year.getItems().clear();
    	viewHandler.ComboBox_Suivi_Compare_Year.getItems().addAll(yearsFrom1970);
    	viewHandler.ComboBox_Suivi_Compare_Month.getItems().clear();
    	viewHandler.ComboBox_Suivi_Compare_Month.getItems().addAll(SpendingController.MONTHSMAP);
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
    	Category c = null;
    	for(Map.Entry<Integer, Float> entry : spendingsByCategory.entrySet()) {
    		try {
				c = (Category) DatabaseManager.getInstance().Select(Category.class,entry.getKey());
				data.add(new PieChart.Data(c.getName(), entry.getValue()));
			} catch (SQLException e) {
	    		data.add(new PieChart.Data(Integer.toString(entry.getKey()), entry.getValue()));
				e.printStackTrace();
			}
    	}
    	pieChart.setData(data);
    	pieChart.setLegendVisible(false);
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
