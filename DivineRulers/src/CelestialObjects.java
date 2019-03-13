import java.util.HashSet;

/**
 * Celestial object
 */
public class CelestialObjects {
    private static final double AU = 149597870700.0; // AU in m

    private String name;

    private CelestialObjects orbitingParent;
    private HashSet<CelestialObjects> orbitingChildren;

    private double mass; // in kg
    private double radius; // in km
    private double eccentricity;
    private double period; // in days
    private double semiMajor; // AU
    private double distance; // AU J2000

    private long posX;
    private long posY;
    private long posZ;

    /**
     * @param mass mass of celestial object
     * @param radius radius of celestial object
     */
    public CelestialObjects(String name, double mass, double radius) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
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
     * @return eccentricity of celestial object
     */
    public double getEccentricity() {
        return eccentricity;
    }

    /**
     * @param eccentricity eccentricity of celestial object
     */
    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
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
     * semi major is the average distance to its orbiting object
     * @return semiMajor of celestial object in km
     */
    public double getSemiMajorKm() {
        return semiMajor * AU / 1000.0;
    }

    /**
     * semi major is the average distance to its orbiting object
     * @return semiMajor of celestial object in AU
     */
    public double getSemiMajor() {
        return semiMajor;
    }

    /**
     * semi major is the average distance to its orbiting object
     * @param semiMajor of celestial object
     */
    public void setSemiMajor(double semiMajor) {
        this.semiMajor = semiMajor;
    }

    /**
     * The distance of the celestial object in respect to the sun as measured on january 1st 2000
     * in km
     * @param distance distance of celestial object in km
     */
    public double getDistanceKm() {
        return distance * AU / 1000.0;
    }


    /**
     * The distance of the celestial object in respect to the sun as measured on january 1st 2000
     * in AU
     * @param distance distance of celestial object in AM
     */
    public double getDistanceAU() {
        return distance;
    }

    /**
     * @param distance to orbiting object in AU
     */
    public void setDistance(double distance) {
        this.distance = distance;
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
