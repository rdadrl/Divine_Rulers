package solarsystem;

import com.fasterxml.jackson.annotation.JsonProperty;
import physics.ODEsolvable;
import utils.Date;
import utils.MathUtil;
import utils.vector.Vector3D;

import java.util.ArrayList;

/**
 * An interface for any celestial object which have to move through space. Any object such as a
 * planet, InterPlanetaryRocketToTitan or cannonball upon which the acceleration of the universe apply need to inherit this
 * class.
 */
public abstract class CelestialObject implements ODEsolvable {
    @JsonProperty("name")
    protected String name;
    @JsonProperty("mass")
    protected double mass;        // in kg
    @JsonProperty("radius")
    protected double radius;      // in km

    protected Date current_date;   // Date of the current
    protected Vector3D centralPos = new Vector3D(); // Coordinate with the sun as a center.
    protected Vector3D centralVel = new Vector3D(); // Velocity vector with sun in the center
    protected Vector3D acceleration;


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
        return radius * 1000;
    }

    /**
     * gets the acceleration of the celestial object in space  in space.
     * @param objectsInSpace arraylist of all the objects that apply a force to
     * @return acceleration
     */
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date){
        this.current_date = date;
        Vector3D forces = gravitationalForces(this, objectsInSpace);
        acceleration = forces.scale(1D/mass);
    }

    /**
     * @return acceleration
     */
    public Vector3D getAcceleration(){
        return acceleration;
    }

    /**
     * @return get coordinates based upon HEE coordinates
     */
    public Vector3D getCentralPos(){
        return centralPos;
    }

    /**
     * set position coordinates based upon HEE coordinate system
     * @param centralPos position vector
     */
    public void setCentralPos(Vector3D centralPos){
        this.centralPos = centralPos;
    }

    /**
     * @return velocity coordinate (HEE coordinate system)
     */
    public Vector3D getCentralVel(){
        return centralVel;
    }

    /**
     * @param centralVel set velocity coordinate (HEE coordinate system)
     */
    public void setCentralVel(Vector3D centralVel){
        this.centralVel = centralVel;
    }

    /**
     * @return current_date associated with the variable
     */
    public Date getDate() {
        return current_date;
    }

    public abstract void initializeCartesianCoordinates(Date date);

    /**
     *
     * Calculate the gravitational acceleration upon an object
     * @param refOb object upon which the gravitation acceleration are calculated
     * @param objectsInSpace all the objects that creates the gravitational acceleration
     * @return a vector of the gravitational force
     */
    public static Vector3D gravitationalForces(CelestialObject refOb, ArrayList<?
            extends CelestialObject> objectsInSpace) {
        Vector3D gForces = new Vector3D(); // reset the acceleration
        // itterate over all the objects in space
        for (CelestialObject o : objectsInSpace) {
            //skip the ref object itself and for now also cannonballs and rockets
            if (o == refOb) {
                continue;
            }
            // get the relative position to the reference object
            Vector3D r = o.getCentralPos().substract(refOb.getCentralPos());
            // radius of distance
            double dist = r.length();
            // F(m1<-m2) = (G * m1 * m2) / r^2

            double netForce_c = (MathUtil.G * o.getMass() * refOb.getMass());
            double netForce = (MathUtil.G * o.getMass() * refOb.getMass()) /
                    Math.pow(dist, 2);
            // create the force vector of between the two objects. It is in the direction of
            // the relative position of the object and the magnitude of the net force
            Vector3D thisForce = r.unit().scale(netForce);
            // add force to total acceleration
            gForces = gForces.add(thisForce);
        }
        return gForces;
    }



}
