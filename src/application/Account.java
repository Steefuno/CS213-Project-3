package application;
import java.text.DecimalFormat;

/**
 * This class represents a generic Account
 * @author Steven Nguyen, Julian Romero
 */
public abstract class Account {
	private Profile holder;
	private double balance;
	private Date dateOpen;
	
	/**
	 * Constructs an account
	 * @param _fname	the first name of the profile
	 * @param _lname	the last name of the profile
	 * @param _balance	the initial balance of the profile
	 * @param _month	the month of the open date
	 * @param _day		the day of the open date
	 * @param _year		the year of the open date
	 */
	public Account(String _fname, String _lname, double _balance, int _month, int _day, int _year) {
		this.holder = new Profile(_fname, _lname);
		this.balance = _balance;
		this.dateOpen = new Date(_month, _day, _year);
	}
	
	/**
	 * Remove money from this account
	 * @param amount	the amount of money to remove
	 */
	public void debit(double amount) {
		balance -= amount;
	}
	
	/**
	 * Add money to this account
	 * @param amount	the amount of money to add
	 */
	public void credit(double amount) {
		balance += amount;
	}
	
	/**
	 * Gets the balance
	 * @return	the balance
	 */
	public double getBalance() {
		return this.balance;
	}
	
	/**
	 * Gets the profile
	 * @return	the profile
	 */
	public Profile getProfile() {
		return this.holder;
	}
	
	/**
	 * Gets the open date of this account
	 * @return	the open date
	 */
	public Date getOpenDate() {
		return this.dateOpen;
	}
	

	/**
	 * Gets the string representation of this money market account<br>
	 * Format: "*[Profile Name]* $[Balance]*[Open Date]*"
	 * @return	the string representation
	 */
	@Override
	public String toString() {
		String priceFormat = "$#.#";
		DecimalFormat formattedPrice = new DecimalFormat(priceFormat);
		formattedPrice.setMinimumFractionDigits(2);
		
		return String.format("*%s* %s*%s", this.holder, formattedPrice.format(this.balance), this.dateOpen);
	}

	/**
	 * Gets if this account is equal to an object
	 * @param o	the object to compare to this account
	 * @return	true if the object is an account with the same name
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Account) {
			Account account = (Account) o;
			return this.getProfile().equals(account.getProfile());
		} else {
			return false;
		}
	}
	
	/**
	 * Calculates the monthly interest
	 * @return	the monthly interest
	 */
	public abstract double monthlyInterest();
	
	/**
	 * Calculates the monthly fee
	 * @return	the monthly fee
	 */
	public abstract double monthlyFee();
}
