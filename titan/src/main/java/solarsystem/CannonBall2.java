package solarsystem;
import Rocket.HohmannTransfer;

import utils.Date;
import java.util.Calendar;
import utils.MathUtil;
import utils.Vector3D;
import utils.Vector2D;
import java.io.IOException;

import java.util.ArrayList;

/**
 *
 *
 */
/*
public class CannonBall2 implements CelestialObject {


    public static void main(String[] args) {
        try {
            SolarSystem solarSystem = new SolarSystem();
            Planet departurePlanet = solarSystem.getPlanets().getEarth();
            Planet arrivalPlanet = solarSystem.getPlanets().getSaturn();

            Date date = new Date(2000, 0, 1);
            boolean didFindDate = false;
            HohmannTransfer transferOrbit = new HohmannTransfer(departurePlanet, arrivalPlanet);
            double phaseAngle = transferOrbit.getPhaseAngle();
            double observedAngle = 0;
            CannonBall2 cannonBall = new CannonBall2(1, 1000, departurePlanet, arrivalPlanet, transferOrbit.getInitialImpulse());
            while (!didFindDate) {
                observedAngle = cannonBall.getProjectedAngle(departurePlanet.getHEEpos(), arrivalPlanet.getHEEpos());
                if (observedAngle == phaseAngle) didFindDate = true;
                else date.add(Calendar.DAY_OF_YEAR, 7);
            }


            // Compute the force vector
            double length = cannonBall.getLaunchForce();
            cannonBall.setLaunchVector(departurePlanet.getHEEpos().rotateZDeg(90));
        } catch (IOException exception) {
            System.out.println(exception);
        }
    }

    private double mass;
    private double radius;
    private Vector3D HEEpos; // Coordinate central body reference frame
    private Vector3D HEEvel; // Velocity central body reference frame
    private Vector3D forces;
    private CelestialObject fromPlanet;
    private CelestialObject toPlanet;
    private double launchForce;
    private Vector3D launchVector;

    public CannonBall2(double mass, double radius, Planet fromPlanet, Planet toPlanet) {
        this.mass = mass;
        this.radius = radius;
        this.fromPlanet = fromPlanet;
        this.toPlanet = toPlanet;
    }

    public double getProjectedAngle(Vector3D departurePosition3D, Vector3D arrivalPosition3D) {
        // Project them into the eccliptical reference plane
        Vector2D departurePosition2D = new Vector2D(departurePosition3D.getX(), departurePosition3D.getY());
        Vector2D arrivalPosition2D = new Vector2D(arrivalPosition3D.getX(), arrivalPosition3D.getY());

        // Get angle between the projected vectors
        double departureAngle = Math.acos(departurePosition2D.getX() / departurePosition2D.length());
        double arrivalAngle = Math.acos(arrivalPosition2D.getX() / arrivalPosition2D.length());
        return arrivalAngle - departureAngle;
    }

    public void setLaunchForce(double launchForce){
        this.launchForce = launchForce;

    }

    @Override
    public Vector3D getForces() {
        return forces;
    }

    @Override
    public void setForces(ArrayList<? extends CelestialObject> objectsInSpace){
        forces = MathUtil.gravitationalForces(this, objectsInSpace);
    }

    public void setLaunchVector(Vector3D vector) {
        launchVector = vector;
    }

    @Override
    public Vector3D getHEEpos() {
        return HEEpos;
    }

    @Override
    public void setHEEpos(Vector3D HEEpos) {
        this.HEEpos = HEEpos;
    }

    @Override
    public Vector3D getHEEvel() {
        return HEEvel;
    }

    @Override
    public void setHEEvel(Vector3D HEEvel) {
        this.HEEvel = HEEvel;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public void initializeCartesianCoordinates(Date date) {

    }

}
*/
