package Util;

import java.util.Calendar;

/**
 * A time class used for keeping track of the date.
 * !Month starts from 0. So 1st January 2000 needs to be added as 2000, 0, 1
 */
public class Date {
    private Calendar calendar;

    public Date() {
        this.calendar = Calendar.getInstance();
    }
    public Date(int Y, int M, int D, int Hr, int Min, int Sec){
        this();
        calendar.set(Y,  M , D, Hr, Min, Sec);
    }
    public Date(int Y, int M, int D){
        this(Y, M, D, 12, 0, 0);
    }

    /**
     * http://scienceworld.wolfram.com/astronomy/JulianDate.html
     * @return time in julian date
     */
    public double dateToJulian() {
        int Y = calendar.get(Calendar.YEAR);
        int M = calendar.get(Calendar.MONTH) +1;
        int D = calendar.get(Calendar.DAY_OF_MONTH);
        double H = calendar.get(Calendar.HOUR_OF_DAY);
        H = H + (calendar.get(Calendar.MINUTE)/60D);

        double JD = 367 * Y - Math.floor(7 * (Y + Math.floor((M + 9) / 12D)) / 4D)
                - Math.floor(3 * (Math.floor((Y + (M - 9) / 7D) / 100D) + 1) / 4D)
                + Math.floor(275 * M / 9D) + D + 1721028.5 + (H / 24D);
        return JD;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
