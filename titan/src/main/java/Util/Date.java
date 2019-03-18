package util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A time class used for keeping track of the date.
 * !Month starts from 0. So 1st January 2000 needs to be added as 2000, 0, 1
 */
public class Date extends GregorianCalendar {
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
        super(year, month, dayOfMonth);
    }

    public Date(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        super(year, month, dayOfMonth, hourOfDay, minute);
    }

    public Date(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        super(year, month, dayOfMonth, hourOfDay, minute, second);
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

    @Override
    public String toString() {
        return "Date{" +
                this.get(Calendar.YEAR) + "-" +
                this.get(Calendar.MONTH) + "-" +
                this.get(Calendar.DAY_OF_MONTH) + " " +
                this.get(Calendar.HOUR_OF_DAY) + ":" +
                this.get(Calendar.MINUTE) + ":" +
                this.get(Calendar.SECOND) +
                '}';
    }
}
