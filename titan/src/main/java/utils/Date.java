package utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A time class used for keeping track of the date.
 * 
 */
public class Date extends GregorianCalendar {
    public static final double J2000 = 2451545.0;
    public Date() {
    }

    public Date(TimeZone zone) {
        super(zone);
    }

    public Date(Locale aLocale) {
        super(aLocale);
    }

    public Date(TimeZone zone, Locale aLocale) {
        super(zone, aLocale);
    }

    public Date(int year, int month, int dayOfMonth) {
        super(year, month-1, dayOfMonth);
    }

    public Date(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        super(year, month-1, dayOfMonth, hourOfDay, minute);
    }

    public Date(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        super(year, month-1, dayOfMonth, hourOfDay, minute, second);
    }
    public Date(Date date){
        this(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH) ,date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE), date.get(Calendar.SECOND));
    }

    /**
     * http://scienceworld.wolfram.com/astronomy/JulianDate.html
     * @return time in julian date
     */
    public double dateToJulian() {
        int Y = this.get(Calendar.YEAR);
        int M = this.get(Calendar.MONTH) +1;
        int D = this.get(Calendar.DAY_OF_MONTH);
        double H = this.get(Calendar.HOUR_OF_DAY);
        H = H + (this.get(Calendar.MINUTE)/60D);

        return 367 * Y - Math.floor(7 * (Y + Math.floor((M + 9) / 12D)) / 4D)
                - Math.floor(3 * (Math.floor((Y + (M - 9) / 7D) / 100D) + 1) / 4D)
                + Math.floor(275 * M / 9D) + D + 1721028.5 + (H / 24D);
    }
    
    public String getDateString() {
    	return get(Calendar.YEAR) + ": " + pad(get(Calendar.MONTH)) + ": " + pad(get(Calendar.DATE));
    }
    
    public String pad(int n) {
    	String str = "" + n;
    	if(n < 10) {
    		str = "0" + str;
    	} 
    	return str;
    }

    @Override
    public String toString() {
        return "Date{" +
                this.get(Calendar.YEAR) + "-" +
                (this.get(Calendar.MONTH) + 1) + "-" +
                this.get(Calendar.DAY_OF_MONTH) + " " +
                this.get(Calendar.HOUR_OF_DAY) + ":" +
                this.get(Calendar.MINUTE) + ":" +
                this.get(Calendar.SECOND) +
                '}';
    }
}
