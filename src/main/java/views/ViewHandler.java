package views;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.Map;

public class ViewHandler {


    @FXML
    public TextField TextField_AddTransaction_Montant;


    @FXML
    public TextArea TextArea_AddTransaction_Description;

    @FXML
    public Button Button_AddTransaction_AddTransaction;

    @FXML
    public Button Button_AddTransaction_AddCategorie;

    @FXML
    public ChoiceBox<Map.Entry<Integer, String>> ChoiceBox_AddTransaction_Categorie;

    @FXML
    public DatePicker DatePicker_AddTransaction_Start;

    @FXML
    public DatePicker DatePicker_AddTransaction_End;

    @FXML
    protected TabPane tabPane;

    @FXML
    protected Text Text_Resume_Seuil;

    @FXML
    protected Text Text_Resume_Hebdo;

    @FXML
    protected Text Text_Resume_Mensuel;

    @FXML
    protected LineChart<Number, Number> LineChart_Resume;

    @FXML
    protected Text Text_Hebdo_Current;

    @FXML
    protected Text Text_Hebdo_Past;

    @FXML
    protected LineChart<Number, Number> LineChart_Hebdo;

    @FXML
    protected Text Text_Monthly_Current;

    @FXML
    protected Text Text_Monthly_Past;

    @FXML
    protected LineChart<Number, Number> LineChart_Mensuel;

    @FXML
    protected ListView ListView_Transactions;

    @FXML
    protected Accordion Accordion_Top5;

    @FXML
    protected ScrollPane ScrollPane_Mensuel;

    @FXML
    protected ChoiceBox<String> ChoiceBox_AddTransaction_Repetition;

    @FXML
    protected TextField TextField_AddTransaction_Repetition;

    @FXML
    protected AnchorPane AnchorPane_Tab_Resume_Main;
    
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

    @FXML
    private void handleStartDateSelection() {

    }

    @FXML
    private void handleEndDateSelection() {

    }

}
