package solarsystem;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import physics.KeplerToCartesian;
import utils.*;
import utils.vector.Vector3D;

import java.util.HashSet;

/**
 * A planet or moon object
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Planet extends CelestialObject {
    @JsonProperty("central_body")
    private Planet centralBody;
    @JsonProperty("orbiting_children")
    private HashSet<Planet> orbitingChildren;

    @JsonProperty("period")
    private double period;      // in days
    @JsonProperty("orbital_properties")
    private PlanetOrbitalProperties planetOrbitalProperties;

    private double sphereOfInfluence = -1; // initialize to -1 to be able to check for bugs
    private Vector3D orbitalPos = new Vector3D(); // Coordinate in the orbital plane AU
    private Vector3D orbitalVel = new Vector3D(); // Velocity vector in the orbital plane AU/day


    /**
     * get the cartesian coordinates of a planet
     * part of this is based upon
     */
    public void initializeCartesianCoordinates(Date date) {
        if(centralBody == null){return;}

        //check whether current values are already stored for these current_date.
        if(this.current_date != null && date.compareTo(this.current_date) == 0){return;}
        else{this.current_date = new Date(date);}

        Vector3D[] cartesian = KeplerToCartesian.getCartesianCoordinates(this, date);
        orbitalPos = cartesian[0]; // position in orbital plane
        orbitalVel = cartesian[1]; // velocity in orbital plane
        centralPos = cartesian[2];
        centralVel = cartesian[3];
    }

    /**
     * e.g., the moon orbits around the earth.
     * @return the object of which the celestial object is orbiting around
     */
    public Planet getCentralBody() {
        return centralBody;
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
    public PlanetOrbitalProperties getPlanetOrbitalProperties() {
        return planetOrbitalProperties;
    }

    /**
     * @return semi-major axis at J2000
     */
    public double getSemiMajorAxisJ2000() {
        return planetOrbitalProperties.getSemiMajorAxisJ2000();
    }

    public Vector3D getCentralPosAtDate(Date date){
        if(super.getDate() == null || !super.getDate().equals(date)){
            initializeCartesianCoordinates(date);
        }
        return centralPos;
    }

    public Vector3D getCentralVelAtDate(Date date){
        if(super.getDate() == null || !super.getDate().equals(date)){
            initializeCartesianCoordinates(date);
        }
        return centralVel;
    }

    public Vector3D getOrbitalPosAtDate(Date date){
        if(super.getDate() == null || !super.getDate().equals(date)){
            initializeCartesianCoordinates(date);
        }
        return orbitalPos;
    }

    public Vector3D OrbitalVel(){
        return orbitalVel;
    }

    /**
     * @param date current_date
     * @return departureVelocity vector
     */
    public Vector3D getOrbitalVelAtDate(Date date){
        if(!super.getDate().equals(date)){
            initializeCartesianCoordinates(date);
        }
        return orbitalVel;
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

    /**
     * Calculate Lagrange sphere of influence: 𝑅(𝑚𝑀)^(2/5)
     * https://space.stackexchange.com/questions/3015/how-large-is-the-earths-gravitational-sphere-of-influence-and-how-can-it-be-cal
     * @return sphere of influence
     */
    public double getSphereOfInfluence(){
        // Sphere of influence not defined yet. 
        if(sphereOfInfluence == -1) {
            double a = planetOrbitalProperties.getSemiMajorAxisJ2000() * MathUtil.AU;
            double M = centralBody.getMass();
            double m = mass;
            sphereOfInfluence = a * Math.pow(m/M, (2.0/5.0));
        }
        return sphereOfInfluence;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "name=" + getName() +
                ", centralPos=" + centralPos +
                ", centralVel=" + centralVel +
                '}';
    }
}
