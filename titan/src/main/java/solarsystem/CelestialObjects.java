package solarsystem;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import utils.Coordinate;
import utils.Date;
import utils.HEECoordinate;
import utils.MathUtil;

import java.util.HashSet;

/**
 * Celestial object
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id")
public class CelestialObjects {

    @JsonProperty("central_body")
    private CelestialObjects centralBody;
    private HashSet<CelestialObjects> orbitingChildren;

    @JsonProperty("name")
    private String name;
    @JsonProperty("mass")
    private double mass;        // in kg
    @JsonProperty("radius")
    private double radius;      // in km
    @JsonProperty("period")
    private double period;      // in days
    @JsonProperty("a")
    private double a0;          // Mean semi-major axis (AU) at J2000
    @JsonProperty("a_Cnt")
    private double aCnt;        // Change semi-major per century (AU)
    @JsonProperty("e")
    private double e0;          // Eccentricity of orbit at J2000
    @JsonProperty("e_Cnt")
    private double eCnt;        // Change of eccentricity of orbit per century
    @JsonProperty("I")
    private double i0;          // Inclination of orbit at J2000
    @JsonProperty("I_Cnt")
    private double iCnt;        // Change of inclination of orbit per century
    @JsonProperty("L")
    private double l0;          // Mean longitude at J2000
    @JsonProperty("L_Cnt")
    private double lCnt;        // Change of longitude per century
    @JsonProperty("w")
    private double w0;          // Longitude of perihelion (degrees) at J2000
    @JsonProperty("w_Cnt")
    private double wCnt;        // Change of perihelion l per century
    @JsonProperty("node")
    private double o0;          // Longitude of ascending node at J2000
    @JsonProperty("node_Cnt")
    private double oCnt;        // Change of longitude of ascending node per

    private double a; // semi major axis;
    private double e; // eccentricity
    private double i; // inclination
    private double l; // longitude
    private double w; // periphelon
    private double o; // change of ascending node
    // century

    private Date coordinateDate;   // Date of the current
    private Coordinate orbitalPos; // Coordinate in the orbital plane
    private Coordinate orbitalVel; // Velocity vector in the orbital plane
    private Coordinate centralPos; // Coordinate central body reference frame
    private Coordinate centralVel; // Velocity central body reference frame

    //@JsonProperty("locationVars")
    //private KeplerToCartesian locationVars;


    public CelestialObjects() {
    }

    /**
     * get the cartesian coordinates of a planet
     */
    private void getCartesianCoordinates(Date date) {
        if(coordinateDate != null && date.compareTo(coordinateDate) == 0){return;}//check
        // whether current values are already stored for these date.
        coordinateDate = new Date(date);

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

        double Mass = centralBody.getMass();
        //double mu = Mass * MathUtil.Gm * MathUtil.AU; // get mu AU/s^2
        double mu = Mass * MathUtil.GAU; // get mu AU/s^2

        Coordinate[] cartesian = KeplerToCartesian.getCartesianCoordinates(a, e,
                i, l, w, o, mu);

        orbitalPos = cartesian[0];
        orbitalVel = cartesian[1];
        centralPos = cartesian[2];
        centralVel = cartesian[3];
    }

    /**
     * @return name of the celestial object
     */
    public String getName() {
        return name;
    }

    /**
     * e.g., the moon orbits around the earth.
     * @return the object of which the celestial object is orbiting around
     */
    public CelestialObjects getCentralBody() {
        return centralBody;
    }

    /**
     * @param orbitingCelestialPlanet the object of which the celestial object is orbiting around
     */
    public void setCentralBody(CelestialObjects orbitingCelestialPlanet) {
        this.centralBody = orbitingCelestialPlanet;
    }

    /**
     * @return celestial objects which are orbiting around the celestial object
     */
    public HashSet<CelestialObjects> getOrbitingChildren() {
        return orbitingChildren;
    }

    /**
     * @param orbitingChildren celestial objects which are orbiting around the celestial object
     */
    public void setOrbitingChildren(HashSet<CelestialObjects> orbitingChildren) {
        this.orbitingChildren = orbitingChildren;
    }

    /**
     * @return mass of the celestial object
     */
    public double getMass() {
        return mass;
    }

    /**
     * @return radius position of celestial object
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @return the period of which the celestial object orbits around its orbiting celestial object
     */
    public double getPeriod() {
        return period;
    }

    public double getSemiMajorAxisJ2000() {
        return a0;
    }

    public Coordinate getHEEpos(Date date){
        getCartesianCoordinates(date);
        return centralPos;
    }
    public Coordinate getHEEvel(Date date){
        getCartesianCoordinates(date);
        return centralVel;
    }

    public Coordinate getRCoord(Date date){
        getCartesianCoordinates(date);
        return orbitalPos;
    }

    public Coordinate getVel(Date date){
        getCartesianCoordinates(date);
        return orbitalVel;
    }
}
