package solarsystem;

import utils.Date;
import utils.MathUtil;
import utils.Vector3D;

import java.util.ArrayList;

/**
 *
 *
 */
public class CannonBall implements ObjectInSpace {
    private double mass;
    private Vector3D HEEpos; // Coordinate central body reference frame
    private Vector3D HEEvel; // Velocity central body reference frame
    private Vector3D forces;
    private ObjectInSpace fromPlanet;
    private double launchForce;


    public CannonBall(double mass, ObjectInSpace fromPlanet, double launchForce) {
        this.mass = mass;
        this.fromPlanet = fromPlanet;
        this.launchForce = launchForce;
    }
    public void setLaunchForce(double launchForce){
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

    }
}
