package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddTransactionController {
		
    @FXML
	private TextField TextField_AddTransaction_Montant; 
    
    @FXML
    private TextField TextField_AddTransaction_Repetition;
    
    @FXML
    private TextArea TextArea_AddTransaction_Description;
    
    @FXML
    private Button Button_AddTransaction_AddTransaction;
    
    @FXML
    private Button Button_AddTransaction_AddCategorie;
    
    @FXML
    private ChoiceBox<String> ChoiceBox_AddTransaction_Categorie;
    
    
	public AddTransactionController() {
		
	}
}

