package solarsystem;

import utils.Date;
import utils.MathUtil;
import utils.Vector;
import utils.Vector3D;

import java.util.ArrayList;

/**
 *
 *
 */
public class CannonBall implements ObjectInSpace {
    private double mass;
    private double radius;
    private Vector3D HEEpos; // Coordinate central body reference frame
    private Vector3D HEEvel; // Velocity central body reference frame
    private Vector3D forces;
    private CelestialObjects fromPlanet;
    private CelestialObjects toPlanet;
    private Vector3D launchForce;



    /**
     * @param mass mass of the cannon ball
     * @param fromPlanet
     * @param launchForce
     */
    public CannonBall(double mass, double radius,
                         CelestialObjects fromPlanet,
                      CelestialObjects toPlanet){
        this.mass = mass;
        this.radius = radius;
        this.toPlanet = toPlanet;
        this.fromPlanet = fromPlanet;
    }
    public void setLaunchForce(Vector3D launchForce){
        this.launchForce = launchForce;
    }

    @Override
    public Vector3D getForces() {
        return forces;
    }

    @Override
    public void setForces(ArrayList<? extends ObjectInSpace> objectsInSpace){
        forces = MathUtil.gravitationalForces(this, objectsInSpace);
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
        //Make our cannon leave from the outside of the planet.
        Vector3D fromPlCoord = fromPlanet.getHEEpos(date);
        toPlanet.initializeCartesianCoordinates(date);
        double r = (fromPlanet.getRadius() * 1000 * MathUtil.AU);
        HEEpos = fromPlCoord.add(fromPlCoord.unit().scale(r));
        HEEvel =
                fromPlanet.getHEEpos().substract(toPlanet.getHEEpos()).unit().scale(20000);
    }
}
