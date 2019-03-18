package solarsystem;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import utils.Coordinate;
import utils.Date;
import utils.HEECoordinate;

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
    private double mass; // in kg
    @JsonProperty("radius")
    private double radius; // in km
    @JsonProperty("period")
    private double period; // in days
    @JsonProperty("locationVars")
    private CelestialObjectPosition locationVars;

    public CelestialObjects() {
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

    /**
     * @param period the period of which the celestial object orbits around its orbiting
     *               celestial object
     */
    public void setPeriod(double period) {
        this.period = period;
    }

    /**
     * @return location parameters
     */
    public CelestialObjectPosition getLocationVars() {
        return locationVars;
    }

    /**
     * @param locationVars location parameters
     */
    public void setLocationVars(CelestialObjectPosition locationVars) {
        this.locationVars = locationVars;
    }

    public HEECoordinate getHEEpos(Date date){
        getLocationVars().initializeCartesianStateVectors(date);
        return getLocationVars().getRelcPos();
    }
    public HEECoordinate getHEEvel(Date date){
        getLocationVars().initializeCartesianStateVectors(date);
        return getLocationVars().getRelcVel();
    }

    public Coordinate getRCoord(Date date){
        getLocationVars().initializeCartesianStateVectors(date);
        return getLocationVars().getRoPos();
    }
}
