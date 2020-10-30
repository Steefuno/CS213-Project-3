package application;

import java.io.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private boolean checkNameInOC(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(name);
        if(matcher.matches()){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isValidDate(String date) {
        boolean isValid = false;
        int leapYearDay = 29;
        int leapYearDivisor = 4;

        //special case months
        int september = 9;
        int november = 11;
        int february = 2;
        int april = 4;
        int june = 6;

        //special case days
        int thirtyFirst = 31;
        int thirty = 30;
        int twentyNine = 29;

        //parse date for special cases
        String[] numbers = date.split("/", 3);
        int month = Integer.parseInt(numbers[0]);
        int day = Integer.parseInt(numbers[1]);
        int year = Integer.parseInt(numbers[2]);


        //handling leap years
        if (year % leapYearDivisor == 0 && month == february && day == leapYearDay) {
            return true;
        }
        //handling months with 31 days
        else if ((month == november || month == september || month == june || month == april) && day == thirtyFirst) {
            return false;
        }
        //handling February
        else if (month == february && (day == twentyNine || day == thirty || day == thirtyFirst)) {
            return false;
        }
        //other date inputs that are not special cases
        else {
            Pattern pattern = Pattern.compile("^(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])/[12]\\d{3}$");
            Matcher matcher = pattern.matcher(date);
            isValid = matcher.matches();

            if (isValid) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isValidInteger(String date) {
        try {
            Integer.parseInt(date);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

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
        FileChooser fileChooser = new FileChooser();
        Stage primaryStage = new Stage();

        fileChooser.setTitle("Save File");
        //adds .txt extension to all text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File database = fileChooser.showSaveDialog(primaryStage);
        saveTextToFile(db.printAccounts(),database);
    }
    private void saveTextToFile(String content, File file){
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            this.output("Export successful");
            writer.close();
        } catch (IOException ex) {
            this.output("Cannot output file");
        }
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

        String fName = firstNameOc.getText();
        String lName = lastNameOc.getText();
        //checks if names are valid input
        if(!checkNameInOC(fName)){
            this.output("Name must be entered and cannot have numbers or symbols!\n");
            return;
        }

        if(!checkNameInOC(lName)){
            this.output("Name must be entered and cannot have numbers or symbols!\n");
            return;
        }

        if(checkingRadioOC.isSelected()){
                Checking checkingAccount = new Checking(fName, lName);
                boolean isPresent = db.remove(checkingAccount);
                if(isPresent){
                    this.output(
                            String.format(
                                    "%s Account, %s %s, has been closed.\n",
                                    checkingAccount.getClass().getSimpleName(),
                                    checkingAccount.getProfile().getFName(),
                                    checkingAccount.getProfile().getLName()
                            )
                    );
                    return;
                }
                else {
                    // Output if failed to deposit from invalid account
                    this.output(
                            String.format(
                                    "%s Account has not been closed: %s %s.\n",
                                    checkingAccount.getClass().getSimpleName(),
                                    checkingAccount.getProfile().getFName(),
                                    checkingAccount.getProfile().getLName()
                            )
                    );
                    return;

                }

        }
        else if(savingRadioOc.isSelected()){
                Savings savingsAccount = new Savings(fName, lName);
                boolean isPresent = db.remove(savingsAccount);

                if (isPresent){
                    this.output(
                            String.format(
                                    "%s Account, %s %s, has been closed.\n",
                                    savingsAccount.getClass().getSimpleName(),
                                    savingsAccount.getProfile().getFName(),
                                    savingsAccount.getProfile().getLName()
                            )
                    );
                    return;

                }
                else{
                    // Output if failed to open from invalid account
                    this.output(
                            String.format(
                                    "%s Account has not been closed: %s %s.\n",
                                    savingsAccount.getClass().getSimpleName(),
                                    savingsAccount.getProfile().getFName(),
                                    savingsAccount.getProfile().getLName()
                            )
                    );
                    return;
                }
        }
        else if(moneyMarketRadioOc.isSelected()){
            MoneyMarket moneyMarket = new MoneyMarket(fName, lName);
            boolean isPresent = db.remove(moneyMarket);
            if(isPresent){
                this.output(
                        String.format(
                                "%s Account, %s %s, has been closed.\n",
                                moneyMarket.getClass().getSimpleName(),
                                moneyMarket.getProfile().getFName(),
                                moneyMarket.getProfile().getLName()
                        )
                );
                return;

            }
            else{
                // Output if failed to open from invalid account
                this.output(
                        String.format(
                                "%s Account has not been closed: %s %s.\n",
                                moneyMarket.getClass().getSimpleName(),
                                moneyMarket.getProfile().getFName(),
                                moneyMarket.getProfile().getLName()
                        )
                );
                return;
            }
        }
        else{
            this.output("An account type must be selected!\n");
        }
    }


    @FXML
    void importDataBase(ActionEvent event) { //TODO
        FileChooser fileChooser = new FileChooser();
        Stage primaryStage = new Stage();

        fileChooser.setTitle("Open File");
        File database = fileChooser.showOpenDialog(primaryStage);

        try {
            File myObj = new File(database.getPath());
            Scanner sc = new Scanner(myObj);

            sc.useDelimiter(",");
            while(sc.hasNext()){
                String accountType = sc.next();
                System.out.println("type: " + accountType);
                if(accountType.contains("S")){
                    String firstName = sc.next();
                    System.out.println(firstName);
                    String lastName = sc.next();
                    System.out.println(lastName);
                    Double balance = Double.parseDouble(sc.next());
                    System.out.println(balance);
                    String[] date = sc.next().split("/", -2);
                    System.out.println(date);
                    boolean loyalCustomer = Boolean.parseBoolean(sc.next());
                    System.out.println(loyalCustomer);


                    Savings savingsAccount = new Savings(firstName, lastName, balance, Integer.parseInt(date[0]),
                            Integer.parseInt(date[1]), Integer.parseInt(date[2]), loyalCustomer);
                    db.add(savingsAccount);
                }
                else if(accountType.contains("C")){
                    String firstName = sc.next();
                    String lastName = sc.next();
                    Double balance = Double.parseDouble(sc.next());
                    String[] date = sc.next().split("/", -2);
                    boolean directDeposit = Boolean.parseBoolean(sc.next());

                    Checking checkingAccount = new Checking(firstName, lastName, balance, Integer.parseInt(date[0]),
                            Integer.parseInt(date[1]), Integer.parseInt(date[2]), directDeposit);
                    db.add(checkingAccount);
                }
                else{
                    String firstName = sc.next();
                    String lastName = sc.next();
                    Double balance = Double.parseDouble(sc.next());
                    String[] date = sc.next().split("/", -2);

                    MoneyMarket moneyMarket = new MoneyMarket(firstName, lastName, balance, Integer.parseInt(date[0]),
                            Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    db.add(moneyMarket);
                }
                sc.nextLine();
            }
            this.output("Import completed\n");
        } catch (FileNotFoundException e) {
            this.output("File not found\n");
            e.printStackTrace();
        }
    }

    @FXML
    void openAccount(ActionEvent event) { //TODO

        String fName = firstNameOc.getText();
        String lName = lastNameOc.getText();
        //checks if names are valid input
        if(!checkNameInOC(fName)){
            this.output("Name must be entered and cannot have numbers or symbols!\n");
            return;
        }

        if(!checkNameInOC(lName)){
            this.output("Name must be entered and cannot have numbers or symbols!\n");
            return;
        }

        if(!isValidInteger(month.getText()) || !isValidInteger(day.getText()) || !isValidInteger(year.getText())){
            this.output("Date must be entered and cannot have letters or symbols!\n");
            return;
        }

        String date = month.getText() + "/" + day.getText()+ "/" + year.getText();

        int monthDate;
        int dayDate;
        int yearDate;

        if(isValidDate(date)){
            monthDate = Integer.parseInt(date.substring(0,2));
            dayDate = Integer.parseInt(date.substring(3,5));
            yearDate = Integer.parseInt(date.substring(6,10));
        }
        else{
            this.output("Not a valid Date!\n");
            return;
        }


        //checks if balance is valid input
        double amount;
        try {
            amount = Double.parseDouble(balance.getText());
        } catch (NumberFormatException e) {
            this.output("Amount must be entered and be a double!\n");
            return;
        }
        //gui button for checking
        if(checkingRadioOC.isSelected()){
            if(directDepositBox.isSelected()){
                Checking checkingAccount = new Checking(fName, lName, amount, monthDate, dayDate, yearDate, true);
                boolean isPresent = db.add(checkingAccount);
                if(isPresent){
                    this.output(
                            String.format(
                                    "%s Account, %s %s, has been added.\n",
                                    checkingAccount.getClass().getSimpleName(),
                                    checkingAccount.getProfile().getFName(),
                                    checkingAccount.getProfile().getLName()
                            )
                    );
                    return;
                }
                else {
                    // Output if failed to deposit from invalid account
                    this.output(
                            String.format(
                                    "%s Account has not been added: %s %s.\n",
                                    checkingAccount.getClass().getSimpleName(),
                                    checkingAccount.getProfile().getFName(),
                                    checkingAccount.getProfile().getLName()
                            )
                    );
                    return;

                }
            }
            else{
                Checking checkingAccount = new Checking(fName, lName, amount, monthDate, dayDate, yearDate, false);
                boolean isPresent = db.add(checkingAccount);
                if (isPresent){
                    this.output(
                            String.format(
                                    "%s Account, %s %s, has been added.\n",
                                    checkingAccount.getClass().getSimpleName(),
                                    checkingAccount.getProfile().getFName(),
                                    checkingAccount.getProfile().getLName()
                            )
                    );
                    return;

                }
                else{
                    // Output if failed to open from invalid account
                    this.output(
                            String.format(
                                    "%s Account has not been added: %s %s.\n",
                                    checkingAccount.getClass().getSimpleName(),
                                    checkingAccount.getProfile().getFName(),
                                    checkingAccount.getProfile().getLName()
                            )
                    );
                    return;
                }

            }
        }
        else if(savingRadioOc.isSelected()){
            if(loyalCustomerBox.isSelected()){
                Savings savingsAccount = new Savings(fName, lName, amount, monthDate, dayDate, yearDate, true);
                boolean isPresent = db.add(savingsAccount);

                if (isPresent){
                    this.output(
                            String.format(
                                    "%s Account, %s %s, has been added.\n",
                                    savingsAccount.getClass().getSimpleName(),
                                    savingsAccount.getProfile().getFName(),
                                    savingsAccount.getProfile().getLName()
                            )
                    );
                    return;

                }
                else{
                    // Output if failed to open from invalid account
                    this.output(
                            String.format(
                                    "%s Account has not been added: %s %s.\n",
                                    savingsAccount.getClass().getSimpleName(),
                                    savingsAccount.getProfile().getFName(),
                                    savingsAccount.getProfile().getLName()
                            )
                    );
                    return;

                }


            }
            else{
                Savings savingsAccount = new Savings(fName, lName, amount, monthDate, dayDate, yearDate, true);
                boolean isPresent = db.add(savingsAccount);
                if (isPresent){
                    this.output(
                            String.format(
                                    "%s Account, %s %s, has been added.\n",
                                    savingsAccount.getClass().getSimpleName(),
                                    savingsAccount.getProfile().getFName(),
                                    savingsAccount.getProfile().getLName()
                            )
                    );
                    return;

                }
                else{
                    // Output if failed to open from invalid account
                    this.output(
                            String.format(
                                    "%s Account has not been added: %s %s.\n",
                                    savingsAccount.getClass().getSimpleName(),
                                    savingsAccount.getProfile().getFName(),
                                    savingsAccount.getProfile().getLName()
                            )
                    );
                    return;

                }
            }
        }
        else if(moneyMarketRadioOc.isSelected()){
            MoneyMarket moneyMarket = new MoneyMarket(fName, lName, amount, monthDate, dayDate, yearDate);
            boolean isPresent = db.add(moneyMarket);
            if(isPresent){
                this.output(
                        String.format(
                                "%s Account, %s %s, has been added.\n",
                                moneyMarket.getClass().getSimpleName(),
                                moneyMarket.getProfile().getFName(),
                                moneyMarket.getProfile().getLName()
                        )
                );
                return;

            }
            else{
                // Output if failed to open from invalid account
                this.output(
                        String.format(
                                "%s Account has not been added: %s %s.\n",
                                moneyMarket.getClass().getSimpleName(),
                                moneyMarket.getProfile().getFName(),
                                moneyMarket.getProfile().getLName()
                        )
                );
                return;

            }
        }
        else{
            this.output("An account type must be selected!\n");
        }


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
