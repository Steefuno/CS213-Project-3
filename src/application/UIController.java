package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.text.DecimalFormat;

// https://docs.oracle.com/javase/8/javafx/api/toc.htm
public class UIController {
	private static AccountDatabase db;
	private static String priceFormatString = "$ #.#";
	private static DecimalFormat priceFormat;
	
    @FXML
    private TextField balance;

    @FXML
    private TextField firstNameOc;

    @FXML
    private TextField lastNameOc;

    @FXML
    private RadioButton checkingRadioOC;

    @FXML
    private RadioButton savingRadioOc;

    @FXML
    private RadioButton moneyMarketRadioOc;

    @FXML
    private CheckBox directDepositBox;

    @FXML
    private CheckBox loyalCustomerBox;

    @FXML
    private Button openAccountButton;

    @FXML
    private Button closeAcountButton;

    @FXML
    private TextField month;

    @FXML
    private TextField day;

    @FXML
    private TextField year;

    @FXML
    private TextField fname_funds;

    @FXML
    private TextField lname_funds;

    @FXML
    private TextField amount_funds;

    @FXML
    private ToggleGroup Funds_Account_Type;
    
    @FXML
    private RadioButton checking_funds;

    @FXML
    private RadioButton savings_funds;

    @FXML
    private RadioButton moneymarket_funds;
    
    @FXML
    private TextArea Output;

    @FXML
    private Button Clear_Output;

    /**
     * Outputs a string on a new line
     * @param text
     */
    void output(String text) { //TODO change system console output to ui console
    	System.out.println(text);
    }
    
    
    /**
     * Deposit money when the Deposit button is clicked on the Funds tab
     * @param event
     */
    @FXML
    void deposit(ActionEvent event) {
    	Account account = this.getAccountInFunds();
    	double amount;
    	
    	// Try to convert inputted amount to a double
    	try {
    		amount = Double.parseDouble(amount_funds.getText());
    	} catch (NumberFormatException e) {
    		this.output("Amount must be a double!");
    		return;
    	}
    	
    	// Check if an account type has been selected
    	if (account == null) {
        	this.output("An Account Type must be selected!");
    		return;
    	}
    	
    	// Try to deposit
    	boolean isDeposited = db.deposit(account, amount);
    	if (isDeposited == true) {
    		// Output if deposited
    		this.output(
    			String.format(
    				"Successfully deposited %s to %s Account, %s %s!",
    				priceFormat.format(amount),
    				account.getClass().getSimpleName(),
    				account.getProfile().getFName(),
    				account.getProfile().getLName()
    			)
    		);
    		return;
    	} else {
    		// Output if failed to deposit from invalid account
    		this.output(
    			String.format(
    				"%s Account not found: %s %s.",
    				account.getClass().getSimpleName(),
    				account.getProfile().getFName(),
    				account.getProfile().getLName()
    			)
    		);
    		return;
    	}
    }
    
    @FXML
    void ExportDataBase(ActionEvent event) {
    	
    }

    @FXML
    void withdraw(ActionEvent event) {
    	Account account = this.getAccountInFunds();
    	double amount;
    	
    	// Try to convert inputted amount to a double
    	try {
    		amount = Double.parseDouble(amount_funds.getText());
    	} catch (NumberFormatException e) {
    		this.output("Amount must be a double!");
    		return;
    	}
    	
    	// Check if an account type has been selected
    	if (account == null) {
        	this.output("An Account Type must be selected!");
    		return;
    	}
    	
    	// Try to withdraw
    	int isWithdrawn = db.withdrawal(account, amount);
    	if (isWithdrawn == 0) {
    		// Output if withdrawn
    		this.output(
    			String.format(
    				"Successfully withdrew %s to %s Account, %s %s!",
    				priceFormat.format(amount),
    				account.getClass().getSimpleName(),
    				account.getProfile().getFName(),
    				account.getProfile().getLName()
    			)
    		);
    		return;
    	} else if (isWithdrawn == 1) {
    		// Output if failed to withdraw from not enough balance
    		this.output(
    			String.format(
    				"%s Account, %s %s, does not have enough balance.",
    				account.getClass().getSimpleName(),
    				account.getProfile().getFName(),
    				account.getProfile().getLName()
    			)
    		);
    		return;
    	} else if (isWithdrawn == -1) {
    		// Output if failed to withdraw from invalid account
    		this.output(
        			String.format(
        				"%s Account not found: %s %s.",
        				account.getClass().getSimpleName(),
        				account.getProfile().getFName(),
        				account.getProfile().getLName()
        			)
        		);
        	return;
    	}
    }
    
    void setup() {
    	db = new AccountDatabase();
    	
    	priceFormat = new DecimalFormat(priceFormatString);
    	priceFormat.setMinimumFractionDigits(2);
    }
    
    private Account getAccountInFunds() {
    	String fname = fname_funds.getText();
    	String lname = lname_funds.getText();
    	
    	//TODO use ToggleGroup instead of spamming isSelected()
    	
    	if (checking_funds.isSelected()) {
    		return (Account)new Checking(fname, lname);
    	} else if (savings_funds.isSelected()) {
    		return (Account)new Savings(fname, lname);
    	} else if (moneymarket_funds.isSelected()) {
    		return (Account)new MoneyMarket(fname, lname);
    	}
    	
    	return null;
    }
    
    @FXML
    void clear(ActionEvent event) { //TODO
    	System.out.println("Clearing output");
    }

    @FXML
    void closeAccount(ActionEvent event) { //TODO

    }

    @FXML
    void getBalance(ActionEvent event) { //TODO
    	
    }

    @FXML
    void importDataBase(ActionEvent event) { //TODO

    }

    @FXML
    void openAccount(ActionEvent event) { //TODO

    }

}
