package solarsystem.rocket.lunarLander.closedLoop;

import physics.PIDcontroller;
import solarsystem.CelestialObject;
import solarsystem.rocket.lunarLander.Lunarlander;
import utils.Date;
import utils.vector.Vector3D;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Rocket_temp lander closed loop. works in a 2D environment where a rocket tries to land on Titan.
 * Forces are still calculated as a 3D vector as we use the z component for the calculation of
 * the rotation vector.
 *
 */
public class LunarlanderLanderClosedLoop_checkInfluenceWind extends Lunarlander {
    private static int counter = 0;

    private PIDcontroller pidYdiff_far;
    private double cutoffClose = 10000.0;
    private double cutoffFinal = 5;
    private double cutoffJerk = 0.01;
    private PIDcontroller pidYdiff_close;
    private PIDcontroller pidXdiff_close;
    private PIDcontroller pidXdiff_far;
    private PIDcontroller pidRot_far;
    private PIDcontroller pidRot_close;

    private BigDecimal increment = new BigDecimal("0.01");

    private double oldxVel = 0;

    private boolean phase2X = false;
    private boolean phase2Y = false;
    private boolean phase3 = false;

    private int i =1;
    private Vector3D oldAcceleration_smooth = new Vector3D();
    private Vector3D newAcceleration_smooth = new Vector3D();

    public LunarlanderLanderClosedLoop_checkInfluenceWind(double length, Vector3D centralPos,
                                                          Vector3D centralVel, Date date, boolean stochasticWind, double meanWindSpeed){
        super();
        super.landedAltitude = 0.01;

        this.name = "Rocket_temp: " + counter;
        this.radius = length;
        this.mass = dryMass + fuelMass;
        this.old_date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.stochasticWind = stochasticWind;
        this.meanWindSpeed = meanWindSpeed;
        //pidYdiff_far = new PIDcontroller(-11.5, 0, -2350);
        //pidYdiff_close = new PIDcontroller(-25.7, 0, -1150);
        //pidXdiff_far = new PIDcontroller(0.002, 0, 0.01);
        //pidXdiff_close = new PIDcontroller(0.003, 0.00005, 0.02);
        //pidRot_far = new PIDcontroller(1000, 0,5000);
        //pidRot_close = new PIDcontroller(22810, 0,13838);

        pidYdiff_far = new PIDcontroller(-12.5, 0, -2350);
        //pidYdiff_far = new PIDcontroller(-12.5, 0, -1792);
        pidYdiff_close = new PIDcontroller(-26.0, 0, -900);
        pidXdiff_far = new PIDcontroller(0.0009, 0, 0.05);
        //pidXdiff_close = new PIDcontroller(0.001, 0.00001, 0.);
        pidXdiff_close = new PIDcontroller(0.001, 0.000009, 0.18);
        //pidXdiff_close = new PIDcontroller(0.002, 0.000005, 0.1);

        //pidXdiff_close = new PIDcontroller(0.0008, 0.00001, 0.2);

        pidRot_far = new PIDcontroller(1000, 0,5000);
        pidRot_close = new PIDcontroller(2500, 0,8500);
        if(stochasticWind) initializeSimpleWind();
    }

    public LunarlanderLanderClosedLoop_checkInfluenceWind(double length, Vector3D centralPos,
                                                          Vector3D centralVel, Date date) {
        this(length, centralPos, centralVel, date, false, 0);
    }

    public LunarlanderLanderClosedLoop_checkInfluenceWind(double length, Vector3D centralPos,
                                                          Vector3D centralVel, Date date, boolean stochasticWind) {
        this(length, centralPos, centralVel, date, stochasticWind, -99);
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date){
        dt = differenceInSeconds(date); // get size of the time step
        this.old_date = new Date(date);
        if(dt!= 0) updateController();

        // IF WE WANT TO USE PID TO TRACK WE NEED TO CHECK WITH THE POSITION AT TIME T-1;

        Ft = Math.min(Ft, maxFtPropulsion);
        Ft = Math.max(Ft, mass*g);
        Fl = Math.min(Fl, maxFlPropulsion);
        Fl = Math.max(Fl, -maxFlPropulsion);

        //Ft = 0;
        Fl = 0;

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

        if(stochasticWind) applySimpleWindForce();
        newAcceleration_smooth = newAcceleration_smooth.add(acceleration);
        if(i%100==0) {
            newAcceleration_smooth = newAcceleration_smooth.scale(1/100D);
            if(dt!= 0) setCentralJerk(oldAcceleration_smooth, newAcceleration_smooth, 100*dt);
            oldAcceleration_smooth = newAcceleration_smooth;
            newAcceleration_smooth = new Vector3D();
        }

        totTime = totTime.add(increment);
        i++;
    }

