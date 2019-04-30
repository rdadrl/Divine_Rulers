package solarsystem;

import utils.Date;
import utils.Vector2D;
import utils.Vector3D;

import java.util.ArrayList;

/**
 * Rocket lander closed loop. works in a 2D environment where a rocket tries to land on Titan.
 * Forces are still calculated as a 3D vector as we use the z component for the calculation of
 * the rotation vector.
 *
 */
public class RocketLanderClosedLoop extends Projectile{
    private static int counter = 0;
    private double dryMass = 5000; //kg
    private double fuelMass = 10000; // kg
    private double mass;
    private double J = 100000; //moment of inertia kg m^2
    private double maxFtPropulsion = 44000; //newton
    private double maxFlPropulsion = 500; //newton
    private double thrusterImpulse = 3000; // ns/kg;
    private double g = 1.352; // m / s^2

    private long dt; // timestep in seconds
    private double Fl; // force thrusters
    private double Ft; // force latereral thrusters



    public RocketLanderClosedLoop(double length, Vector3D centralPos,
                                  Vector3D centralVel, Date date){
        this.name = "Rocket: " + counter;
        this.radius = length;
        this.mass = dryMass + fuelMass;
        this.date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date){
        dt = differenceInSeconds(date); // get size of the time step
        this.date = date;

        /*
        Ft = Math.min(Ft, maxFtPropulsion);
        Ft = Math.max(Ft, -maxFtPropulsion);
        Fl = Math.min(Fl, maxFlPropulsion);
        Fl = Math.max(Fl, -maxFlPropulsion);
        */
        Ft = 44000;
        Fl = 0;

        calculateMass();

        acceleration = new Vector3D(); // 3d acceleration where x and y are the positions and z is the angle
        // theta.
        double theta = centralPos.getZ();
        //page 2

        double xacc = ((Fl * Math.cos(theta) - Ft * Math.sin(theta))/mass);
        System.out.println("xacc:" + xacc);
        double yacc = ((Fl * Math.sin(theta) + Ft * Math.cos(theta))/mass) - g;
        System.out.println("yacc:" + yacc);
        double tacc = Fl*4/J;
        System.out.println("tacc:" + tacc);

       acceleration.setX(xacc);
       acceleration.setY(yacc);
       acceleration.setZ(tacc);
    }

    //TODO: check for bugs
    private void calculateMass() {
        double totalThrust = Math.abs(Ft) + Math.abs(Fl);
        double fuelMassLoss = (totalThrust/thrusterImpulse) * dt;
        fuelMass = fuelMass - fuelMassLoss;
        if(fuelMass<0) System.out.println("Ran out of fuel!");
        mass = dryMass + fuelMass;
    }

    private long differenceInSeconds(Date date) {
        return (date.getTimeInMillis()-this.date.getTimeInMillis())/1000;
    }


    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        centralPos = newCentralPos;
    }

    @Override
    public void setCentralVel(Vector3D newCentralVel) {
        this.date = new Date(date);
        this.centralVel = newCentralVel;
    }

    @Override
    public Vector3D getAcceleration() {
        return acceleration;
    }

    @Override
    public void checkColisions() { }

    /**
     * initializes the cartesian coordinates (i.e, position velocity vector) for the rocket
     * @param date Date to be initialized
     */
    @Override
    public void initializeCartesianCoordinates(Date date){}

    public double getFuelMass() {
        return fuelMass;
    }
}
