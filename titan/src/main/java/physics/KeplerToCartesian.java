package physics;

import solarsystem.OrbitalProperties;
import solarsystem.Planet;
import utils.*;


/**
 *
 *
 */
public class KeplerToCartesian {
    /**
     * calculate the mean kepler coordinates when you have the following arguments
     * source https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
     * https://downloads.rene-schwarz.com/download/M001-Keplerian_Orbit_Elements_to_Cartesian_State_Vectors.pdf
     *
     * @param planet planet for cartesian coordinates
     * @param date date for cartesian coordinates
     * @return cartesian coordinates.
     */
    public static Vector3D[] getCartesianCoordinates(Planet planet,
                                                     Date date) {
        OrbitalProperties orbitalProperties = planet.getOrbitalProperties();

        // get the necessary orbital properties
        double a = orbitalProperties.getSemiMajorAxis(date);
        double e = orbitalProperties.getEccentricity(date);
        double i = orbitalProperties.getInclination(date);
        double l = orbitalProperties.getLongitude(date);
        double w = orbitalProperties.getPeriphelon(date);
        double o = orbitalProperties.getAscendingNode(date);
        double mu = planet.getCentralBody().getMass() * MathUtil.G;

        // Step 2
        // compute the argument of perihelion, w_a, and the mean anomaly, M
        double w_a = orbitalProperties.getPeriphelonArgument(date);
        double M = l - w;

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
        centralPos = orbitalToEclipticPlane(w_a, o, i, orbitalPos);
        centralVel = orbitalToEclipticPlane(w_a, o, i, orbitalVel);
        //centralVel = centralVel.scale(60*60*24);

        // if the planet object is a moon such as titan. We have to calculate the central
        // position and velocity in regard to the sun and not it's central body.
        if(!planet.getCentralBody().getName().equals("Sun")) {
            Planet centralBody = planet.getCentralBody();
            centralBody.initializeCartesianCoordinates(date);
            OrbitalProperties c_orbitalProperties = centralBody.getOrbitalProperties();

            // get orbital properties of the central body
            double c_i = c_orbitalProperties.getInclination(date);
            double c_w = c_orbitalProperties.getPeriphelon(date);
            double c_o = c_orbitalProperties.getAscendingNode(date);
            double c_wa = c_w - c_o;

            // revert properties to correct central positions with the sun as the center
            centralPos = KeplerToCartesian.orbitalToEclipticPlane(c_wa, c_o, c_i, centralPos);
            centralPos = centralPos.add(centralBody.getcentralPos(date));
            centralVel = KeplerToCartesian.orbitalToEclipticPlane(c_wa, c_o, c_i, centralVel);
            centralVel = centralVel.add(centralBody.getCentralVel());
        }

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


    /**
     * switch vector from an orbital to an ecliptic plane
     * @param w_a argument of the periphelon
     * @param o ascending node
     * @param i inclination
     * @param obC vector (position or velocity)  in the orbital plane
     * @return central (position or velocity) vector
     */
    public static Vector3D orbitalToEclipticPlane(double w_a, double o, double i,
                                       Vector3D obC){
        Vector3D centralCV = new Vector3D();
        // We have to perform several vector relations to get the central velocity and coordinates.
        centralCV.setX((cosD(w_a) * cosD(o) - sinD(w_a) * sinD(o) * cosD(i)) * obC.getX() +
                ((-sinD(w_a)) * cosD(o) - cosD(w_a) * sinD(o) * cosD(i)) * obC.getY() +
                (sinD(i) * sinD(o) * obC.getZ()));
        centralCV.setY((cosD(w_a) * sinD(o) + sinD(w_a) * cosD(o) * cosD(i)) * obC.getX() +
                ((-sinD(w_a)) * sinD(o) + cosD(w_a) * cosD(o) * cosD(i)) * obC.getY() +
                ((-cosD(o)) * sinD(i) * obC.getZ()));
        centralCV.setZ((sinD(w_a) * sinD(i)) * obC.getX() +
                (cosD(w_a) * sinD(i)) * obC.getY() +
                (cosD(i) * obC.getZ()));
        return centralCV;
    }



}
