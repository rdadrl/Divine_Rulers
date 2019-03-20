package solarsystem;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import utils.*;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Celestial object
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id")
public class CelestialObjects implements ObjectInSpace{

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
    @JsonProperty("MA_J2000")
    private double MAJ2000;
    @JsonProperty("peri")
    private double m0; // argument of periapsis

    private double a; // semi major axis;
    private double e; // eccentricity
    private double i; // inclination
    private double l; // longitude
    private double w; // periphelon
    private double o; // change of ascending node
    // century

    private Date dateStart;   // Date of the current
    private Date currentDate;
    private Vector3D orbitalPos; // Coordinate in the orbital plane AU
    private Vector3D orbitalVel; // Velocity vector in the orbital plane AU/day

    private Vector3D HEEpos = new Vector3D(); // Coordinate central body reference
    // frame AU
    private Vector3D HEEvel = new Vector3D(); // Velocity central body reference frame
    // AU/day

    private Vector3D forces;

    //@JsonProperty("locationVars")
    //private KeplerToCartesian locationVars;


    public CelestialObjects() {
    }

    /**
     * get the cartesian coordinates of a planet
     */
    public void initializeCartesianCoordinates(Date date) {
        if(centralBody == null) return;
        if(dateStart != null && date.compareTo(dateStart) == 0){return;}//check
        // whether current values are already stored for these date.
        dateStart = new Date(date);

        // step 1
        // compute the value of each of that planet's six elements
        double JT = date.dateToJulian(); // in centuries
        double T = (JT - Date.J2000)/36525.0;

        double Mass = centralBody.getMass();
        double mu = Mass * MathUtil.GAU; // get mu AU/s^2

        Vector3D[] cartesian;
        // as we don't have all the exact details for Titan we need to
        // approximate it.
        if(name.equals("Titan")){
            double dt = 86400 * (JT - Date.J2000); // difference in seconds
            double M = MAJ2000 + dt * Math.sqrt((mu)/(Math.pow(a0,3)));
            cartesian = KeplerToCartesian.calculateKepler(a0, e0, m0, o0, i0, M,
                    mu);
            double mc = centralBody.w - centralBody.o;

            HEEpos = KeplerToCartesian.rotatePlane(mc, centralBody.o,
                    centralBody.i, cartesian[2]);
            HEEvel = KeplerToCartesian.rotatePlane(mc, centralBody.o,
                    centralBody.i, cartesian[3]);
        }else{
            a = a0 + aCnt * T;
            e = e0 + eCnt * T;
            i = i0 + iCnt * T;
            l = l0 + lCnt * T;
            w = w0 + wCnt * T;
            o = o0 + oCnt * T;
            //double mu = Mass * MathUtil.GAU; // get mu AU/s^2

            cartesian = KeplerToCartesian.getCartesianCoordinates(a, e,
                    i, l, w, o, mu);

            HEEpos = cartesian[2];
            HEEvel = cartesian[3];
        }
        orbitalPos = cartesian[0];
        orbitalVel = cartesian[1];
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

    public Vector3D getHEEpos(Date date){
        initializeCartesianCoordinates(date);
        return HEEpos;
    }

    public Vector3D getHEEpos(){
        return HEEpos;
    }

    public void setHEEpos(Vector3D pos){
        HEEpos = pos;
    }

    public Vector3D getHEEvel(Date date){
        initializeCartesianCoordinates(date);
        return HEEvel;
    }
    public Vector3D getHEEvel(){
        return HEEvel;
    }

    public void setHEEvel(Vector3D centralVel){
        this.HEEvel = centralVel;
    }


    public Vector3D getRCoord(Date date){
        initializeCartesianCoordinates(date);
        return orbitalPos;
    }

    public Vector3D getVel(Date date){
        initializeCartesianCoordinates(date);
        return orbitalVel;
    }

    /**
     * gets the gravetational force that are applied on the planet  in space.
     * @param objectsInSpace arraylist of all the objects that apply a force to
     * @return forces
     */
    public void setForces(ArrayList<? extends ObjectInSpace> objectsInSpace){
        forces = MathUtil.gravitationalForces(this, objectsInSpace);
    }

    public Vector3D getForces(){
        return forces;
    }

    @Override
    public String toString() {
        return "CelestialObjects{" +
                "name=" + name +
                ", HEEpos=" + HEEpos +
                ", HEEvel=" + HEEvel +
                '}';
    }
}