    private void updateController() {
        double yError = centralPos.getY();
        double xError = centralPos.getX();
        double tError = centralPos.getZ();

        if(!phase2X && totTime.doubleValue() > 50.0){
            if(totTime.doubleValue()%0.5 == 0){
                double curxVel = centralVel.getX();
                if(curxVel * oldxVel < 0 || (Math.abs(curxVel) < 0.05 && Math.abs(oldxVel) < Math.abs(curxVel))) {
                    phase2X = true;
//                    System.out.println(totTime.doubleValue());
//                    System.out.println("CP: " + centralPos.getX() );
//                    System.out.println("CV: " + curxVel );
//                    System.out.println("OV: " + oldxVel);
//                    System.out.println("CA: " + acceleration.getX() );
//                    System.out.println("CJ: " + centralJerk.getX() );
                }
                oldxVel = curxVel;
            }
        }
//        if(totTime.doubleValue() == 300.0) {
//            System.out.println(totTime.doubleValue());
//            System.out.println("CP: " + centralPos.getX() );
//            System.out.println("CV: " + centralVel.getX() );
//            System.out.println("CA: " + acceleration.getX() );
//            System.out.println("CJ: " + centralJerk.getX() );
//        }
        if(!phase2Y && (centralPos.getY() < cutoffClose)) {
            phase2Y = true;
        }

        double xImpulse = 0;


        if(Math.abs(centralVel.getZ()) < 1.5708){
            if(!phase2X){
                xImpulse = pidXdiff_far.calculateOutput(xError, dt);
            }else {
                //xImpulse = pidXdiff_far.calculateOutput(xError, dt);
                xImpulse = pidXdiff_close.calculateOutput(xError, dt);
            }
        }
        double tImpulse;
        if(!phase3 && ((centralPos.getY()-landedAltitude)/-centralVel.getY()) < cutoffFinal){
            phase3 = true;
            printStatus();
        }

        if(phase3){
            tImpulse = -tError;
            //tImpulse = (0.2*xImpulse)-tError;
            Fl = pidRot_close.calculateOutput(tImpulse, dt);
        }else{
            tImpulse = xImpulse-tError;
            Fl = pidRot_far.calculateOutput(tImpulse, dt);
        }


        if(!phase2Y){
            Ft = pidYdiff_far.calculateOutput(yError, dt);
        }else{
            Ft = pidYdiff_close.calculateOutput(yError, dt);
        }


        Ft = Ft + ((mass*g)/Math.cos(centralPos.getZ()));
    }

    public Vector3D getAccelerationSmooth() {
        return oldAcceleration_smooth;
    }


    public void printStatus() {
        System.out.println("time: " + totTime.toString() + "\n" +
                "fuel: " + fuelMass + "\n" +
                "Ft: " + Ft + "\n" +
                "Fl: " + Fl + "\n" +
                "w_acc: " + windAcc + "\n" +
                "x_acc: " + (acceleration.getX() - windAcc) + "\n" +
                "y-pos: " + this.getCentralPos().getY() + "\n" +
                "y-vel: " + this.getCentralVel().getY() + "\n" +
                "y-acc: " + this.getAcceleration().getY() + "\n" +
                "y-jerk: " + this.getCentralJerk().getY() + "\n" +
                "x-pos: " + this.getCentralPos().getX() + "\n" +
                "x-vel: " + this.getCentralVel().getX() + "\n" +
                "x-acc: " + this.getAcceleration().getX() + "\n" +
                "x-jerk: " + this.getCentralJerk().getX() + "\n" +
                "t-pos: " + this.getCentralPos().getZ() + "\n" +
                "t-vel: " + this.getCentralVel().getZ() + "\n" +
                "mean wind: " + meanWindSpeed + "\n\n");
    }

}
