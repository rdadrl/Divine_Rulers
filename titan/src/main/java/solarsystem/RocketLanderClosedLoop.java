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

    private PIDcontroller pidYdiff_far;
    private double cutoffClose = 5000.0;
    private PIDcontroller pidYdiff_close;
    private PIDcontroller pidXdiff_close;
    private PIDcontroller pidXdiff_far;
    private PIDcontroller pidRot;

    private BigDecimal totTime = new BigDecimal("0.0");
    private BigDecimal increment = new BigDecimal("0.01");

    private int i = 0;



    public RocketLanderClosedLoop(double length, Vector3D centralPos,
                                  Vector3D centralVel, Date date, boolean stochasticWind){
        this.name = "Rocket_temp: " + counter;
        this.radius = length;
        this.mass = dryMass + fuelMass;
        this.date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.stochasticWind = stochasticWind;
        pidYdiff_far = new PIDcontroller(-11.5, 0, -2350);
        pidYdiff_close = new PIDcontroller(-25.7, 0, -1150);
        pidXdiff_far = new PIDcontroller(0.002, 0.0000, 0.01);
        pidXdiff_close = new PIDcontroller(0.003, 0.00001, 0.02);
        pidRot = new PIDcontroller(1000, 0,5000);

        if(stochasticWind) initializeWind();
    }

    public RocketLanderClosedLoop(double length, Vector3D centralPos,
                                  Vector3D centralVel, Date date) {
        this(length, centralPos, centralVel, date, false);
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date){
        dt = differenceInSeconds(date); // get size of the time step
        this.date = new Date(date);
        if(dt!= 0) updateController();

        // IF WE WANT TO USE PID TO TRACK WE NEED TO CHECK WITH THE POSITION AT TIME T-1;

        Ft = Math.min(Ft, maxFtPropulsion);
        if(centralPos.getY() < cutoffClose){
            Ft = Math.max(Ft, mass*g);
        }else{
            Ft = Math.max(Ft, mass*g);
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

       if(stochasticWind) applyWindForce();


       if(i%1000==0){
           printStatus();
       }
       i++;
       totTime = totTime.add(increment);
    }

    private void updateController() {
        double yError = centralPos.getY();
        double xError = centralPos.getX();
        double tError = centralPos.getZ();

        double xImpulse;
        if(centralPos.getY() > cutoffClose){
            xImpulse = pidXdiff_far.calculateOutput(xError, dt);
        }else{
            xImpulse = pidXdiff_close.calculateOutput(xError, dt);
        }


        double tImpulse = xImpulse-tError;
        Fl = pidRot.calculateOutput(tImpulse, dt);
        if(centralPos.getY() > cutoffClose){
            Ft = pidYdiff_far.calculateOutput(yError, dt);
        }else{
            Ft = pidYdiff_close.calculateOutput(yError, dt);
        }
        Ft = Ft + mass*g;
    }

    private void initializeWind() {
        // random wind speed form -10 to 10 meters per seconds;
        //meanWindSpeed = (Math.random() * 20) - 10;
        meanWindSpeed = 10;
    }

    private void applyWindForce() {
        // random windnoise of -0.5 m to 0.5 m.
        double windNoise = Math.random() - 0.5;
        currentWindSpeed = meanWindSpeed + windNoise;
        // force = area of impact * air density * windSpeed
        double windForce = sidearea * airDensity * currentWindSpeed;
        windAcc = windForce/mass;
        //System.out.println("Acc: " + acceleration.getX());
        //System.out.println("Wind_acc: " + windAcc);
        //System.out.println();

        acceleration.setX(acceleration.getX() + windAcc);
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




    private void printStatus() {
        System.out.println("time: " + totTime.toString() + "\n" +
                "fuel: " + fuelMass + "\n" +
                "Ft: " + Ft + "\n" +
                "Fl: " + Fl + "\n" +
                "w_acc: " + windAcc + "\n" +
                "x_acc: " + (acceleration.getX() - windAcc) + "\n" +
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

}
