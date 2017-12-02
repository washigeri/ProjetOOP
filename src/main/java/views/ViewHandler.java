package views;

import controllers.TransactionController;
import database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Category;
import models.User;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

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
    public ChoiceBox<IntStringPair> ChoiceBox_AddTransaction_Categorie;

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
    protected Text Text_Hebdo_Current;

    @FXML
    protected Text Text_Hebdo_Past;


    @FXML
    protected Text Text_Monthly_Current;

    @FXML
    protected Text Text_Monthly_Past;


    @FXML
    protected ListView<Text> ListView_Transactions;

    @FXML
    protected VBox VBox_Top5;

    @FXML
    protected ScrollPane ScrollPane_Mensuel;

    @FXML
    protected ChoiceBox<String> ChoiceBox_AddTransaction_Repetition;

    @FXML
    protected TextField TextField_AddTransaction_Repetition;

    @FXML
    protected Button AddCategoryValidateButton;

    @FXML
    protected TextField AddTransactionFieldName;

    @FXML
    private void handleAddTransactionButtonAction() {

        try {
            Category category = (Category) DatabaseManager.getInstance().Select(Category.class
                    , this.ChoiceBox_AddTransaction_Categorie.getSelectionModel().getSelectedItem().getKey());
            String description = this.TextArea_AddTransaction_Description.getText();
            LocalDate stardDate = this.DatePicker_AddTransaction_Start.getValue();
            LocalDate endDate = this.DatePicker_AddTransaction_End.getValue();
            float amount = Float.parseFloat(this.TextField_AddTransaction_Montant.getText());
            int frequency;
            if (endDate == null) {
                frequency = 0;
                endDate = stardDate;
            } else {
                frequency = Integer.parseInt(this.TextField_AddTransaction_Repetition.getText());
                String daysOrWeeks = this.ChoiceBox_AddTransaction_Repetition.getSelectionModel().getSelectedItem();
                int multiplier = (daysOrWeeks.equals("Jours")) ? 1 : 7;
                frequency *= multiplier;
            }
            TransactionController.CreateNewTransaction(amount, category, frequency, description,
                    new User(1, "test_user", "pwd"), Date.from(
                            Instant.from(stardDate.atStartOfDay(ZoneId.systemDefault()))
                    ), Date.from(Instant.from(
                            endDate.atStartOfDay(ZoneId.systemDefault())
                    )));
            System.out.println("");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void handleAddCategoryButtonAction() {
        this.AddCategoryValidateButton.disableProperty().setValue(false);
        this.AddTransactionFieldName.disableProperty().setValue(false);

    }

    @FXML
    private void handleValidateCategoryButton() {
        String name = this.AddTransactionFieldName.getText();
        if (!Objects.equals(name, "")) {
            TransactionController.CreateNewCategory(name);
            this.AddCategoryValidateButton.disableProperty().setValue(true);
            this.AddTransactionFieldName.disableProperty().setValue(true);
            ArrayList<Category> categories = (ArrayList<Category>) TransactionController.GetAllCategories();
            this.ChoiceBox_AddTransaction_Categorie.getItems().clear();
            for (Category category :
                    categories) {
                this.ChoiceBox_AddTransaction_Categorie.getItems().add(
                        new IntStringPair(category.getId(), category.getName()));
            }
            this.ChoiceBox_AddTransaction_Categorie.getSelectionModel().selectLast();
        }
    }

    @FXML
    private void handleEndDateSelection() {
        LocalDate date = this.DatePicker_AddTransaction_End.getValue();
        if (date.isAfter(this.DatePicker_AddTransaction_Start.getValue())) {
            this.TextField_AddTransaction_Repetition.disableProperty().setValue(false);
            this.ChoiceBox_AddTransaction_Repetition.disableProperty().setValue(false);
        } else {
            this.TextField_AddTransaction_Repetition.disableProperty().setValue(true);
            this.ChoiceBox_AddTransaction_Repetition.disableProperty().setValue(true);

        }
    }
}
