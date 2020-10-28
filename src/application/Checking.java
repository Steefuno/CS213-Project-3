package application;
/**
 * This class represents a checking account<br>
 * Checking accounts have a monthly fee of 25, a minimum balance of 1500 or direct deposit to waive the fee, and an annual interest rate of .05%
 * @author Steven Nguyen, Julian Romero
 */
public class Checking extends Account {
	private boolean directDeposit;
	private static final double monthlyFee = 25;
	private static final double waiveBalanceMinimum = 1500;
	private static final double annualInterestRate = .0005;

	private static final int fillerBalance = 0;
	private static final int fillerMonth = 1;
	private static final int fillerDay = 1;
	private static final int fillerYear = 2000;
	private static final boolean fillerDirectDeposit = false;
	
	/**
	 * Constructs a checking account
	 * @param _fname			the first name of profile
	 * @param _lname			the last name of the profile
	 * @param _balance			the initial balance
	 * @param _month			the month of the open date
	 * @param _day				the day of the open date
	 * @param _year				the year of the open date
	 * @param _directDeposit	if the account has direct deposit enabled
	 */
	public Checking(String _fname, String _lname, double _balance, int _month, int _day, int _year, boolean _directDeposit) {
		super(_fname, _lname, _balance, _month, _day, _year);
		this.directDeposit = _directDeposit;
	}
	
	/**
	 * Constructs a temporary checking account used for comparisons
	 * @param _fname	the first name of the profile
	 * @param _lname	the last name of the profile
	 */
	public Checking(String _fname, String _lname) {
		super(_fname, _lname, fillerBalance, fillerMonth, fillerDay, fillerYear);
		this.directDeposit = fillerDirectDeposit;
	}
	
	/**
	 * Calculate the monthly interest for this account
	 */
	@Override
	public double monthlyInterest() {
		return this.getBalance() * (annualInterestRate / 12);
	}
	
	/**
	 * Calculate the monthly fee for this account
	 */
	@Override
	public double monthlyFee() {
		if (this.getBalance() >= waiveBalanceMinimum || this.directDeposit) {
			return 0;
		} else {
			return monthlyFee;
		}
	}
	
	/**
	 * Gets if this checking account has direct deposit
	 * @return	true if this checking account has direct deposit, false otherwise
	 */
	public boolean getIsDirectDeposit() {
		return directDeposit;
	}
	
	/**
	 * Gets the string representation of this checking account<br>
	 * Format: "*Checking*[Profile Name]* $[Balance]*[Open Date]*"<br>
	 * "direct deposit account*" is appended if the account has direct deposit enabled
	 * @return	the string representation
	 */
	@Override
	public String toString() {
		if (this.directDeposit) {
			return String.format("*%s%s*%s*", "Checking", super.toString(), "direct deposit account");
		} else {
			return String.format("*%s%s", "Checking", super.toString());
		}
	}
	
	/**
	 * Gets if an object is equal to this checking account
	 * @return	true if the other object is a checking account with the same name, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Checking) {
			return super.equals(o);
		} else {
			return false;
		}
	}
}