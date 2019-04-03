package solarsystem;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import physics.KeplerToCartesian;
import utils.MathUtil;
import utils.*;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A planet or moon object
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Planet implements CelestialObject {
    @JsonProperty("central_body")
    private Planet centralBody;
    private HashSet<Planet> orbitingChildren;

    @JsonProperty("name")
    private String name;
    @JsonProperty("mass")
    private double mass;        // in kg
    @JsonProperty("radius")
    private double radius;      // in km
    @JsonProperty("period")
    private double period;      // in days
    @JsonProperty("orbital_properties")
    private OrbitalProperties orbitalProperties;
    /*
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
    private double MA0;         // Mean anomaly at J2000
    @JsonProperty("peri")
    private double w_peri; // argument of periapsis

    private double a; // semi major axis;
    private double e; // eccentricity
    private double i; // inclination
    private double l; // longitude
    private double w; // periphelon
    private double o; // change of ascending node
    */
    // century

    private Date date;   // Date of the current
    private Vector3D orbitalPos; // Coordinate in the orbital plane AU
    private Vector3D orbitalVel; // Velocity vector in the orbital plane AU/day
    private Vector3D centralPos; // Coordinate with the sun as a center.
    private Vector3D centralVel; // Velocity vector with sun in the center

    private Vector3D forces;


    /**
     * get the cartesian coordinates of a planet
     * part of this is based upon
     */
    public void initializeCartesianCoordinates(Date date) {
        if(centralBody == null) return;
        //check whether current values are already stored for these date.
        if(this.date != null && date.compareTo(this.date) == 0){return;}
        else{this.date = new Date(date);}

        Vector3D[] cartesian = KeplerToCartesian.getCartesianCoordinates(this, date);
        orbitalPos = cartesian[0];
        orbitalVel = cartesian[1];
        centralPos = cartesian[2];
        centralVel = cartesian[3];

       /*
        if(name.equals("titan")){
            double Mass = (centralBody.getMass() );
            double mu = Mass * MathUtil.G; // get mu m/s^2
            this.centralBody.initializeCartesianCoordinates(date);
            double dt = 86400 * (JT - Date.J2000); // difference in seconds
            double M = MA0 + dt * Math.sqrt((mu)/(Math.pow(a0,3)));
            cartesian = KeplerToCartesian.calculateKepler(a0 * MathUtil.AU, e0, w_peri, o0, i0, M,
                    mu);
        }else{

        double Mass = centralBody.getMass();
        double mu = Mass * MathUtil.G; // get mu AU^3/s^2


            a = (a0 + aCnt * T) * MathUtil.AU; // convert to meters
            e = e0 + eCnt * T;
            i = i0 + iCnt * T;
            l = l0 + lCnt * T;
            w = w0 + wCnt * T;
            o = o0 + oCnt * T;

            w_peri = w - o;
            //double mu = Mass * MathUtil.GAU; // get mu AU/s^2

            cartesian = KeplerToCartesian.getCartesianCoordinates(a, e,
                    i, l, w, o, mu);
        }


        if(name.equals("Titan")){
            centralBody.initializeCartesianCoordinates(date);
            double omC = centralBody.w - centralBody.o;

            //Look from here on!!
            Vector3D SatPlanePos = cartesian[2];
            Vector3D SatPlaneVel = cartesian[3];

            centralPos = KeplerToCartesian.orbitalToEclipticPlane(omC, centralBody.o,
                    centralBody.i, SatPlanePos);
            centralPos = centralPos.add(centralBody.getcentralPos(date));
            centralVel = KeplerToCartesian.orbitalToEclipticPlane(omC, centralBody.o,centralBody.i, SatPlaneVel);
            centralVel = centralVel.add(centralBody.getCentralVel());
        }else{
            centralPos = cartesian[2];
            centralVel = cartesian[3];
        }

        orbitalPos = cartesian[0];
        orbitalVel = cartesian[1];
        */
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
    public Planet getCentralBody() {
        return centralBody;
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

    /**
     * @return orbital properties of planet
     */
    public OrbitalProperties getOrbitalProperties() {
        return orbitalProperties;
    }

    /**
     * @return semi-major axis at J2000
     */
    public double getSemiMajorAxisJ2000() {
        return orbitalProperties.getSemiMajorAxisJ2000();
    }

    public Vector3D getcentralPos(Date date){
        initializeCartesianCoordinates(date);
        return centralPos;
    }

    /**
     * @return get coordinates based upon HEE coordinates
     */
    public Vector3D getCentralPos(){
        return centralPos;
    }



    public Vector3D getcentralVel(Date date){
        initializeCartesianCoordinates(date);
        return centralVel;
    }
    public Vector3D getCentralVel(){
        return centralVel;
    }

    public void setCentralPos(Vector3D pos, Date date){
        this.date = new Date(date);
        centralPos = pos;
    }

    public void setCentralVel(Vector3D centralVel, Date date){
        this.date = new Date(date);
        this.centralVel = centralVel;
    }

    public Vector3D initializeOrbitalPos(Date date){
        initializeCartesianCoordinates(date);
        return orbitalPos;
    }

    public Vector3D initializeOrbitalVel(Date date){
        initializeCartesianCoordinates(date);
        return orbitalVel;
    }

    public Vector3D initializeOrbitalVel(){
        return orbitalVel;
    }

    /**
     * @param date date
     * @return velocity vector
     */
    public Vector3D getVel(Date date){
        initializeCartesianCoordinates(date);
        return orbitalVel;
    }

    /**
     * gets the gravetational force that are applied on the planet  in space.
     * @param objectsInSpace arraylist of all the objects that apply a force to
     * @return forces
     */
    @Override
    public void setForces(ArrayList<? extends CelestialObject> objectsInSpace){
        forces = MathUtil.gravitationalForces(this, objectsInSpace);
    }

    /**
     * @return forces
     */
    @Override
    public Vector3D getForces(){
        return forces;
    }

    /**
     * Get the planet around which the whole solarsystem is based upon. E.g
     * ., the sun in our case
     * @return the sun
     */
    public Planet getReferencePlanet(){
        Planet ref = centralBody;
        while(ref.centralBody != null){
            ref = ref.centralBody;
        }
        return centralBody;
    }
    /*
    public double getPeriapsis() {
        return w_peri;
    }
    public double getInclination(){
        return i;
    }
    public double getAscendingNode(){
        return o;
    }
    */


    @Override
    public String toString() {
        return "Planet{" +
                "name=" + name +
                ", centralPos=" + centralPos +
                ", centralVel=" + centralVel +
                '}';
    }
}
