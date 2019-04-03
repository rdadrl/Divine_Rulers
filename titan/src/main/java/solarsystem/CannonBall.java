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
    private Vector3D centralPos; // Coordinate central body reference frame
    private Vector3D oldcentralPos;
    private Vector3D centralVel; // Velocity central body reference frame
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

        //this.velocity = fromPlanet.getCentralVel(date).length();
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
    public Vector3D getCentralPos() {
        return centralPos;
    }

    @Override
    public void setCentralPos(Vector3D newcentralPos, Date date) {
        this.date = new Date(date);
        cleckClosestDistance(newcentralPos, toPlanet, velocity, inclination, startVelVec);

        // TODO check crashes after whole update.
        if(!crashed){
            checkCrash(fromPlanet, newcentralPos);
            checkCrash(toPlanet, newcentralPos);
            checkCrash(toPlanet.getCentralBody(), newcentralPos);
            oldcentralPos = centralPos;
            centralPos = newcentralPos;
        }else{
            centralPos = crashedPlanet.getCentralPos();
        }
    }
    private void checkCrash(Planet planet, Vector3D newcentralPos){
        Point3D[] crash = MathUtil.collisionDetector(centralPos, newcentralPos,
                planet.getCentralPos(),
                planet.getRadius()*1000);

        if(crash != null){
            System.out.println("collision with: " + planet.getName());
            System.out.println(Arrays.toString(crash));
            System.out.println(velocity);
            System.out.println(inclination);

            this.centralPos = planet.getCentralPos();
            this.crashedPlanet = planet;
            crashed = true;
        }
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
        Vector3D addRadX = fromPlanet.getcentralPos(date).unit().scale((fromPlanet.getRadius() * 1000));
        Vector3D addRadY = fromPlanet.getcentralVel(date).unit().scale((fromPlanet.getRadius() * 1000));
        //addRad = addRad.add(new Vector3D(10,10,10000));
        centralPos = centralPos.add(addRadX);
        centralPos = centralPos.add(addRadY);
        //System.out.println(centralPos.substract(fromPlanet.getCentralPos()).length()/1000);
        //System.out.println(fromPlanet.getRadius());
        /*
        double dist = centralPos.substract(fromPlanet.getCentralPos()).length();
        Vector3D test = centralPos.substract(fromPlanet.getCentralPos());
        //centralPos = fromPlanet.getCentralPos(date);
        */

        //centralVel = toPlanet.getCentralPos().substract(fromPlanet.getCentralPos()).unit().scale(velocity);
        if (startVelVec == null) {
            getStartVelocity(date);
        }
        centralVel = startVelVec;
    }

    private void getStartVelocity(Date date){
        Vector3D relEarthV = fromPlanet.initializeOrbitalVel(date).rotateAntiClockWise(inclination).unit().scale(velocity);
        OrbitalProperties orbitalPropertiesFromPlanet = fromPlanet.getOrbitalProperties();

        double w_a = orbitalPropertiesFromPlanet.getPeriphelonArgument(date);
        double o = orbitalPropertiesFromPlanet.getAscendingNode(date);
        double i = orbitalPropertiesFromPlanet.getInclination(date);
        startVelVec = KeplerToCartesian.orbitalToEclipticPlane(w_a, o, i, relEarthV);
        //System.out.println(KeplerToCartesian.orbitalToEclipticPlane(w, o, i,fromPlanet.initializeOrbitalVel()));
        //System.out.println(fromPlanet.getCentralVel());
    }

    private static void cleckClosestDistance(Vector3D newcentralPos, Planet toPlanet, double velocity
            , double inclination, Vector3D startVelVec){
        Vector3D diff = newcentralPos.substract(toPlanet.getCentralPos());
        double distance = diff.length();
        if(distance < closestDistance){
            closestDistance = distance;
            System.out.println("Dist: " + closestDistance + "\tVel: " + velocity + "\tInc: " + Math.toDegrees(inclination) + "\tD_pos: " + diff);
        }

    }

}
