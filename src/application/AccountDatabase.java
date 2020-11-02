package application;
import java.text.DecimalFormat;

/**
 * This class handles storing and handling accounts
 * @author Steven Nguyen, Julian Romero
 */
public class AccountDatabase {
    private Account[] accounts;
    private int size;

    private static final int GROW_SIZE = 5;

    /**
     * Constructs a default account database with 0 accounts
     */
    public AccountDatabase() {
        this.size = 0;
        this.accounts = new Account[5];
    }
    public int getsize() {
        return this.size;
    }

    /**
     * Searches for an account with a given First and Last name
     * @param account	an account with the same first and last name as the account to find
     * @return			the index of the found account or -1
     */
    private int find(Account account) {
        for (int i = 0; i < this.size; i++) {
            if (accounts[i].equals(account)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Raises the pseudo arraylist's size
     */
    private void grow() {
        Account[] expandedAccounts = new Account[this.accounts.length + GROW_SIZE];
        System.arraycopy(this.accounts, 0, expandedAccounts, 0, this.accounts.length);
        this.accounts = expandedAccounts;
    }

    /**
     * Adds an account to the database
     * @param account	the account to add to the database
     * @return			true if successfully added, false otherwise
     */
    public boolean add(Account account) {
        if (this.find(account) != -1) {
            return false;
        }

        if ((size + 1) % GROW_SIZE == 0) {
            this.grow();
        }

        this.accounts[size] = account;
        size++;
        return true;
    }

    /**
     * Removes an account from the database
     * @param account	an account with the same first and last name as the account to remove
     * @return			true if the account was successfully removed, false otherwise
     */
    public boolean remove(Account account) {
        int accountIndex = this.find(account);
        if (accountIndex == -1) {
            return false;
        }

        for (int i = accountIndex; i < size; i++) {
            accounts[i] = accounts[i + 1];
        }

        size--;
        accounts[size] = null;
        return true;
    }

    /**
     * Deposits money into an account
     * @param account	an account with the same first and last name as the account to add to
     * @param amount	the amount of money to add to the account
     * @return			true if the money is successfully deposited, false otherwise
     */
    public boolean deposit(Account account, double amount) {
        int accountIndex = this.find(account);
        if (accountIndex == -1) {
            return false;
        }

        account = accounts[accountIndex];

        account.credit(amount);
        return true;
    }

    /**
     * Withdraws money from an account
     * @param account	an account with the same first and last name as the account to withdraw from
     * @param amount	the amount of money to withdraw
     * @return			-1 if the account is not found, 1 if the account doesn't have enough money to withdraw,<br>
     * and 0 if successfully withdrawn
     */
    public int withdrawal(Account account, double amount) {
        int accountIndex = this.find(account);
        if (accountIndex == -1) {
            return -1;
        }

        account = accounts[accountIndex];
        if (account.getBalance() - amount < 0) {
            return 1;
        }

        account.debit(amount);
        return 0;
    }

    /**
     * Recursively sorts the accounts by dateOpened<br>
     * Uses quicksort with the pivot as the last element<br>
     * Sorts by last name if the dates are the same
     * @param startIndex	the start index of accounts to start sorting from
     * @param endIndex		the index after the last index of accounts to sort from
     */
    private void sortByDateOpenHelper(int startIndex, int endIndex) {
        int pivotIndex = endIndex - 1;

        if (pivotIndex <= startIndex)
            return;

        Date pivot = accounts[pivotIndex].getOpenDate();
        Profile pivotProfile = accounts[pivotIndex].getProfile();
        int smallerIndex = startIndex;

        // For each account, if it is to be ordered before the pivot, swap with the index after the previously ordered account
        for (int i = startIndex; i < endIndex; i++) {
            Account account = accounts[i];
            Date date = account.getOpenDate();

            int dateComparison = date.compareTo(pivot);
            if (dateComparison < 0) {
                accounts[i] = accounts[smallerIndex];
                accounts[smallerIndex] = account;
                smallerIndex++;
                // Compares the account names if they were opened on the same day
            } else if (dateComparison == 0) {
                int nameComparison = account.getProfile().compareTo(pivotProfile);
                if (nameComparison < 0) {
                    accounts[i] = accounts[smallerIndex];
                    accounts[smallerIndex] = account;
                    smallerIndex++;
                }
            }
        }

        // Swaps the pivot with the index after the previously ordered account
        Account account = accounts[smallerIndex];
        accounts[smallerIndex] = accounts[pivotIndex];
        accounts[pivotIndex] = account;

        // Sorts the left and right partition of the pivot
        sortByLastNameHelper(startIndex, smallerIndex);
        sortByLastNameHelper(smallerIndex + 1, endIndex);
        return;
    }

    /**
     * Calls sortByDateOpenHelper to recursively sort accounts by date opened
     */
    private void sortByDateOpen() {
        sortByDateOpenHelper(0, size);
    }

    /**
     * Recursively sorts the accounts by last name<br>
     * Uses quicksort with the pivot as the last element<br>
     * Sorts by first name if the last names are the same
     * @param startIndex	the start index of accounts to start sorting from
     * @param endIndex		the index after the last index of accounts to sort from
     */
    private void sortByLastNameHelper(int startIndex, int endIndex) {
        int pivotIndex = endIndex - 1;

        if (pivotIndex <= startIndex)
            return;

        Profile pivot = accounts[pivotIndex].getProfile();
        int smallerIndex = startIndex;

        // For each account, if it is to be ordered before the pivot, swap with the index after the previously ordered account
        for (int i = startIndex; i < endIndex; i++) {
            Account account = accounts[i];
            Profile profile = account.getProfile();

            int nameComparison = profile.compareTo(pivot);
            if (nameComparison < 0) {
                accounts[i] = accounts[smallerIndex];
                accounts[smallerIndex] = account;
                smallerIndex++;
            }
        }

        // Swaps the pivot with the index after the previously ordered account
        Account account = accounts[smallerIndex];
        accounts[smallerIndex] = accounts[pivotIndex];
        accounts[pivotIndex] = account;

        // Sorts the left and right partition of the pivot
        sortByLastNameHelper(startIndex, smallerIndex);
        sortByLastNameHelper(smallerIndex + 1, endIndex);
        return;
    }

    /**
     * Calls sortByLastNameHelper to recursively sort accounts by last name
     */
    private void sortByLastName() {
        sortByLastNameHelper(0, this.size);
    }

    /**
     * Updates an account's balance for a new month with interest and the monthly fee
     * @param accountIndex	the index of the account to update in accounts
     * @return				the string that represents this update for the output
     */
    private String updateAccount(int accountIndex) {
        Account account = accounts[accountIndex];
        double interest = account.monthlyInterest();
        double fee = account.monthlyFee();
        double newBalance = account.getBalance() + interest - fee;

        String priceFormat = "$ #.#";
        DecimalFormat formattedPrice = new DecimalFormat(priceFormat);
        formattedPrice.setMinimumFractionDigits(2);

        String result = String.format(
                "\n%s\n-interest: %s\n-fee: %s\n-new balance: %s",
                account,
                formattedPrice.format(interest),
                formattedPrice.format(fee),
                formattedPrice.format(newBalance)
        );

        account.credit(interest);
        account.debit(fee);
        return result;
    }

    /**
     * Sorts accounts by date opened, updates all the accounts, then outputs them
     * @return	a string to output to the UI
     */
    public String printByDateOpen() {
        if (size == 0) {
            return "Database is empty\n";
        }

        sortByDateOpen();

        String result = "";
        for (int i = 0; i < this.size; i++) {
            result += updateAccount(i) + "\n";
        }

        return result;
    }

    /**
     * Sorts accounts by last name, updates all the accounts, then outputs them
     * @return	a string to output to the UI
     */
    public String printByLastName() {
        if (size == 0) {
            return "Database is empty\n";
        }

        sortByLastName();

        String result = "";
        for (int i = 0; i < this.size; i++) {
            result += updateAccount(i) + "\n";
        }

        return result;
    }

    /**
     * Returns a string of all the accounts
     * @return	a string to output to the UI
     */
    public String printAccounts() {
        if (size == 0) {
            return "Database is empty\n";
        }

        String result = "";
        for (int i = 0; i < this.size; i++) {
            result += accounts[i].toString() + "\n";
        }

        return result;
    }
    /**
     * Returns a string of all the accounts in the format for export
     * Format Account Type first letter,first name, last name, balance, date, and boolean or int value
     * @return	a string that is to be used for export to a file
     */
    public String printAccountsForExport() {
        int sizeOfBooleanAccounts = 5;
        if (size == 0) {
            return "Database is empty\n";
        }

        String result = "";

        for (int i = 0; i < this.size; i++) {
            //gets account type
            String typeOfAccount = this.accounts[i].getClass().getSimpleName();
            //tokens to be able to get boolean value from Accounts
            String[] token = accounts[i].toString().split("\\*",-2);

            //conditionals to print the right format for each account type
            if(typeOfAccount.substring(0, 1).contains("C")) {
                if(token.length == sizeOfBooleanAccounts) {
                    String booleanValue = ",false";
                    result += typeOfAccount.substring(0, 1) + this.accounts[i].toStringExport() + booleanValue + "\n";
                }
                else{
                    String booleanValue = ",true";
                    result += typeOfAccount.substring(0, 1) + this.accounts[i].toStringExport() + booleanValue + "\n";

                }
            }
            else if(typeOfAccount.substring(0, 1).contains("S")){
                if(token.length == sizeOfBooleanAccounts) {
                    String booleanValue = ",false";
                    result += typeOfAccount.substring(0, 1) + this.accounts[i].toStringExport() + booleanValue + "\n";
                }
                else{
                    String booleanValue = ",true";
                    result += typeOfAccount.substring(0, 1) + this.accounts[i].toStringExport() + booleanValue + "\n";

                }
            }
            else{
                String[] array = this.accounts[i].toString().split("\\*",-2);
                String withdrawals = "," + array[array.length-2].substring(0,1);
                result += typeOfAccount.substring(0, 1) + this.accounts[i].toStringExport() + withdrawals + "\n";
            }
        }

        return result;
    }

    /**
     * Testmain - tests methods used in AccountDatabase.java
     * @param args	unused
     */
    public static void main(String[] args) {
        AccountDatabase db = new AccountDatabase();

        // Tests if the prints are correct when the database is empty
        db.printAccounts();
        db.printByLastName();
        //db.printByDateOpen();

        // Tests adding different types of accounts
        if (db.add(new Checking("A", "A", 100, 2, 2, 2001, false)) == true) {
            System.out.println("Expected: added A A Checking");
        } else {
            System.out.println("Unexpected: cannot add A A Checking");
        }

        if (db.add(new MoneyMarket("A", "Banana", 100, 2, 1, 2001)) == true) {
            System.out.println("Expected: added A Banana MoneyMarket");
        } else {
            System.out.println("Unexpected: cannot add A Banana MoneyMarket");
        }

        if (db.add(new Checking("B", "A", 100, 2, 2, 2000, false)) == true) {
            System.out.println("Expected: added B A Checking");
        } else {
            System.out.println("Unexpected: cannot add B A Checking");
        }

        if (db.add(new Savings("C", "C", 100, 1, 2, 2001, false)) == true) {
            System.out.println("Expected: added C C Savings");
        } else {
            System.out.println("Unexpected: cannot add C C Savings");
        }

        // Tests is adding a duplicate account is invalid
        if (db.add(new Checking("A", "A", 100, 2, 1, 2001, false)) == true) {
            System.out.println("Unexpected: added another A A Checking");
        } else {
            System.out.println("Expected: cannot add another A A Checking");
        }

        // Tests if depositing into an account works
        if (db.deposit(new Checking("A", "A"), 50) == true) {
            System.out.println("Expected: deposited into A A Checking");
        } else {
            System.out.println("Unexpected: cannot deposit into A A Checking");
        }

        // Tests if depositing into an invalid account is invalid
        if (db.deposit(new Checking("Z", "Z"), 50) == true) {
            System.out.println("Unexpected: deposited into account that does not exist");
        } else {
            System.out.println("Expected: cannot deposit into account that does not exists");
        }

        // Tests if withdrawing from an invalid account is invalid
        int withdrawResult = db.withdrawal(new Checking("Z", "Z"), 100);
        if (withdrawResult == -1) {
            System.out.println("Expected: cannot withdraw from account that does not exist");
        } else {
            System.out.println("Unexpected: withdraw returned: " + withdrawResult);
        }

        // Tests if withdrawing from an account with insufficient balance is invalid
        withdrawResult = db.withdrawal(new Savings("C", "C"), 101);
        if (withdrawResult == 1) {
            System.out.println("Expected: account C C Savings does not have enough balance");
        } else {
            System.out.println("Unexpected: withdraw returned: " + withdrawResult);
        }

        // Tests if withdrawing from a valid account is valid
        withdrawResult = db.withdrawal(new Savings("C", "C"), 100);
        if (withdrawResult == 0) {
            System.out.println("Expected: withdrew 100 from account C C Savings");
        } else {
            System.out.println("Unexpected: withdraw returned: " + withdrawResult);
        }

        // Tests outputting accounts with accounts stored
        db.printAccounts();
        db.printByLastName();
        //db.printByDateOpen();

        // Tests removing an account with a matching name, but different account type is invalid
        if (db.remove(new Savings("A", "A")) == true) {
            System.out.println("Unexpected: removed A A Savings that shouldn't exist");
        } else {
            System.out.println("Expected: cannot remove an account that doesn't exist");
        }

        // Tests if removing a valid account is valid
        if (db.remove(new Checking("A", "A")) == true) {
            System.out.println("Expected: removed A A Checking");
        } else {
            System.out.println("Unexpected: cannot remove A A Checking");
        }

        // Tests if removing a removed account is valid
        if (db.remove(new Checking("A", "A")) == true) {
            System.out.println("Unexpected: removed A A Checking that shouldn't exist");
        } else {
            System.out.println("Expected: cannot remove an account that doesn't exist");
        }

        // Tests if removed accounts are not outputted
        db.printAccounts();
        db.printByLastName();
        //db.printByDateOpen();

        // Tests if more than 5 accounts can be added
        for (int i = 1; i < 10; i++) {
            db.add(new Checking("F" + i, "L" + i, 100, 1, i, 2002, false));
        }

        // Tests if more than 5 accounts can be outputted
        db.printAccounts();
        db.printByLastName();
        //db.printByDateOpen();
    }
}
