package views;

import controllers.Controller;
import controllers.SpendingController;
import controllers.TransactionController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import models.Category;
import javafx.scene.chart.LineChart;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TabHandler {

    public static ViewHandler viewHandler;

    public static void TabChangeHandler() {
        viewHandler.tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        switch (newValue.getId()) {
                            case "Tab_Resume":
                                System.out.println("Resume");
                                TabHandler.LoadSummaryTab();
                                break;
                            case "Tab_AddTransaction":
                                System.out.println("Transaction");
                                TabHandler.LoadAddTransactionTab();
                                break;
                            case "Tab_Hebdo":
                                System.out.println("Hebo");

                                break;
                            case "Tab_Mensuel":
                                System.out.println("Mensuel");

                                break;
                            case "Tab_Historique":
                                System.out.println("Historique");

                                break;
                        }
                    }
                }
        );
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
        viewHandler.ChoiceBox_AddTransaction_Repetition.getItems().addAll("Jours", "Semaines");
        viewHandler.ChoiceBox_AddTransaction_Repetition.getSelectionModel().selectFirst();
    }
    
    public static void Start() {
    	LoadSummaryTab();
    }
    
    private static void LoadSummaryTab() {
    	viewHandler.Text_Resume_Hebdo.setText("$ " + Controller.GetAmountSpentOverTheLastWeek());
    	viewHandler.Text_Resume_Mensuel.setText("$ " + Controller.GetAmountSpentOverTheLastMonth());
    	viewHandler.Anchor_Resume_LineChart.getChildren().clear();
    	LineChart<Number, Number> x = SpendingController.GetChartOfSpendingsDuringPeriodOfYear(Calendar.getInstance().get(Calendar.MONTH), Calendar.MONTH, Calendar.getInstance().get(Calendar.YEAR));
//    	LineChart<Number, Number> x = SpendingController.GetChartOfSpendingsDuringPeriodOfYear(10, Calendar.MONTH, Calendar.getInstance().get(Calendar.YEAR));
    	x.setPrefWidth(viewHandler.Anchor_Resume_LineChart.getWidth());
    	x.setPrefHeight(viewHandler.Anchor_Resume_LineChart.getHeight());
    	viewHandler.Anchor_Resume_LineChart.getChildren().add(x);
    }

}

class IntStringPair {
    private final Integer key;
    private final String value;

    public IntStringPair(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    @Override
    public String toString() {
        return value;
    }
}
