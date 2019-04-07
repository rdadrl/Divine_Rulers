package solarsystem;

import com.fasterxml.jackson.annotation.JsonProperty;
import utils.Date;
import utils.MathUtil;
import utils.Vector3D;

import java.util.ArrayList;

/**
 * An interface for any celestial object which have to move through space. Any object such as a
 * planet, rocket or cannonball upon which the forces of the universe apply need to inherit this
 * class.
 */
public abstract class CelestialObject {
    @JsonProperty("name")
    String name;
    @JsonProperty("mass")
    double mass;        // in kg
    @JsonProperty("radius")
    double radius;      // in km

    Date date;   // Date of the current
    Vector3D centralPos = new Vector3D(); // Coordinate with the sun as a center.
    Vector3D centralVel = new Vector3D(); // Velocity vector with sun in the center

    Vector3D forces;


    /**
     * @return name of the celestial object
     */
    public String getName() {
        return name;
    }

    /**
     * @return mass of the celestial object
     */
    public double getMass() {
        return mass;
    }

    /**
     * @return radius of celestial object
     */
    public double getRadius() {
        return radius;
    }

    /**
     * gets the gravetational force that are applied on the planet  in space.
     * @param objectsInSpace arraylist of all the objects that apply a force to
     * @return forces
     */
    public void setForces(ArrayList<? extends CelestialObject> objectsInSpace){
        forces = MathUtil.gravitationalForces(this, objectsInSpace);
    }

    /**
     * @return forces
     */
    public Vector3D getForces(){
        return forces;
    }

    /**
     * @return get coordinates based upon HEE coordinates
     */
    public Vector3D getCentralPos(){
        return centralPos;
    }

    public void setCentralPos(Vector3D centralPos, Date date){
        this.date = new Date(date);
        this.centralPos = centralPos;
    }

    public Vector3D getCentralVel(){
        return centralVel;
    }

    public void setCentralVel(Vector3D centralVel, Date date){
        this.date = new Date(date);
        this.centralVel = centralVel;
    }


    /**
     * @return date associated with the variable
     */
    public Date getDate() {
        return date;
    }

    public abstract void initializeCartesianCoordinates(Date date);



}
