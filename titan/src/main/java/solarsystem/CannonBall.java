package solarsystem;

import utils.Date;
import physics.MathUtil;
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
    private Vector3D startVelVec;


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
        this.velocity = velocity * 1000;
    }

    public CannonBall(double mass, double radius, Planet fromPlanet,
                      Planet toPlanet, Date date, Vector3D velocity){
        this.mass = mass;
        this.radius = radius;
        this.toPlanet = toPlanet;
        this.fromPlanet = fromPlanet;
        this.date = date;
        this.startVelVec = velocity;
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
    public void setHEEpos(Vector3D newHEEpos) {
        /*
        boolean colFromPlanet = MathUtil.FindLineSphereIntersections(HEEpos,newHEEpos,
                fromPlanet.getHEEpos(),
                fromPlanet.getRadius()*1000);
        */
        if(false){
            System.out.println("collision!");
            this.HEEpos = fromPlanet.getHEEpos();
        }
        this.HEEpos = newHEEpos;
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
        Vector3D addRad = HEEpos.unit().scale(fromPlanet.getRadius()*1000);
        HEEpos = HEEpos.substract(addRad);
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
