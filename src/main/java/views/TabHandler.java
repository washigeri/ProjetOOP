package views;

import controllers.Controller;
import controllers.TransactionController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import models.Category;

import java.util.ArrayList;

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
                                LoadSummaryTab();
                                break;
                            case "Tab_AddTransaction":
                                System.out.println("Transaction");
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
        ArrayList<Category> categories = (ArrayList<Category>) TransactionController.GetAllCategories();
    }
    
    private static void LoadSummaryTab() {
    	viewHandler.Text_Resume_Hebdo.setText("" + Controller.GetAmountSpentOverTheLastWeek() + " $");
    	viewHandler.Text_Resume_Mensuel.setText("" + Controller.GetAmountSpentOverTheLastMonth() + " $");
    	viewHandler.AnchorPane_Tab_Resume_Main.getChildren().add(Controller.ShowGraphOfSpendingsOverTheLastMonths(0));
    }
}
