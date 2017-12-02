package views;

import controllers.Controller;
import controllers.SpendingController;
import controllers.TransactionController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.Category;
import models.Spending;

import java.time.ZoneId;
import java.util.*;

public class TabHandler {

    public static ViewHandler viewHandler;

    public static void TabChangeHandler() {
        viewHandler.tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
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
                            TabHandler.LoadHistoryTab();
                            break;
                    }
                }
        );
    }

    //TODO: Arranger le tab du formulaire
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
