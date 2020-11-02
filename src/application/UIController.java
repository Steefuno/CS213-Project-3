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

    /**
     * disables loyalCustomerBox when checking button is clicked
     * @param event
     */
    @FXML
    void checkingOcSelected(ActionEvent event) {
        loyalCustomerBox.setDisable(true);
        directDepositBox.setDisable(false);
    }

    /**
     * disables directDepositBox when saving button is clicked
     * @param event
     */
    @FXML
    void savingsOcSelected(ActionEvent event) {
        directDepositBox.setDisable(true);
        loyalCustomerBox.setDisable(false);
    }
    /**
     * disables loyalCustomerBox and directDepositBox when moneyMarket button is clicked
     * @param event
     */
    @FXML
    void moneyMarkerOcSelected(ActionEvent event) {
        directDepositBox.setDisable(true);
        loyalCustomerBox.setDisable(true);
    }

    /**
     * Checks input of checking names for invalid numbers and symbols
     * @param name
     * @return boolean true if name is a valid integer for date, otherwise false
     */
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

    /**
     * Checks input of date for invalid dates
     * @param date
     * @return boolean true if input is a valid date and format, otherwise false
     */
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
            Pattern pattern = Pattern.compile("^((((0[13578])|([13578])|(1[02]))[\\/](([1-9])|([0-2][0-9])|(3[01])))" +
                    "|(((0[469])|([469])|(11))[\\/](([1-9])|([0-2][0-9])|(30)))|((2|02)[\\/](([1-9])" +
                    "|([0-2][0-9]))))[\\/]\\d{4}$|^\\d{4}$");
            Matcher matcher = pattern.matcher(date);
            isValid = matcher.matches();

            if (isValid) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Checks input of date for invalid numbers
     * @param input
     * @return boolean true if date input is a valid integer, otherwise false
     */
    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks input of date for invalid numbers
     * @param input
     * @return boolean true if number input is a valid double, otherwise false
     */
    private boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }
    /**
     * Checks input of date for invalid numbers
     * @param input
     * @return boolean true if number input is a valid double, otherwise false
     */
    private boolean isValidBoolean(String input) {
        try {
            Boolean.parseBoolean(input);
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

    /**
     * Exports the current database to a chosen directory in a given format when export is chosen
     * Format Account Type first initial,first name,last name,balance,date,and optional boolean value
     * @param event
     */
    @FXML
    void ExportDataBase(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        Stage primaryStage = new Stage();

        fileChooser.setTitle("Save File");
        //adds .txt extension to all text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File database = fileChooser.showSaveDialog(primaryStage);
        saveTextToFile(db.printAccountsForExport(),database);
    }
    /**
     * Saves the text from the database to a file
     * @param content,file Takes the strings in the database and file location
     */
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

    /**
     * Closes account in database when the closed account button is clicked
     * @param event
     */
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

    /**
     * Imports a text file to the database in a given format when import is chosen
     * Format Account Type first initial,first name,last name,balance,date,and optional boolean value
     * @param event
     */
    @FXML
    void importDataBase(ActionEvent event) { //TODO
        int i = 0;
        FileChooser fileChooser = new FileChooser();
        Stage primaryStage = new Stage();

        fileChooser.setTitle("Open File");
        File database = fileChooser.showOpenDialog(primaryStage);

        try {
            File myObj = new File(database.getPath());
            Scanner sc = new Scanner(myObj);


            while(sc.hasNextLine()){
                String lineRead = sc.nextLine();
                String[] accountInputs = lineRead.split(",",-2);
                //checks if input length is right
                if(accountInputs.length != 6){
                    this.output("wrong input size format\n");
                    return;
                }
                //checks if format of inputs are right
                if(accountInputs[0].length() > 1 || checkNameInOC(accountInputs[0]) == false){
                    this.output("wrong input length format\n");
                    return;
                }

                if(checkNameInOC(accountInputs[1]) == false){
                    this.output("wrong input name format\n");
                    return;
                }

                if(checkNameInOC(accountInputs[2]) == false){
                    this.output("wrong input name format\n");
                    return;
                }

                if(isValidDouble(accountInputs[3]) == false){
                    this.output("wrong input double format\n");
                    return;
                }

                if(isValidDate(accountInputs[4]) == false){
                    System.out.println(i);
                    i++;
                    this.output("wrong input date format\n");
                    return;
                }

                if(accountInputs[0].contains("S")){
                    if(isValidBoolean(accountInputs[5]) == false){
                        this.output("wrong input boolean format\n");
                        return;
                    }
                    String firstName = accountInputs[1];
                    String lastName = accountInputs[2];
                    Double balance = Double.parseDouble(accountInputs[3]);
                    String[] date = accountInputs[4].split("/", -2);

                    if(isValidInteger(date[0]) == false || isValidInteger(date[1]) == false || isValidInteger(date[2]) == false){
                        this.output("wrong input integer format\n");
                        return;
                    }
                    boolean loyalCustomer = Boolean.parseBoolean(accountInputs[5]);


                    Savings savingsAccount = new Savings(firstName, lastName, balance, Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), loyalCustomer);
                    boolean isPresent = db.add(savingsAccount);
                    if(isPresent == false){
                        this.output("Warning some accounts already in database and have not been added\n");
                    }
                }
                else if(accountInputs[0].contains("C")){
                    if(isValidBoolean(accountInputs[5]) == false){
                        this.output("wrong input boolean format\n");
                        return;
                    }
                    String firstName = accountInputs[1];
                    String lastName = accountInputs[2];
                    Double balance = Double.parseDouble(accountInputs[3]);
                    String[] date = accountInputs[4].split("/", -2);
                    if(isValidInteger(date[0]) == false || isValidInteger(date[1]) == false || isValidInteger(date[2]) == false){
                        this.output("wrong input integer format\n");
                        return;
                    }

                    boolean directDeposit = Boolean.parseBoolean(accountInputs[5]);


                    Checking checkingAccount = new Checking(firstName, lastName, balance, Integer.parseInt(date[0]),
                            Integer.parseInt(date[1]), Integer.parseInt(date[2]), directDeposit);
                    boolean isPresent = db.add(checkingAccount);
                    if(isPresent == false){
                        this.output("Warning some accounts already in database and have not been added\n");
                    }
                }
                else{
                    String firstName = accountInputs[1];
                    String lastName = accountInputs[2];
                    Double balance = Double.parseDouble(accountInputs[3]);
                    String[] date = accountInputs[4].split("/", -2);

                    if(isValidInteger(date[0]) == false || isValidInteger(date[1]) == false || isValidInteger(date[2]) == false){
                        this.output("wrong input integer format\n");
                        return;
                    }
                    if(isValidInteger(accountInputs[5]) == false){
                        this.output("wrong input integer format\n");
                        return;
                    }
                    int withdrawals = Integer.parseInt(accountInputs[5]);

                    MoneyMarket moneyMarket = new MoneyMarket(firstName, lastName, balance, Integer.parseInt(date[0]),
                            Integer.parseInt(date[1]), Integer.parseInt(date[2]), withdrawals);
                    boolean isPresent = db.add(moneyMarket);
                    if(isPresent == false){
                        this.output("Waring some accounts already in database and have not been added\n");
                    }
                }
            }
            this.output("Import completed\n");
        } catch (FileNotFoundException e) {
            this.output("File not found\n");
            e.printStackTrace();
        }
    }

    /**
     * Opens an account in a database when the opened account button is clicked
     * @param event
     */
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
            if(date.substring(1,2).contains("/")){
                monthDate = Integer.parseInt(date.substring(0,1));
                if(date.substring(3,4).contains("/")){
                    dayDate = Integer.parseInt(date.substring(2,3));
                    yearDate = Integer.parseInt(date.substring(4, 8));
                }
                else{
                    dayDate = Integer.parseInt(date.substring(2,4));
                    yearDate = Integer.parseInt(date.substring(5, 9));
                }

            }
            else if(date.substring(4,5).contains("/")){
                monthDate = Integer.parseInt(date.substring(0,2));
                dayDate = Integer.parseInt(date.substring(3,4));
                yearDate = Integer.parseInt(date.substring(5,9));
            }
            else {
                monthDate = Integer.parseInt(date.substring(0, 2));
                dayDate = Integer.parseInt(date.substring(3, 5));
                yearDate = Integer.parseInt(date.substring(6, 10));
            }
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

        //gui button conditon for checking, saving, and moneymarket
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

                if(isPresent){
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
