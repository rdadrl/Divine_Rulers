package solarsystem.rocket;

import solarsystem.CelestialObject;
import solarsystem.Planet;
import utils.vector.Vector3D;

/**
 * A projectile class which is used as a building block for all projectiles i.e., InterPlanetaryRocketToTitan and
 * cannonballs.
 *
 */
public abstract class Projectile extends CelestialObject {
    protected boolean phaseFinished;

    public static double minDistanceAll = Double.MAX_VALUE;
    public static double minDistanceAllCurrentDT = Double.MAX_VALUE;
    private double closestDistanceThisProjectile = Double.MAX_VALUE;
    private double currentDistance = Double.MAX_VALUE;

    protected double departureInclination;
    protected double departureVelocity;
    protected Vector3D startVelVec;

    protected Vector3D oldCentralPos = new Vector3D(); // Coordinate with the sun as a center.

    protected Planet fromPlanet;
    protected Planet toPlanet;
    private boolean crashed;
    private Planet crashedPlanet;

    public double getDepartureInclination() {
        return departureInclination;
    }

    public void setDepartureInclination(double departureInclination) {
        this.departureInclination = departureInclination;
    }

    /**
     * @return starting departureVelocity of the planet
     */
    public double getDepartureVelocity() {
        return departureVelocity;
    }

    public void setDepartureVelocity(double departureVelocity) {
        this.departureVelocity = departureVelocity;
    }

    public Vector3D getStartVelVec() {
        return startVelVec;
    }

    /**
     * @return departure planet
     */
    public Planet getFromPlanet() {
        return fromPlanet;
    }


    /**
     * @return arrival planet
     */
    public Planet getToPlanet() {
        return toPlanet;
    }

    /**
     * @return current distance to the destination planet
     */
    public double getCurrentDistance() {return currentDistance; }

    /**
     * @return whether planet has crashed
     */
    public boolean isCrashed() {
        return crashed;
    }

    /**
     * @return planet on which the projectile crashed.
     */
    public Planet getCrashedPlanet() {
        return crashedPlanet;
    }

    /**
     * Method to update the central position of the cannonball object.
     * @param newCentralPos central position to be update
     */
    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        oldCentralPos = centralPos;
        centralPos = newCentralPos;
        // if the projectile hasn't crashed yet we will update the location as normal
        super.setCentralPos(newCentralPos);
        if(toPlanet != null) checkClosestDistance();
    }


    public void resetClosestDistanceThisProjectile() {
        closestDistanceThisProjectile = Double.MAX_VALUE;
        currentDistance = Double.MAX_VALUE;
    }

    /**
     * @return closest distance of the current projectile
     */
    public double getClosestDistanceThisProjectile() {
        return closestDistanceThisProjectile;
    }

    /**
     * Keeps track of the closest a projectile will get to the arrival planet.
     */
    private void checkClosestDistance() {
        Vector3D diff = centralPos.substract(toPlanet.getCentralPos());
        currentDistance = diff.length();
        // if the currentDistance is less than the current closest currentDistance of all the projectiles update
        if (currentDistance < closestDistanceThisProjectile) {
            closestDistanceThisProjectile = currentDistance;
            checkClosestDistanceAll(centralPos, toPlanet, departureVelocity, departureInclination, startVelVec);
            //System.out.println(name + ": " + closestDistanceThisProjectile);
        }
        // if the distances is closer than its current closest currentDistance update.
        if (currentDistance < minDistanceAllCurrentDT) {
            minDistanceAllCurrentDT = currentDistance;
        }
    }

    /**
     * checks the closest any projectile will have gotten to the arrival planet
     * @param newcentralPos central position of the projectile
     * @param toPlanet arrival planet
     * @param velocity start departureVelocity of the projectile
     * @param inclination start departureInclination of the projectile
     * @param startVelVec start velocityVector of the projectile
     */
    private static void checkClosestDistanceAll(Vector3D newcentralPos, Planet toPlanet, double velocity
            , double inclination, Vector3D startVelVec){
        Vector3D diff = newcentralPos.substract(toPlanet.getCentralPos());
        double distance = diff.length();
        if(distance < minDistanceAll){
            minDistanceAll = distance;
            if(distance<utils.Constant.BEST_DISTANCE_RANGE_CANNONBALL)
            System.out.println("Dist: " + minDistanceAll + "\tVel: " + velocity + "\tInc: " + Math.toDegrees(inclination) + "\tD_pos: " + diff);
        }
    }

    /**
     * Check whether a collision between the projectile and certain planets happened.
     */
    public void checkColisions() {
        if (crashed) return;
        if(fromPlanet != null) checkCollisionWithPlanet(fromPlanet);
        if(toPlanet != null) checkCollisionWithPlanet(toPlanet);
    }

    /**
     * Checks a collision with a particulair planet
     * @param planet planet to detect crash with
     */
    private void checkCollisionWithPlanet(Planet planet){
        if(crashed) return;
        // difference vector
        Vector3D diff = this.centralPos.substract(planet.getCentralPos());
        // distance between planet an projectile
        double distance = diff.length();

        // if the distances is less than the radius of the planet, we collided
        // with the planet.
        if(distance < planet.getRadius()){
            System.out.println("Crashed with: " + planet.getName());
            System.out.println("Dist: " + minDistanceAll + "\tVel: " + departureVelocity + "\tInc: " + Math.toDegrees(departureInclination) + "\tD_pos: " + diff);
            this.centralPos = planet.getCentralPos();
            this.crashedPlanet = planet;
            crashed = true;
        }


        /*
        // FIXME: DEBUG THE FOLLOWING SECTION
        Point3D[] crash = MathUtil.collisionDetector(oldCentralPos, centralPos,
                planet.getCentralPos(),
                planet.getRadius()*1000);

        if(crash != null){
            System.out.println("collision with: " + planet.getName());
            System.out.println(Arrays.toString(crash));
            System.out.println(departureVelocity);
            System.out.println(departureInclination);

            this.centralPos = planet.getCentralPos();
            this.crashedPlanet = planet;
            crashed = true;
        }
        */
    }
    public static void resetDistances() {
        minDistanceAll = Double.MAX_VALUE;
        minDistanceAllCurrentDT = Double.MAX_VALUE;

    }

    public boolean phaseFinished(){
        return phaseFinished;
    }
}
