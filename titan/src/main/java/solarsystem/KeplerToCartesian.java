package solarsystem;

import utils.*;


/**
 *
 *
 */
public class KeplerToCartesian {



    /**
     * source https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
     * https://downloads.rene-schwarz.com/download/M001-Keplerian_Orbit_Elements_to_Cartesian_State_Vectors.pdf
     * @param a  semi-major axis
     * @param e eccentricity
     * @param i inclination
     * @param l longitude
     * @param w periphelon
     * @param o ascending node
     * @param mu gravetational force of central body
     * @return arraylist with coordinate vectors
     */
    public static Vector3D[] getCartesianCoordinates(
            double a, double e, double i, double l, double w, double o,
            double mu) {
        // Step 2
        // compute the argument of perihelion, m, and the mean anomaly, M
        double m = w - o;
        double M = l - w;

        return calculateKepler(a, e, m, o, i, M, mu);
    }

    public static Vector3D[] calculateKepler(double a, double e, double m, double o,
                                double i, double M, double mu){

        // Step 3
        // solve keplers equation
        M = M % 360; // bring to degree within the circle;
        if (M<0) M = M + 360D;
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


        /* true anomanaly calclulation if necessary
        double vt = 2 * Math.atan2(
                Math.sqrt(1.0 + e) * sinD(E / 2D),
                Math.sqrt(1.0 - e) * cosD(E / 2D));
        vt = Math.toDegrees(vt);
        */


        // Step 4
        // compute the planet's heliocentric coordinates in its orbital
        // plane, roPos', with the x'-axis aligned from the focus to the perihelion
        Vector3D orbitalPos; // Vector3D in the orbital plane
        Vector3D orbitalVel; // Velocity vector in the orbital plane
        Vector3D centralPos; // Vector3D central body reference frame
        Vector3D centralVel; // Velocity central body reference frame

        double radius = a * (1 - e * cosD(E));

        orbitalPos = new Vector3D();
        orbitalPos.setX(a * (cosD(E) - e));
        orbitalPos.setY(a * Math.sqrt(1.0 - e * e) * sinD(E));
        orbitalPos.setZ(0.0);

        orbitalVel = new Vector3D();
        orbitalVel.setX(-sinD(E));
        orbitalVel.setY(Math.sqrt(1.0 - e * e) * cosD(E));
        orbitalVel.setZ(0.0);


        orbitalVel = orbitalVel.scale(Math.sqrt(mu * a)/radius);


        // Step 5
        // compute the coordinates in the J2000 ecliptic plane, with the x-axis
        // aligned toward the equinox:
        centralPos = rotatePlane(m, o, i, orbitalPos);
        centralVel = rotatePlane(m, o, i, orbitalVel);
        centralVel = centralVel.scale(60*60*24);

        /*
        // Step 6
        double obliquity = 34.43928;

        double xeq = xelc;
        double yeq = cosD(obliquity) * yelc - sinD(obliquity) * zelc;
        double zeq = sinD(obliquity) * yelc + cosD(obliquity) * zelc;

        eq = new Vector3D(xeq, yeq, zeq);
        */
        return new Vector3D[]{orbitalPos, orbitalVel, centralPos, centralVel};
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

    public static Vector3D rotatePlane(double m, double o, double i,
                                       Vector3D obC){
        Vector3D heeC = new Vector3D();
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
