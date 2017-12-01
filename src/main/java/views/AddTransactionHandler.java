package views;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddTransactionHandler {


    @FXML
    public TextField TextField_AddTransaction_Montant;

    @FXML
    public TextField TextField_AddTransaction_Repetition;

    @FXML
    public TextArea TextArea_AddTransaction_Description;

    @FXML
    public Button Button_AddTransaction_AddTransaction;

    @FXML
    public Button Button_AddTransaction_AddCategorie;

    @FXML
    public ChoiceBox<String> ChoiceBox_AddTransaction_Categorie;

    @FXML
    public ChoiceBox<String> ChoiceBox_AddTransaction_Repetition;

    @FXML
    public DatePicker DatePicker_AddTransaction_Start;

    @FXML
    public DatePicker DatePicker_AddTransaction_End;


    @FXML
    private void handleAddTransactionButtonAction() {
        // Button was clicked, do something...
        System.out.println("Button AddTransaction Clicked");
        System.out.println(TextField_AddTransaction_Montant.getText());
        System.out.println(TextField_AddTransaction_Repetition.getText());
    }

    @FXML
    private void handleAddCategoryButtonAction() {
        // Button was clicked, do something...
        System.out.println("Button AddCategory Clicked");

    }
}
