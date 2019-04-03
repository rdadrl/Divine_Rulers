package solarsystem;

import javafx.geometry.Point3D;
import physics.KeplerToCartesian;
import utils.Constant;
import utils.Date;
import utils.MathUtil;
import utils.Vector3D;


import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 *
 */
public class CannonBall implements CelestialObject {
    static double closestDistance = Constant.BESTDISTANCERANGECANNONBALL;
    private String name;
    private double mass;
    private double radius;
    private Vector3D HEEpos; // Coordinate central body reference frame
    private Vector3D oldHEEpos;
    private Vector3D HEEvel; // Velocity central body reference frame
    private Vector3D forces;
    private Planet fromPlanet;
    private Planet toPlanet;

    private boolean crashed;
    private Planet crashedPlanet;
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
        this.inclination = Math.toRadians(inclination);
        this.velocity = velocity * 1000;

        //this.velocity = fromPlanet.getHEEvel(date).length();
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
        cleckClosestDistance(newHEEpos, toPlanet, velocity, inclination, startVelVec);
        if(!crashed){
            checkCrash(fromPlanet, newHEEpos);
            checkCrash(toPlanet, newHEEpos);
            checkCrash(toPlanet.getCentralBody(), newHEEpos);
            oldHEEpos = HEEpos;
            HEEpos = newHEEpos;
        }else{
            HEEpos = crashedPlanet.getHEEpos();
        }
    }
    private void checkCrash(Planet planet, Vector3D newHEEpos){
        Point3D[] crash = MathUtil.collisionDetector(HEEpos, newHEEpos,
                planet.getHEEpos(),
                planet.getRadius()*1000);

        if(crash != null){
            System.out.println("collision with: " + planet.getName());
            System.out.println(Arrays.toString(crash));
            System.out.println(velocity);
            System.out.println(inclination);

            this.HEEpos = planet.getHEEpos();
            this.crashedPlanet = planet;
            crashed = true;
        }
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
        Vector3D addRadX = fromPlanet.getHEEpos(date).unit().scale((fromPlanet.getRadius() * 1000));
        Vector3D addRadY = fromPlanet.getHEEvel(date).unit().scale((fromPlanet.getRadius() * 1000));
        //addRad = addRad.add(new Vector3D(10,10,10000));
        HEEpos = HEEpos.add(addRadX);
        HEEpos = HEEpos.add(addRadY);
        //System.out.println(HEEpos.substract(fromPlanet.getHEEpos()).length()/1000);
        //System.out.println(fromPlanet.getRadius());
        /*
        double dist = HEEpos.substract(fromPlanet.getHEEpos()).length();
        Vector3D test = HEEpos.substract(fromPlanet.getHEEpos());
        //HEEpos = fromPlanet.getHEEpos(date);
        */

        //HEEvel = toPlanet.getHEEpos().substract(fromPlanet.getHEEpos()).unit().scale(velocity);
        if (startVelVec == null) {
            getStartVelocity();
        }
        HEEvel = startVelVec;
    }

    private void getStartVelocity(){
        Vector3D relEartV =
                fromPlanet.getOrbitalVel(date).rotateAntiClockWise(inclination).unit().scale(velocity);
        double w = fromPlanet.getPeriapsis();
        double o = fromPlanet.getAscendingNode();
        double i = fromPlanet.getInclination();
        startVelVec = KeplerToCartesian.orbitalToEclipticPlane(w, o, i, relEartV);
        //System.out.println(KeplerToCartesian.orbitalToEclipticPlane(w, o, i,fromPlanet.getOrbitalVel()));
        //System.out.println(fromPlanet.getHEEvel());
    }

    private static void cleckClosestDistance(Vector3D newHEEpos, Planet toPlanet, double velocity
            , double inclination, Vector3D startVelVec){
        double temp = closestDistance;
        Vector3D diff = newHEEpos.substract(toPlanet.getHEEpos());
        double distance = diff.length();

        double tempw = distance;
        if(distance < closestDistance){
            closestDistance = distance;
            System.out.println("Dist: " + closestDistance + "\tVel: " + velocity + "\tInc: " + Math.toDegrees(inclination) + "\tD_pos: " + diff);
        }

    }

}
