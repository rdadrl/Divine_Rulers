package utils;

/**
 *
 *
 */
public class MathUtil {
    public static final double AU = 149597870700.0;    // AU in m
    public static final double GAU = 1.993E-44;          // AU/kg*s^2
    public static final double Gm =  6.6742867E-11;         // AU/kg*s^2


    public static double sinDegree(double degree){
        return(Math.sin(Math.toRadians(degree)));
    }

    public static double cosDegree(double degree){
        return(Math.cos(Math.toRadians(degree)));
    }
}
