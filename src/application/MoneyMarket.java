package application;
/**
 * This class represents a money market account<br>
 * Checking accounts have a monthly fee of 6, a minimum balance of 2500 unless the withdrawals has surpassed 6, and an annual interest rate of .65%
 * @author Steven Nguyen, Julian Romero
 */
public class MoneyMarket extends Account {
	private int withdrawals = 0;
	private static final int maxWithdrawals = 6;
	private static final double monthlyFee = 12;
	private static final double waiveBalanceMinimum = 2500;
	private static final double annualInterestRate = .0065;
	
	private static final int fillerBalance = 0;
	private static final int fillerMonth = 1;
	private static final int fillerDay = 1;
	private static final int fillerYear = 2000;
	
	/**
	 * Constructs a money market account
	 * @param _fname	the first name of the profile
	 * @param _lname	the last name of the profile
	 * @param _balance	the initial balance
	 * @param _month	the month of the open date
	 * @param _day		the day of the open date
	 * @param _year		the year of the open date
	 */
	public MoneyMarket(String _fname, String _lname, double _balance, int _month, int _day, int _year) {
		super(_fname, _lname, _balance, _month, _day, _year);
	}
	
	/**
	 * Constructs a temporary money market account used for comparisons
	 * @param _fname	the first name of the profile
	 * @param _lname	the last name of the profile
	 */
	public MoneyMarket(String _fname, String _lname) {
		super(_fname, _lname, fillerBalance, fillerMonth, fillerDay, fillerYear);
	}
	
	/**
	 * Withdraw money
	 * @param amount	the amount of money to withdraw
	 */
	@Override
	public void debit(double amount) {
		withdrawals++;
		super.debit(amount);
	}
	
	/**
	 * Calculate the monthly interest
	 * @return	the monthly interest
	 */
	@Override
	public double monthlyInterest() {
		return this.getBalance() * (annualInterestRate / 12);
	}
	
	/**
	 * Calculate the monthly fee
	 * @return	the monthly fee
	 */
	@Override
	public double monthlyFee() {
		if (this.getBalance() >= waiveBalanceMinimum && this.withdrawals <= maxWithdrawals) {
			return 0;
		} else {
			return monthlyFee;
		}
	}
	
	/**
	 * Gets the string representation of this money market account<br>
	 * Format: "*Money Market*[Profile Name]* $[Balance]*[Open Date]* [Number of withdrawals] withdrawals*
	 * @return	the string representation
	 */
	@Override
	public String toString() {
		return String.format("*%s%s*%d withdrawals*", "Money Market", super.toString(), this.withdrawals);
	}
	
	/**
	 * Gets if this account is equal to an object
	 * @param o	the object to compare to this account
	 * @return	true if the object is a money market account with the same name
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof MoneyMarket) {
			return super.equals(o);
		} else {
			return false;
		}
	}
}
