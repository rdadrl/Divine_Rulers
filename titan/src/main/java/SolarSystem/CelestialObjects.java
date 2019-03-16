package SolarSystem;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashSet;

/**
 * Celestial object
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id")
public class CelestialObjects {

    @JsonProperty("orbits_around")
    private CelestialObjects orbitingParent;
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
    private LocationFinder locationVars;



    private long posX;
    private long posY;
    private long posZ;

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
    public CelestialObjects getOrbitingParent() {
        return orbitingParent;
    }

    /**
     * @param orbitingCelestialPlanet the object of which the celestial object is orbiting around
     */
    public void setOrbitingParent(CelestialObjects orbitingCelestialPlanet) {
        this.orbitingParent = orbitingCelestialPlanet;
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
    public LocationFinder getLocationVars() {
        return locationVars;
    }

    /**
     * @param locationVars location parameters
     */
    public void setLocationVars(LocationFinder locationVars) {
        this.locationVars = locationVars;
    }

    /**
     * @return x position of celestial object
     */
    public long getPosX() {
        return posX;
    }

    /**
     * @param posX x position of celestial object
     */
    public void setPosX(long posX) {
        this.posX = posX;
    }

    /**
     * @return y position of celestial object
     */
    public long getPosY() {
        return posY;
    }

    /**
     * @param posY y position of celestial object
     */
    public void setPosY(long posY) {
        this.posY = posY;
    }

    /**
     * @return z position of celestial object
     */
    public long getPosZ() {
        return posZ;
    }

    /**
     * @param posZ z position of celestial object
     */
    public void setPosZ(long posZ) {
        this.posZ = posZ;
    }
}
