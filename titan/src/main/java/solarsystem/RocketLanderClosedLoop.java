package solarsystem;

import physics.PIDcontroller;
import utils.Date;
import utils.Vector3D;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Rocket_temp lander closed loop. works in a 2D environment where a rocket tries to land on Titan.
 * Forces are still calculated as a 3D vector as we use the z component for the calculation of
 * the rotation vector.
 *
 */
public class RocketLanderClosedLoop extends Rocket{
    private static int counter = 0;
    private double dryMass = 5000; //kg
    private double fuelMass = 10000; // kg
    private double mass;
    private double J = 100000; //moment of inertia kg m^2
    private double maxFtPropulsion = 44000; //newton
    private double maxFlPropulsion = 500; //newton
    private double thrusterImpulse = 3000; // ns/kg;
    private double g = 1.352; // m / s^2

    private PIDcontroller pidYdiff_far;
    private double cutoffClose = 5000.0;
    private PIDcontroller pidYdiff_close;
    private PIDcontroller pidXdiff;
    private PIDcontroller pidRot;

    private BigDecimal totTime = new BigDecimal("0.0");
    private BigDecimal increment = new BigDecimal("0.01");
    private double dt; // timestep in seconds
    private double Fl; // force thrusters
    private double Ft; // force latereral thrusters
    private int i = 0;



    public RocketLanderClosedLoop(double length, Vector3D centralPos,
                                  Vector3D centralVel, Date date){
        this.name = "Rocket_temp: " + counter;
        this.radius = length;
        this.mass = dryMass + fuelMass;
        this.date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        pidYdiff_far = new PIDcontroller(-13.5, 0, -2350);
        pidYdiff_close = new PIDcontroller(-32.7, 0, -1150);
        pidXdiff = new PIDcontroller(0.001, 0, 0.02);
        pidRot = new PIDcontroller(1000, 0,5000);
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date){
        dt = differenceInSeconds(date); // get size of the time step
        this.date = new Date(date);
        if(dt!= 0) updateController();

        // IF WE WANT TO USE PID TO TRACK WE NEED TO CHECK WITH THE POSITION AT TIME T-1;

        Ft = Math.min(Ft, maxFtPropulsion);
        if(centralPos.getY() > cutoffClose){
            Ft = Math.max(Ft, mass*g);
        }else{
            Ft = Math.max(Ft, 0);
        }
        Fl = Math.min(Fl, maxFlPropulsion);
        Fl = Math.max(Fl, -maxFlPropulsion);

        //Ft = 0;
        //Fl = 0;

        calculateMass();

        acceleration = new Vector3D(); // 3d acceleration where x and y are the positions and z is the angle
        // theta.
        double theta = centralPos.getZ();
        //page 2

        double xacc = ((Fl * Math.cos(theta) - Ft * Math.sin(theta))/mass);
        double yacc = ((Fl * Math.sin(theta) + Ft * Math.cos(theta))/mass) - g;
        double tacc = Fl*4/J;

       acceleration.setX(xacc);
       acceleration.setY(yacc);
       acceleration.setZ(tacc);


       if(i%1000==0){
           printStatus();
       }
       i++;
       totTime = totTime.add(increment);
    }

    //TODO: check for bugs
    private void calculateMass() {
        double totalThrust = Math.abs(Ft) + Math.abs(Fl);
        double fuelMassLoss = (totalThrust/thrusterImpulse) * dt;
        fuelMass = fuelMass - fuelMassLoss;
        if(fuelMass<0){
            //System.out.println("Ran out of fuel!");
            fuelMass = 0;
        }
        mass = dryMass + fuelMass;
    }

    private double differenceInSeconds(Date date) {
        return (date.getTimeInMillis() - this.date.getTimeInMillis())/1000D;
    }

    private void updateController() {
        double yError = centralPos.getY();
        double xError = centralPos.getX();
        double tError = centralPos.getZ();

        double xImpulse = pidXdiff.calculateOutput(xError, dt);
        double tImpulse = xImpulse-tError;
        Fl = pidRot.calculateOutput(tImpulse, dt);
        if(centralPos.getY() > cutoffClose){
            Ft = pidYdiff_far.calculateOutput(yError, dt);
        }else{
            Ft = pidYdiff_close.calculateOutput(yError, dt);
        }
        Ft = Ft + mass*g;
    }


    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        centralPos = newCentralPos;
        if(newCentralPos.getY() < 0.01) {
            printStatus();
            System.out.println("Landed");
            System.exit(-1);
        }
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
    public void checkColisions() {
    }

    /**
     * initializes the cartesian coordinates (i.e, position velocity vector) for the rocket
     * @param date Date to be initialized
     */
    @Override
    public void initializeCartesianCoordinates(Date date){}

    public double getFuelMass() {
        return fuelMass;
    }

    private void printStatus() {
        System.out.println("time: " + totTime.toString() + "\n" +
                "fuel: " + fuelMass + "\n" +
                "Ft: " + Ft + "\n" +
                "Fl: " + Fl + "\n" +
                "y-pos: " + this.getCentralPos().getY() + "\n" +
                "y-vel: " + this.getCentralVel().getY() + "\n" +
                "x-pos: " + this.getCentralPos().getX() + "\n" +
                "x-vel: " + this.getCentralVel().getX() + "\n" +
                "t-pos: " + this.getCentralPos().getZ() + "\n" +
                "t-vel: " + this.getCentralVel().getZ() + "\n\n");
    }

    public double getSideThrusterForce() {
        return Fl;
    }

    public double getMainThrusterForce() {
        return Ft;
    }
    public double getMainThrusterForceAsPercentage() {
        return Ft/maxFtPropulsion;
    }
}
