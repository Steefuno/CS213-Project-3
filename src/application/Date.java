package application;

/**
 * This class represents a date following the format mm/dd/yyyy
 * @author Steven Nguyen, Julian Romero
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    /**
     * Constructs a date object
     * @param month	the month of the open date
     * @param day		the day of the open date
     * @param year		the year of the open date
     */
    public Date(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    /**
     * Compares the instance with a date object<br>
     * returns -1 if the date is not valid<br>
     * returns 1 if the date object is the same and
     * @param date	takes a date object
     * @return the integer value if it is valid , or equal to the object or not
     */
    public int compareTo(Date date) {
        String dateToString = date.toString();
        if (isValid(dateToString)) {
            if(this.equals(date)) {
                return 1;
            }
            else {
                return 0;
            }
        }
        else {
            return -1;
        }
    }

    /**
     * Gets the string representation of this Date<br>
     * Format: *Month*, *day* ,*year*
     * @return	the string representation
     */
    public String toString() {
        String output = String.format("%d/%d/%d", this.month, this.day, this.year);
        return output;
    }

    /**
     * Checks whether or not the instance is in a valid date format<br>
     * Format:  *Month*, *day* ,*year*<br>
     * @return the boolean value if it is a valid date format
     */

    /**
     * Checks input of date for invalid dates
     * @param date
     * @return boolean true if input is a valid date and format, otherwise false
     */
    private boolean isValid(String date) {
        int leapYearDay = 29;
        int leapYearDivisor = 4;
        //length of inputs
        int lengthOfDayInput = 2;
        int lengthOfYearInput = 4;
        int lengthOfMonthInput = 2;

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
        if(numbers[0].length() > lengthOfMonthInput || numbers[1].length() > lengthOfDayInput
                || numbers[2].length() > lengthOfYearInput){
            return false;
        }
        int month = Integer.parseInt(numbers[0]);
        int day = Integer.parseInt(numbers[1]);
        int year = Integer.parseInt(numbers[2]);

        if(month > 12 || day > 31 || month < 1||day < 1){
            return false;
        }

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
        else{
            return true;
        }
    }
}
