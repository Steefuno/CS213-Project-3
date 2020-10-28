package application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class UIController {
	static AccountDatabase db;
	
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

    /**
     * Deposit money when the Deposit button is clicked on the Funds tab
     * @param event
     */
    @FXML
    void deposit(ActionEvent event) {
    	System.out.println("Deposited");
    }

    /**
     * Withdraw money when the Withdraw button is clicked on the Funds tab
     * @param event
     */
    @FXML
    void withdraw(ActionEvent event) {
    	System.out.println("Withdrew");
    }
    
    void setup() {
    	db = new AccountDatabase();
    }
}
