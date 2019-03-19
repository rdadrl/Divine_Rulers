package solarsystem;

import utils.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;


/**
 *
 *
 */
public class KeplerToCartesian {



    /**
     * source https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
     * https://downloads.rene-schwarz.com/download/M001-Keplerian_Orbit_Elements_to_Cartesian_State_Vectors.pdf
     * @param date date for which the solar system need to be configured
     */
    public static Coordinate[] getCartesianCoordinates(
            double a, double e, double i, double l, double w, double o,
            double mu){
        // Step 2
        // compute the argument of perihelion, m, and the mean anomaly, M
        double m = w - o;
        double M = l - w;

        // Step 3
        // solve keplers equation
        double EPSILON = 10e-6;
        double eStar = (180D/Math.PI) * e; // e0 star to radians

        double E = M + eStar * sinD(M);
        double deltaM;
        double deltaE;
        do{
            deltaM = M - (E - eStar * sinD(E));
            deltaE = deltaM / (1.0 - e * cosD(E));
            E = E + deltaE;
        }while(Math.abs(deltaE) > EPSILON);

        /*
        double vt = 2 * Math.atan2(
                Math.sqrt(1.0 + e) * sinD(E / 2D),
                Math.sqrt(1.0 - e) * cosD(E / 2D));
        */

        // Step 4
        // compute the planet's heliocentric coordinates in its orbital
        // plane, roPos', with the x'-axis aligned from the focus to the perihelion
        Coordinate orbitalPos; // Coordinate in the orbital plane
        Coordinate orbitalVel; // Velocity vector in the orbital plane
        Coordinate centralPos; // Coordinate central body reference frame
        Coordinate centralVel; // Velocity central body reference frame


        double radius = a * (1 - e * cosD(E));

        orbitalPos = new Coordinate();
        orbitalPos.setX(a * (cosD(E) - e));
        orbitalPos.setY(a * Math.sqrt(1.0 - e * e) * sinD(E));
        orbitalPos.setZ(0.0);

        orbitalVel = new Coordinate();
        orbitalVel.setX(-sinD(E));
        orbitalVel.setY(Math.sqrt(1.0 - e * e) * cosD(E));
        orbitalVel.setZ(0.0);


        orbitalVel = orbitalVel.scale(Math.sqrt(mu * a)/radius);


        // Step 5
        // compute the coordinates in the J2000 ecliptic plane, with the x-axis
        // aligned toward the equinox:
        centralPos = OrbitalCoordinateToHEECoordinate(m, o, i, orbitalPos);
        centralVel = OrbitalCoordinateToHEECoordinate(m, o, i, orbitalVel);
        centralVel = centralVel.scale(60*60*24);

        /*
        // Step 6
        double obliquity = 34.43928;

        double xeq = xelc;
        double yeq = cosD(obliquity) * yelc - sinD(obliquity) * zelc;
        double zeq = sinD(obliquity) * yelc + cosD(obliquity) * zelc;

        eq = new Coordinate(xeq, yeq, zeq);
        */
        return new Coordinate[]{orbitalPos, orbitalVel, centralPos, centralVel};
    }

    /**
     * function using cos for degrees
     * @param degree degree
     * @return cos degree
     */
    private static double cosD(double degree){
        return MathUtil.cosDegree(degree);
    }

    /**
     * function using sin degree
     * @param degree degree
     * @return sin degree
     */
    private static double sinD(double degree){
        return MathUtil.sinDegree(degree);
    }

    public static HEECoordinate OrbitalCoordinateToHEECoordinate(double m,
                                                                  double o,
                                                                  double i,
                                                                  Coordinate obC){
        HEECoordinate heeC = new HEECoordinate();
        heeC.setX((cosD(m) * cosD(o) - sinD(m) * sinD(o) * cosD(i)) * obC.getX() +
                ((-sinD(m)) * cosD(o) - cosD(m) * sinD(o) * cosD(i)) * obC.getY() +
                (sinD(i) * sinD(o) * obC.getZ()));
        heeC.setY((cosD(m) * sinD(o) + sinD(m) * cosD(o) * cosD(i)) * obC.getX() +
                ((-sinD(m)) * sinD(o) + cosD(m) * cosD(o) * cosD(i)) * obC.getY() +
                ((-cosD(o)) * sinD(i) * obC.getZ()));
        heeC.setZ((sinD(m) * sinD(i)) * obC.getX() +
                (cosD(m) * sinD(i)) * obC.getY() +
                (cosD(i) * obC.getZ()));
        return heeC;
    }
}
