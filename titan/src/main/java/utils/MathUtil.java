package utils;

import javafx.geometry.Point3D;
import utils.vector.Vector3D;

/**
 *
 *
 */
public class MathUtil {
    public static final double AU = 149597870700.0;    // AU in m
    public static final double GAU = 1.993E-44;          // AU^3/kg*s^2
    public static final double G =  6.6742867E-11;         // m/kg*s^2


    /**
     * Sinus function for degrees
     * @param degree degree
     * @return sin(degree)
     */
    public static double sinDegree(double degree){
        return(Math.sin(Math.toRadians(degree)));
    }

    /**
     * Cosine function for degrees
     * @param degree degree
     * @return cos(degree)
     */
    public static double cosDegree(double degree){
        return(Math.cos(Math.toRadians(degree)));
    }

    /**
     * https://forgetcode.com/Java/1747-acosh-Return-the-hyperbolic-Cosine-of-value-as-a-Argument
     * @param x
     * @return acosh of x
     */
    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x * x - 1.0));
    }

    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }
    /**
     * ABC formula solver
     * @param A A constant
     * @param B B constant
     * @param C C constant
     * @return array including the discriminant, solution 1 and solution 2.
     */
    public static double[] ABCformula(double A, double B, double C){
        // discriminant
        double D = B * B - 4 * A * C;
        double s1 = (-B -Math.sqrt(D)) / (2.0 * A);
        double s2 = (-B + Math.sqrt(D)) / (2.0 * A);
        return new double[]{D, s1,s2};
    }


    //TODO: rewrite

    /**
     * Function the check whether there is a collision between the rocket/cannonball and
     * a planet. We check whether the line between the rocket at position x(t) and x(t + dt)
     * intersects the spehere
     * https://stackoverflow.com/questions/5883169/intersection-between-a-line-and-a-sphere
     * @param fromPos starting position
     * @param toPos ending position
     * @param sphereCenter center of the sphere
     * @param sphereRadius radius of the sphere
     * @return whether a line intersects a sphere
     */
    public static Point3D[] collisionDetector(Vector3D fromPos, Vector3D toPos,
                                              Vector3D sphereCenter, double sphereRadius)
    {
        double cx = sphereCenter.getX();
        double cy = sphereCenter.getY();
        double cz = sphereCenter.getZ();

        double x1 = fromPos.getX();
        double y1 = fromPos.getY();
        double z1 = fromPos.getZ();

        double dx = toPos.getX() - x1;
        double dy = toPos.getY() - y1;
        double dz = toPos.getZ() - z1;

        double A =  (dx * dx) + (dy * dy) + (dz * dz);
        double B =  2.0 * ((x1 * dx) + (y1 * dy) + (z1 * dz) - (dx * cx) - (dy * cy) - (dz * cz));
        double C =  (x1 * x1) - (2 * x1 * cx) + (cx * cx) + (y1 * y1) - (2 * y1 * cy) + (cy * cy) +
                    (z1 * z1) - (2 * z1 * cz) + (cz * cz) - (sphereRadius * sphereRadius);

        // see if there is a solutions
        double[] sol = ABCformula(A, B, C);
        double D  = sol[0]; // discriminant
        double s1 = sol[1]; // solution 1
        double s2 = sol[2]; // solution 2

        // calculate both the crashing point
        Point3D solution1 = new Point3D(
                fromPos.getX() * (1 - s1) + s1 * toPos.getX(),
                fromPos.getY() * (1 - s1) + s1 * toPos.getY(),
                fromPos.getZ() * (1 - s1) + s1 * toPos.getZ());

        Point3D solution2 = new Point3D(
                x1 * (1 - s2) + s2 * x1,
                y1 * (1 - s2) + s2 * y1,
                z1 * (1 - s2) + s2 * z1);

        if  (D < 0 || s1 > 1 || s1 < 0 || s2 >1 || s2 < 0)
        {
            return null; // the line does not intersect with the sphere
        }
        else if (D == 0)
        {
            return new Point3D[]{solution1}; // line intersects on one point
            //TODO: Would this mean a succesfull landing
        }
        else
        {
            return new Point3D[]{solution1, solution2}; // line intersect on two points
            //TODO: figure out which point it the rocket crashed.
        }
    }
}
