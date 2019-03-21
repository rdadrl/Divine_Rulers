package solarsystem;

import utils.Date;
import utils.MathUtil;
import utils.Vector3D;

import java.util.ArrayList;

/**
 *
 *
 */
public class CannonBall implements CelestialObject {
    private String name;
    private double mass;
    private double radius;
    private Vector3D HEEpos; // Coordinate central body reference frame
    private Vector3D HEEvel; // Velocity central body reference frame
    private Vector3D forces;
    private Planet fromPlanet;
    private Planet toPlanet;
    private Date date;
    private double inclination;
    private double velocity;


    /**
     * @param mass mass of the cannon ball
     * @param fromPlanet
     */
    public CannonBall(double mass, double radius, Planet fromPlanet,
                      Planet toPlanet, Date date, double inclination, double velocity){
        this.mass = mass;
        this.radius = radius;
        this.toPlanet = toPlanet;
        this.fromPlanet = fromPlanet;
        this.date = date;
        this.inclination = inclination;
        this.velocity = velocity;

    }

    @Override //TODO:change
    public String getName() {
        return "cannonball";
    }

    @Override
    public Vector3D getForces() {
        return forces;
    }

    @Override
    public void setForces(ArrayList<? extends CelestialObject> objectsInSpace){
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
        HEEpos = fromPlanet.getHEEpos(date);
        HEEvel = fromPlanet.getHEEvel(date).add(fromPlanet.getHEEvel().unit().scale(velocity));
    }
}
