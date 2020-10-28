package application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class UIController {

    @FXML
    private TextField fname_funds;

    @FXML
    private TextField lname_funds;

    @FXML
    private TextField amount_funds;

    @FXML
    private RadioButton checking_funds;

    @FXML
    private ToggleGroup Funds_Account_Type;

    @FXML
    private RadioButton savings_funds;

    @FXML
    private RadioButton moneymarket_funds;

    @FXML
    private Button withdraw;

    @FXML
    private Button deposit_funds;

    @FXML
    private TextArea Output;

    @FXML
    private Button Clear_Button;

    @FXML
    void deposit(ActionEvent event) {
    	
    }

    @FXML
    void withdraw(ActionEvent event) {
    	
    }

}
