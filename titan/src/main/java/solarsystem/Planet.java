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
    @JsonProperty("orbiting_children")
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

    private Date date;   // Date of the current
    private Vector3D orbitalPos = new Vector3D(); // Coordinate in the orbital plane AU
    private Vector3D orbitalVel = new Vector3D(); // Velocity vector in the orbital plane AU/day
    private Vector3D centralPos = new Vector3D(); // Coordinate with the sun as a center.
    private Vector3D centralVel = new Vector3D(); // Velocity vector with sun in the center

    private Vector3D forces;


    /**
     * get the cartesian coordinates of a planet
     * part of this is based upon
     */
    public void initializeCartesianCoordinates(Date date) {
        if(centralBody == null){return;}

        //check whether current values are already stored for these date.
        if(this.date != null && date.compareTo(this.date) == 0){return;}
        else{this.date = new Date(date);}

        Vector3D[] cartesian = KeplerToCartesian.getCartesianCoordinates(this, date);
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
