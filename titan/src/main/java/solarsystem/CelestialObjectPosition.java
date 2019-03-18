package solarsystem;

import utils.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;


/**
 *
 *
 */
public class CelestialObjectPosition {
    private static final double AU = 149597870700.0; // AU in m
    private static final double G = 1.993E-44; // AU/kg*s^2
    @JsonProperty("co")
    CelestialObjects celestialObject;

    @JsonProperty("a")
    private double a0;           // Mean semi-major axis (AU)
    @JsonProperty("a_Cnt")
    private double aCnt;             // Change semi-major per century (AU)
    @JsonProperty("e")
    private double e0;       // Eccentricity of orbit
    @JsonProperty("e_Cnt")
    private double eCnt;         // Change of eccentricity of orbit per century
    @JsonProperty("I")
    private double i0;        // Inclination of orbit
    @JsonProperty("I_Cnt")
    private double iCnt;          // Change of inclination of orbit per century
    @JsonProperty("L")
    private double l0;      // Mean longitude
    @JsonProperty("L_Cnt")
    private double lCnt;        // Change of longitude per century
    @JsonProperty("w")
    private double w0;         // Longitude of perihelion (degrees)
    @JsonProperty("w_Cnt")
    private double wCnt;           // Change of perihelion l per century
    @JsonProperty("node")
    private double o0;      // Longitude of ascending node
    @JsonProperty("node_Cnt")
    private double oCnt;        // Change of longitude of ascending node per
    // century

    private double a;
    private double e;
    private double i;
    private double l;
    private double w;
    private double o;

    private Date savedDate;
    private Coordinate roPos;
    private Coordinate roVel;
    private HEECoordinate relcPos;
    private HEECoordinate relcVel;
    private Coordinate eq;


    /**
     * source https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
     * https://downloads.rene-schwarz.com/download/M001-Keplerian_Orbit_Elements_to_Cartesian_State_Vectors.pdf
     * @param date date for which the solar system need to be configured
     */
    public void initializeCartesianStateVectors(Date date){
        if(savedDate != null && date.compareTo(savedDate) == 0){return;}//check
        // whether current values are already stored for these date.
        savedDate = new Date(date);

        // step 1
        // compute the value of each of that planet's six elements
        double JT = date.dateToJulian(); // in centuries
        double T = (JT - 2451545.0)/36525.0;

        a = a0 + aCnt * T;
        e = e0 + eCnt * T;
        i = i0 + iCnt * T;
        l = l0 + lCnt * T;
        w = w0 + wCnt * T;
        o = o0 + oCnt * T;

        // Step 2
        // compute the argument of perihelion, m, and the mean anomaly, M
        double m = w - o;
        double M = l - w;

        // Step 3
        // modulus the mean anomaly so that -180° ≤ M ≤ +180°
        M = (M % 180.0);

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

        double radius = a * (1 - e * cosD(E));

        roPos = new Coordinate();
        roPos.setX(a * (cosD(E) - e));
        roPos.setY(a * Math.sqrt(1.0 - e * e) * sinD(E));
        roPos.setZ(0.0);

        roVel = new Coordinate();
        roVel.setX(-sinD(E));
        roVel.setY(Math.sqrt(1.0 - e * e) * cosD(E));
        roVel.setZ(0.0);

        double Mass = celestialObject.getCentralBody().getMass();
        double mu = Mass * G;
        roVel = roVel.scale(Math.sqrt(mu * a)/radius);


        // Step 5
        // compute the coordinates in the J2000 ecliptic plane, with the x-axis
        // aligned toward the equinox:
        relcPos = OrbitalCoordinateToHEECoordinate(roPos, m);
        relcVel = OrbitalCoordinateToHEECoordinate(roVel, m);
        relcVel = relcVel.scale(60*60*24);

        /*
        // Step 6
        double obliquity = 34.43928;

        double xeq = xelc;
        double yeq = cosD(obliquity) * yelc - sinD(obliquity) * zelc;
        double zeq = sinD(obliquity) * yelc + cosD(obliquity) * zelc;

        eq = new Coordinate(xeq, yeq, zeq);
        */
    }

    /**
     * function using cos for degrees
     * @param degree degree
     * @return cos degree
     */
    private double cosD(double degree){
        return MathUtil.cosDegree(degree);
    }

    /**
     * function using sin degree
     * @param degree degree
     * @return sin degree
     */
    private double sinD(double degree){
        return MathUtil.sinDegree(degree);
    }

    private HEECoordinate OrbitalCoordinateToHEECoordinate(Coordinate obC,
                                                           double m){
        HEECoordinate heeC = new HEECoordinate();
        heeC.setX((cosD(m) * cosD(o) - sinD(m) * sinD(o) * cosD(i)) * obC.getX() +
                ((-sinD(m)) * cosD(o) - cosD(m) * sinD(o) * cosD(i)) * obC.getY());
        heeC.setY((cosD(m) * sinD(o) + sinD(m) * cosD(o) * cosD(i)) * obC.getX() +
                ((-sinD(m)) * sinD(o) + cosD(m) * cosD(o) * cosD(i)) * obC.getY());
        double temp1 = (sinD(m) * sinD(i)) * obC.getX();
        double temp2 = (cosD(m) * sinD(i)) * obC.getY();

        heeC.setZ((sinD(m) * sinD(i)) * obC.getX() +
                (cosD(m) * sinD(i)) * obC.getY());
        return heeC;
    }


    public HEECoordinate getRelcPos() {
        return relcPos;
    }
    public HEECoordinate getRelcVel() {
        return relcVel;
    }

    public Coordinate getRoPos() {
        return roPos;
    }

    public Coordinate getRoVel() {
        return roVel;
    }



    @Override
    public String toString() {
        return "CelestialObjectPosition{" +
                "a=" + a +
                ", aCnt=" + aCnt +
                ", e=" + e +
                ", eCnt=" + eCnt +
                ", i=" + i +
                ", iCnt=" + iCnt +
                ", l=" + l +
                ", lCnt=" + lCnt +
                ", w=" + w +
                ", wCnt=" + wCnt +
                ", o=" + o +
                ", oCnt=" + oCnt +
                '}';
    }
}
