package solarsystem;

import utils.Date;
import utils.Vector3D;


import java.util.ArrayList;



public class Rocket_temp extends Projectile{
    private static int counter = 0;
    public Rocket_temp(double mass, double radius, Planet fromPlanet,
                       Planet toPlanet, Date date, double velocity){
        this.name = "Rocket_temp: " + counter;
        this.mass = mass;
        this.radius = radius;
        this.toPlanet = toPlanet;
        this.fromPlanet = fromPlanet;
        this.date = date;
        this.departureVelocity = velocity * 1000;
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date){
        this.date = date;
        Vector3D gravity = gravitationalForces(this, objectsInSpace);
        Vector3D dir = toPlanet.getCentralPos().substract(centralPos).unit().scale(departureVelocity);
        double dist = toPlanet.getCentralPos().substract(centralPos).length();
        dir = dir.scale(Math.pow(dist, 2)/toPlanet.getMass());
        Vector3D thrust = dir.substract(gravity);
        acceleration = gravity.add(thrust);
    }

    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        double distF = newCentralPos.substract(fromPlanet.getCentralPos()).length() - (fromPlanet.getRadius() * 1000);
        if(distF < 0){
            this.centralPos = fromPlanet.getCentralPos();
        }
        double distT =
                newCentralPos.substract(toPlanet.getCentralPos()).length() - (toPlanet.getRadius() * 1000);
        if(distT < 0){
            this.centralPos = toPlanet.getCentralPos();
        }
        this.centralPos = newCentralPos;
    }

    @Override
    public void setCentralVel(Vector3D centralVel) {
        this.centralVel = centralVel;
    }

    /**
     * initializes the cartesian coordinates (i.e, position velocity vector) for the rocket
     * @param date Date to be initialized
     */
    @Override
    public void initializeCartesianCoordinates(Date date) {
        //Make our cannon leave from the outside of the planet.
        centralPos = fromPlanet.getCentralPosAtDate(date);
        Vector3D addRad = centralPos.unit().scale(fromPlanet.getRadius()*2000);
        centralPos = centralPos.add(addRad);
        if(startVelVec== null){
            startVelVec = fromPlanet.getCentralVel().unit().scale(departureVelocity);
        }
        centralVel = startVelVec;

    }


}