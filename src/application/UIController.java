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
	private AccountDatabase db;
	private String priceFormatString = "$ #.#";
	private DecimalFormat priceFormat;

	/**
	 * Method called by Main.java to initialize variables
	 */
    void setup() {
    	db = new AccountDatabase();
    	
    	priceFormat = new DecimalFormat(priceFormatString);
    	priceFormat.setMinimumFractionDigits(2);
    	
    	//TODO remove temp account for testing
    	db.add(new Checking("Steve", "N", 100, 1, 1, 2000, true));
    	db.add(new Checking("Michael", "Q", 100, 1, 1, 2000, true));
    }
	
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
     * Deposit money when the Deposit button is clicked on the Funds tab
     * @param event
     */
    @FXML
    void deposit(ActionEvent event) {
    	Object getAccountResult = this.getAccountInFunds();
    	double amount;
    	
    	// Check if an account type has been selected
    	if (getAccountResult instanceof String) {
        	this.output((String)getAccountResult);
    		return;
    	}
    	Account account = (Account)getAccountResult;
    	
    	// Try to convert inputted amount to a double
    	try {
    		amount = Double.parseDouble(amount_funds.getText());
    	} catch (NumberFormatException e) {
    		this.output("Amount must be a double!\n");
    		return;
    	}
    	
    	// Try to deposit
    	boolean isDeposited = db.deposit(account, amount);
    	if (isDeposited == true) {
    		// Output if deposited
    		this.output(
    			String.format(
    				"Successfully deposited %s to %s Account, %s %s!\n",
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
    				"%s Account not found: %s %s.\n",
    				account.getClass().getSimpleName(),
    				account.getProfile().getFName(),
    				account.getProfile().getLName()
    			)
    		);
    		return;
    	}
    }

    @FXML
    void withdraw(ActionEvent event) {
    	Object getAccountResult = this.getAccountInFunds();
    	double amount;
    	
    	// Check if an account type has been selected
    	if (getAccountResult instanceof String) {
        	this.output((String)getAccountResult);
    		return;
    	}
    	Account account = (Account)getAccountResult;
    	
    	// Try to convert inputted amount to a double
    	try {
    		amount = Double.parseDouble(amount_funds.getText());
    	} catch (NumberFormatException e) {
    		this.output("Amount must be a double!\n");
    		return;
    	}
    	
    	// Try to withdraw
    	int isWithdrawn = db.withdrawal(account, amount);
    	if (isWithdrawn == 0) {
    		// Output if withdrawn
    		this.output(
    			String.format(
    				"Successfully withdrew %s to %s Account, %s %s!\n",
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
    				"%s Account, %s %s, does not have enough balance.\n",
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
        				"%s Account not found: %s %s.\n",
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
    
    /**
     * Gets an account on the Funds tab given the inputs
     * @return	either a template account or a string with the error
     */
    private Object getAccountInFunds() {
    	String fname = fname_funds.getText();
    	String lname = lname_funds.getText();
    	RadioButton selected = (RadioButton)Funds_Account_Type.getSelectedToggle();
    	
    	if (fname.length() == 0 || lname.length() == 0) {
    		return "Must input a first and last name!\n";
    	}
    	
    	if (selected == checking_funds) {
    		return (Account)new Checking(fname, lname);
    	} else if (selected == savings_funds) {
    		return (Account)new Savings(fname, lname);
    	} else if (selected == moneymarket_funds) {
    		return (Account)new MoneyMarket(fname, lname);
    	}
    	
    	return "Must select an account type!\n";
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

    @FXML
    void PrintAccountByName(ActionEvent event) {
    	output(db.printByLastName());
    }

    @FXML
    void PrintAccounts(ActionEvent event) {
    	output(db.printAccounts());
    }

    @FXML
    void PrintAccountsByDate(ActionEvent event) {
    	output(db.printByDateOpen());
    }

    /**
     * Outputs a string on a new line
     * @param text
     */
    void output(String text) {
    	Output.appendText(text);
    }
    
    /**
     * Clears the UI Output
     * @param event
     */
    @FXML
    void clear(ActionEvent event) {
    	Output.setText("");
    }
}
