package views;

import controllers.SpendingController;
import controllers.ThreshController;
import controllers.TransactionController;
import database.DatabaseManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Category;
import models.User;

import java.sql.SQLException;
import java.time.*;
import java.util.*;

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
    public ComboBox<IntStringPair> ChoiceBox_AddTransaction_Categorie;

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
    protected ListView<Text> ListView_Transactions;

    @FXML
    protected VBox VBox_Top5;


    @FXML
    protected ChoiceBox<String> ChoiceBox_AddTransaction_Repetition;

    @FXML
    protected TextField TextField_AddTransaction_Repetition;

    @FXML
    protected Button AddCategoryValidateButton;

    @FXML
    protected TextField AddTransactionFieldName;

    @FXML
    protected AnchorPane Anchor_Resume_LineChart;

    @FXML
    protected Label Label_Repetition;

    @FXML
    protected ListView<HBox> TransactionList;

    @FXML
    protected Text Text_Suivi_Mensuel;

    @FXML
    protected AnchorPane Anchor_Suivi_LineChart;

    @FXML
    protected ComboBox<IntStringPair> ComboBox_Suivi_Compare_Month;

    @FXML
    protected ComboBox<Integer> ComboBox_Suivi_Compare_Year;

    @FXML
    protected Button Button_Suivi_Compare;

    @FXML
    protected AnchorPane Anchor_Suivi_Category_PieChart;

    @FXML
    protected Text threshValueDisplay;

    @FXML
    protected CustomMenuItem threshMenuItem;

    @FXML
    protected Rectangle Rectangle_Resume_Seuil;

    void updateThreshValueInMenu() {
        threshMenuItem.setHideOnClick(false);
        this.threshValueDisplay.setText(String.format("$%.2f", ThreshController.getThresh()));
        this.handleOnMenuRefresh();
    }


    @FXML
    private void handleAddModifyDateToCompare() {
        try {
            int year = ComboBox_Suivi_Compare_Year.getValue();
            int month = ComboBox_Suivi_Compare_Month.getSelectionModel().getSelectedItem().getKey();
            if (year > Calendar.getInstance().get(Calendar.YEAR) || (year == Calendar.getInstance().get(Calendar.YEAR) && month >= Calendar.getInstance().get(Calendar.MONTH))) {
                Button_Suivi_Compare.setDisable(true);
            } else {
                Button_Suivi_Compare.setDisable(false);
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            ;
        }
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void handleAddPeriodToCompareToChartSuivi() {
        int year = ComboBox_Suivi_Compare_Year.getValue();
        int month = ComboBox_Suivi_Compare_Month.getSelectionModel().getSelectedItem().getKey();
        XYChart.Series<Number, Number> serieToAdd = SpendingController.GetSeriesOfSpendingsDuringPeriodOfYear(month, Calendar.MONTH, year);
        LineChart<Number, Number> lineChart = (LineChart<Number, Number>) Anchor_Suivi_LineChart.getChildren().get(0);
        if (lineChart.getData().size() == 2) {
            lineChart.getData().remove(1);
        }
        lineChart.getData().add(serieToAdd);
    }

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
            LocalDate today = LocalDate.now(ZoneId.systemDefault());
            boolean result;
            if (stardDate.isEqual(today)) {
                LocalDateTime now = stardDate.atTime(LocalTime.now());
                result = TransactionController.CreateNewTransaction(amount, category, frequency, description,
                        new User(1, "test_user", "pwd"), Date.from(
                                now.atZone(ZoneId.systemDefault()).toInstant()),
                        Date.from(Instant.from(
                                endDate.atStartOfDay(ZoneId.systemDefault())
                        )));
            } else {
                result = TransactionController.CreateNewTransaction(amount, category, frequency, description,
                        new User(1, "test_user", "pwd"), Date.from(
                                Instant.from(
                                        stardDate.atStartOfDay(ZoneId.systemDefault())
                                ))
                        , Date.from(Instant.from(
                                endDate.atStartOfDay(ZoneId.systemDefault())
                        )));
            }
            handleOnMenuRefresh();
            if (result) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Création réussie");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur est servenue lors de la création");
                alert.showAndWait();
            }
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
            this.Label_Repetition.disableProperty().setValue(false);
        } else {
            this.TextField_AddTransaction_Repetition.disableProperty().setValue(true);
            this.ChoiceBox_AddTransaction_Repetition.disableProperty().setValue(true);
            this.Label_Repetition.disableProperty().setValue(true);

        }
    }

    @FXML
    private void handleOnMenuExit() {
        Platform.exit();
    }

    @FXML
    private void handleOnMenuRefresh() {
        Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null)
            TabHandler.RefreshTab(selectedTab);
    }

    @FXML
    private void handleOnThreshSet() {
        TextInputDialog textInputDialog = new TextInputDialog(String.format("$%.2f", 0f));
        textInputDialog.setTitle("Définir seuil");
        textInputDialog.setHeaderText(null);
        textInputDialog.setContentText("Entrez votre seuil: ");
        Optional<String> result = textInputDialog.showAndWait();
        final String[] resA = {""};
        String res;
        result.ifPresent(value -> resA[0] = value);
        float threshValue = -1;
        if (!Objects.equals(resA[0], "")) {
            res = resA[0].replace(',', '.');
            int k = 0;
            boolean condition = Character.isLetter(res.charAt(k)) || Character.isWhitespace(res.charAt(k));
            while (condition) {
                k++;
                condition = Character.isLetter(res.charAt(k)) || Character.isWhitespace(res.charAt(k));
            }
            if (res.charAt(k) == '$')
                k++;
            threshValue = Float.parseFloat(res.substring(k));
        }
        if (threshValue != -1) {
            ThreshController.setThresh(threshValue);
            this.updateThreshValueInMenu();
        }
    }

}
