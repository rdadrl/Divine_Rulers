package utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A test class for the Date class.
 */
public class DateTest {

    @Test
    public void dateToJulian() {
        Date date = new Date(2000,0,1,12,0,0);
        assertEquals(date.dateToJulian(), 2451545.0, 1e-5);
    }
}