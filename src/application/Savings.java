package application;
/**
 * This class represents a savings account<br>
 * Checking accounts have a monthly fee of 5, a minimum balance of 300 to waive the fee, and an annual interest rate of .25%<br>
 * the annual interest rate is .35% on loyal accounts
 * @author Steven Nguyen, Julian Romero
 */
public class Savings extends Account {
	private boolean isLoyal;
	private static final double monthlyFee = 5;
	private static final double waiveBalanceMinimum = 300;
	private static final double annualInterestRate = .0025;
	private static final double loyalAnnualInterestRate = .0035;

	private static final int fillerBalance = 0;
	private static final int fillerMonth = 1;
	private static final int fillerDay = 1;
	private static final int fillerYear = 2000;
	private static final boolean fillerIsLoyal = false;
	
	/**
	 * Constructs a savings account
	 * @param _fname	the first name of the profile
	 * @param _lname	the last name of the profile
	 * @param _balance	the initial balance
	 * @param _month	the month of the open date
	 * @param _day		the day of the open date
	 * @param _year		the year of the open date
	 * @param _isLoyal	true if the account is loyal, false otherwise
	 */
	public Savings(String _fname, String _lname, double _balance, int _month, int _day, int _year, boolean _isLoyal) {
		super(_fname, _lname, _balance, _month, _day, _year);
		this.isLoyal = _isLoyal;
	}
	
	/**
	 * Constructs a temporary savings account used for comparisons
	 * @param _fname	the first name of the profile
	 * @param _lname	the last name of the profile
	 */
	public Savings(String _fname, String _lname) {
		super(_fname, _lname, fillerBalance, fillerMonth, fillerDay, fillerYear);
		this.isLoyal = fillerIsLoyal;
	}

	/**
	 * Calculate the monthly interest for this account
	 */
	@Override
	public double monthlyInterest() {
		double rate = annualInterestRate;
		if (this.isLoyal) {
			rate = loyalAnnualInterestRate;
		}
		
		return this.getBalance() * (rate / 12);
	}

	/**
	 * Calculate the monthly fee for this account
	 */
	@Override
	public double monthlyFee() {
		if (this.getBalance() >= waiveBalanceMinimum) {
			return 0;
		} else {
			return monthlyFee;
		}
	}
	
	/**
	 * Gets if this account is loyal
	 * @return	true if this account is loyal, false otherwise
	 */
	public boolean getIsLoyal() {
		return this.isLoyal;
	}

	/**
	 * Gets the string representation of this money market account<br>
	 * Format: "*Money Market*[Profile Name]* $[Balance]*[Open Date]*<br>
	 * appends "special savings account*" at the end if the account is loyal
	 * @return	the string representation
	 */
	@Override
	public String toString() {
		if (this.isLoyal) {
			return String.format("*%s%s*%s*", "Savings", super.toString(), "special savings account");
		} else {
			return String.format("*%s%s", "Savings", super.toString());
		}
	}

	/**
	 * Gets if this account is equal to an object
	 * @param o	the object to compare to this account
	 * @return	true if the object is a savings account with the same name
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Savings) {
			return super.equals(o);
		} else {
			return false;
		}
	}
	
	/**
	 * Testmain - tests the methods in this class
	 * @param args	unused
	 */
	public static void main(String[] args) {
		Savings s1 = new Savings("A", "A", 100, 1, 1, 2000, false);
		Savings s2 = new Savings("A", "A", 100, 1, 1, 2000, false);
		Savings s3 = new Savings("A", "B", 100, 1, 1, 2000, true);
		
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s3);

		System.out.println("s1:\n\tbalance: " + s1.getBalance() + "\n\tinterest: " + s1.monthlyInterest() + "\n\tfee: " + s1.monthlyFee());
		System.out.println("s2:\n\tbalance: " + s2.getBalance() + "\n\tinterest: " + s2.monthlyInterest() + "\n\tfee: " + s2.monthlyFee());
		System.out.println("s3:\n\tbalance: " + s3.getBalance() + "\n\tinterest: " + s3.monthlyInterest() + "\n\tfee: " + s3.monthlyFee());
		
		if (s1.equals(s2)) {
			System.out.println("Expected: s1 == s2");
		} else {
			System.out.println("Unexpected: s1 != s2");
		}
		
		if (s1.equals(s3)) {
			System.out.println("Unexpected: s1 == s3");
		} else {
			System.out.println("Expected: s1 != s3");
		}
	}
}
