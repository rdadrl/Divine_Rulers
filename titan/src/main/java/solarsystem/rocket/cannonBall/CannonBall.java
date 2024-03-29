package solarsystem.rocket.cannonBall;

import physics.KeplerToCartesian;
import solarsystem.Planet;
import solarsystem.PlanetOrbitalProperties;
import solarsystem.rocket.Projectile;
import utils.Date;
import utils.vector.Vector3D;

/**
 * A cannonball class which extends the projectile class which in turn extends the celestial
 * object class. ! A lot of the important methods are in the projectile class so make sure to
 * check it out.
 */
public class CannonBall extends Projectile {
    private static int counter = 1;


    /**
     * @param mass mass of the cannon ball
     * @param fromPlanet
     */
    public CannonBall(double mass, double radius, Planet fromPlanet,
                      Planet toPlanet, Date date, double inclination, double velocity){
        this.name = "Cannonball " + counter++;
        this.mass = mass;
        this.radius = radius;
        this.current_date = date;

        this.toPlanet = toPlanet;
        this.fromPlanet = fromPlanet;

        this.departureInclination = Math.toRadians(inclination);
        this.departureVelocity = velocity * 1000;
    }

    public CannonBall(double mass, double radius, Planet fromPlanet,
                      Planet toPlanet, Date date, Vector3D velocity){
        this(mass, radius, fromPlanet, toPlanet, date, 0, 0);
        this.startVelVec = velocity;
    }

    /**
     * initialize the cartesian coordinates at a specific current_date
     * @param date current_date for the coordinates to be initialized
     */
    @Override
    public void initializeCartesianCoordinates(Date date) {
        // Make our cannon leave from the outside of the planet.
        centralPos = fromPlanet.getCentralPosAtDate(date);
        // add the radius vector and put it outside the sphere of influence
        Vector3D addRadX = fromPlanet.getCentralPosAtDate(date).unit().scale((fromPlanet.getSphereOfInfluence()));
        centralPos = centralPos.add(addRadX);
        // centralPos = centralPos.add(addRadY);
        // if we initialized the departureVelocity as an array it won't be based upon the inlcination and
        // departureVelocity property.
        if (startVelVec == null) {
            getStartVelocityVector(date);
        }
        centralVel = startVelVec;
    }

    /**
     * initialize a starting departureVelocity if this hasn't been passed in the constructor. The departureVelocity
     * is dependent on the starting departureInclination and starting departureVelocity
     * @param date Date for which the starting departureVelocity needs to be initialized
     */
    private void getStartVelocityVector(Date date){
        // We will get the departureVelocity vector from the earth on the orbital plane
        // as a starting position. We then rotate it anticlockwise by the departureInclination, take the
        // unit vector from it and them scale it with the departureVelocity vector
        Vector3D VelOrbitalFromPlan = fromPlanet.getOrbitalVelAtDate(date).rotateAntiClockWise(departureInclination).unit().scale(departureVelocity);

        // Now we have to translate the departureVelocity vector the the central coordinate system. For
        // this we need the orbital properties from the departure planet.
        PlanetOrbitalProperties planetOrbitalPropertiesFromPlanet = fromPlanet.getPlanetOrbitalProperties();

        // Get the necessary orbital properties from the departure planet
        double w_a = planetOrbitalPropertiesFromPlanet.getPeriphelonArgument(date);
        double o = planetOrbitalPropertiesFromPlanet.getAscendingNode(date);
        double i = planetOrbitalPropertiesFromPlanet.getInclination(date);

        // Attain the starting vector.
        startVelVec = KeplerToCartesian.orbitalToEclipticPlane(w_a, o, i, VelOrbitalFromPlan);
    }

    public void setStartVelocityVector(Vector3D velocityVector) {
        this.startVelVec = velocityVector;
    }

    @Override
    public String toString() {
        return "CannonBall{" +
                "name=" + getName() +
                ", centralPos=" + centralPos +
                ", centralVel=" + centralVel +
                ", startVel=" + departureVelocity +
                ", startInc=" + departureInclination +
                '}';
    }
}
