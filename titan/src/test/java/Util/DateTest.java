package util;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * A test class for the DateUtil class.
 */
public class DateUtilTest {

    @Test
    public void dateToJulian() {
        GregorianCalendar date = new GregorianCalendar(2000,0,1,12,0,0);
        assertEquals(DateUtil.dateToJulian(date), 2451545.0, 1e-5);
    }
}