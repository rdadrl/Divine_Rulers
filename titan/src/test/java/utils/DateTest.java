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

        Vector3D test = new Vector3D(1000, 20, 3);
        test.length();
        Vector3D t2 = test.unit().scale(400);
        double l = t2.length();
    }

    @Test
    public void dateToJulian2() {
        Date date = new Date(2049,0,1,12,0,0);
        System.out.println(date.dateToJulian());
    }
}
