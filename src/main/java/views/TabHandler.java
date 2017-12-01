package views;

import controllers.Controller;
import controllers.TransactionController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import models.Category;

import java.time.ZoneId;
import java.util.ArrayList;
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
    
    private static void LoadSummaryTab() {
    	viewHandler.Text_Resume_Hebdo.setText("" + Controller.GetAmountSpentOverTheLastWeek() + " $");
    	viewHandler.Text_Resume_Mensuel.setText("" + Controller.GetAmountSpentOverTheLastMonth() + " $");
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
