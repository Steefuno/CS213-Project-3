package application;
/**
 * This class represents an account's profile with a first and last name
 * @author Steven Nguyen, Julian Romero
 */
public class Profile {
	private String fname;
	private String lname;
	
	/**
	 * Constructs a profile
	 * @param _fname	the first name
	 * @param _lname	the last name
	 */
	public Profile(String _fname, String _lname) {
		this.fname = _fname;
		this.lname = _lname;
	}
	
	/**
	 * Gets the first name
	 * @return	the first name
	 */
	public String getFName() {
		return this.fname;
	}
	
	/**
	 * Gets the last name
	 * @return	the last name
	 */
	public String getLName() {
		return this.lname;
	}
	
	/**
	 * Gets if an object is equal to this profile
	 * @return	true if the other object is a profile with the same name, false otherwise
	 */
	public boolean equals(Object o) {
		if (o instanceof Profile) {
			Profile profile = (Profile) o;
			return
				(this.getLName().equals(profile.getLName())) &&
				(this.getFName().equals(profile.getFName()));
		} else {
			return false;
		}
	}
	
	/**
	 * Compares this profile with another by last name
	 * @param profile	the profile to compare to
	 * @return			0 if the names are equal, 1 if this profile is after profile, -1 if this profile is before profile
	 */
	public int compareTo(Profile profile) {
		int lnameComparison = this.lname.compareTo(profile.lname);
		if (lnameComparison == 0) {
			int fnameComparison = this.fname.compareTo(profile.fname);
			return fnameComparison;
		} else {
			return lnameComparison;
		}
	}
	
	/**
	 * Gets the string representation of this profile
	 * Format: "[First Name] [Last Name]"
	 * @return	the string representation
	 */
	@Override
	public String toString() {
		return String.format("%s %s", this.fname, this.lname);
	}
	
	/**
	 * Testmain - tests the methods in this class
	 * @param args	unused
	 */
	public static void main(String[] args) {
		Profile p1 = new Profile("B", "B");
		Profile p2 = new Profile("B", "A");
		Profile p3 = new Profile("B", "C");
		Profile p4 = new Profile("A", "B");
		Profile p5 = new Profile("B", "B");
		
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		System.out.println(p5);
		
		
		if (p1.equals(p2)) {
			System.out.println("Unexpected: p1 equals p2");
		} else {
			System.out.println("Expected: p1 doesn't equal p2");
		}
		
		if (p1.equals(p3)) {
			System.out.println("Unexpected: p1 equals p3");
		} else {
			System.out.println("Expected: p1 doesn't equal p3");
		}

		if (p1.equals(p4)) {
			System.out.println("Unexpected: p1 equals p4");
		} else {
			System.out.println("Expected: p1 doesn't equal p4");
		}

		if (p1.equals(p5)) {
			System.out.println("Expected: p1 equals p5");
		} else {
			System.out.println("Unexpected: p1 doesn't equal p5");
		}
		
		
		if (p1.compareTo(p2) > 0) {
			System.out.println("Expected: p1 > p2");
		} else {
			System.out.println("Unxpected: p1 !> p2");
		}
		
		if (p1.compareTo(p3) < 0) {
			System.out.println("Expected: p1 < p2");
		} else {
			System.out.println("Expected: p1 !< p2");
		}
		
		if (p1.compareTo(p4) > 0) {
			System.out.println("Expected: p1 > p2");
		} else {
			System.out.println("Unexpected: p1 !> p2");
		}
		
		if (p1.compareTo(p5) == 0) {
			System.out.println("Expected: p1 == p2");
		} else {
			System.out.println("Unexpected: p1 != p2");
		}
	}
}
