package solarsystem;

import utils.Date;
import physics.MathUtil;
import utils.Vector3D;


import java.util.ArrayList;

/**
 *
 *
 */
public class Rocket implements CelestialObject {
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
    private Vector3D startVelVec;


    /**
     * @param mass mass of the cannon ball
     * @param fromPlanet
     */
    public Rocket(double mass, double radius, Planet fromPlanet,
                      Planet toPlanet, Date date, double velocity){
        this.mass = mass;
        this.radius = radius;
        this.toPlanet = toPlanet;
        this.fromPlanet = fromPlanet;
        this.date = date;
        this.velocity = velocity * 1000;

    }

    @Override //TODO:change
    public String getName() {
        return "rocket";
    }

    @Override
    public Vector3D getForces() {
        return forces;
    }

    @Override
    public void setForces(ArrayList<? extends CelestialObject> objectsInSpace){
        Vector3D gravity = MathUtil.gravitationalForces(this, objectsInSpace);
        Vector3D dir = toPlanet.getHEEpos().substract(HEEpos).unit().scale(velocity);
        double dist = toPlanet.getHEEpos().substract(HEEpos).length();
        dir = dir.scale(Math.pow(dist, 2)/toPlanet.getMass());
        Vector3D thrust = dir.substract(gravity);
        forces = gravity.add(thrust);
    }

    @Override
    public Vector3D getHEEpos() {
        return HEEpos;
    }

    @Override
    public void setHEEpos(Vector3D HEEpos) {
        Vector3D test = HEEpos.substract(fromPlanet.getHEEpos());
        double distF = HEEpos.substract(fromPlanet.getHEEpos()).length() - (fromPlanet.getRadius() * 1000);
        if(distF < 0){
            this.HEEpos = fromPlanet.getHEEpos();
        }
        double distT =
                HEEpos.substract(toPlanet.getHEEpos()).length() - (toPlanet.getRadius() * 1000);
        if(distT < 0){
            this.HEEpos = toPlanet.getHEEpos();
        }
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
    public double getRadius() {
        return radius;
    }

    @Override
    public void initializeCartesianCoordinates(Date date) {
        //Make our cannon leave from the outside of the planet.
        HEEpos = fromPlanet.getHEEpos(date);
        Vector3D addRad = HEEpos.unit().scale(fromPlanet.getRadius()*2000);
        HEEpos = HEEpos.add(addRad);
        /*
        double dist = HEEpos.substract(fromPlanet.getHEEpos()).length();
        Vector3D test = HEEpos.substract(fromPlanet.getHEEpos());
        //HEEpos = fromPlanet.getHEEpos(date);
        */

        //HEEvel = toPlanet.getHEEpos().substract(fromPlanet.getHEEpos()).unit().scale(velocity);
        if(startVelVec== null){
            startVelVec = fromPlanet.getHEEvel().unit().scale(velocity);
        }
        HEEvel = startVelVec;

    }

}
