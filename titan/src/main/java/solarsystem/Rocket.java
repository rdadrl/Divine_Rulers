package solarsystem;

import utils.Date;
import utils.MathUtil;
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
    private Vector3D centralPos; // Coordinate central body reference frame
    private Vector3D centralVel; // Velocity central body reference frame
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
        Vector3D dir = toPlanet.getCentralPos().substract(centralPos).unit().scale(velocity);
        double dist = toPlanet.getCentralPos().substract(centralPos).length();
        dir = dir.scale(Math.pow(dist, 2)/toPlanet.getMass());
        Vector3D thrust = dir.substract(gravity);
        forces = gravity.add(thrust);
    }

    @Override
    public Vector3D getCentralPos() {
        return centralPos;
    }

    @Override
    public void setCentralPos(Vector3D centralPos, Date date) {
        this.date = new Date(date);
        double distF = centralPos.substract(fromPlanet.getCentralPos()).length() - (fromPlanet.getRadius() * 1000);
        if(distF < 0){
            this.centralPos = fromPlanet.getCentralPos();
        }
        double distT =
                centralPos.substract(toPlanet.getCentralPos()).length() - (toPlanet.getRadius() * 1000);
        if(distT < 0){
            this.centralPos = toPlanet.getCentralPos();
        }
        this.centralPos = centralPos;
    }

    @Override
    public Vector3D getCentralVel() {
        return centralVel;
    }

    @Override
    public void setCentralVel(Vector3D centralVel, Date date) {
        this.date = new Date(date);
        this.centralVel = centralVel;
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
        centralPos = fromPlanet.getcentralPos(date);
        Vector3D addRad = centralPos.unit().scale(fromPlanet.getRadius()*2000);
        centralPos = centralPos.add(addRad);
        /*
        double dist = centralPos.substract(fromPlanet.getCentralPos()).length();
        Vector3D test = centralPos.substract(fromPlanet.getCentralPos());
        //centralPos = fromPlanet.getCentralPos(date);
        */

        //centralVel = toPlanet.getCentralPos().substract(fromPlanet.getCentralPos()).unit().scale(velocity);
        if(startVelVec== null){
            startVelVec = fromPlanet.getCentralVel().unit().scale(velocity);
        }
        centralVel = startVelVec;

    }

}
