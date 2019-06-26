package solarsystem.rocket.lunarLander.closedLoop;

import physics.PIDcontroller;
import solarsystem.CelestialObject;
import solarsystem.rocket.lunarLander.Lunarlander;
import utils.AverageQueue;
import utils.Date;
import utils.vector.Vector3D;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Rocket_temp lander closed loop. works in a 2D environment where a InterPlanetaryRocketToTitan tries to land on Titan.
 * Forces are still calculated as a 3D vector as we use the z component for the calculation of
 * the rotation vector.
 *
 */
public class LunarlanderLanderClosedLoop_advancedWind_good extends Lunarlander {
    private static int counter = 0;
    private boolean advancedWind = true;

    private PIDcontroller pidYdiff_far;
    private double getCutoffClose_x = 10000.0; // start of the second x phase
    private double cutoffClose_y = 10000.0; // start of the second phase based upon the y coordinate
    private double cutoffClose_yVel = -100.0;

    private double cutoffFinal = 5;

    private PIDcontroller pidYdiff_close, pidXdiff_close, pidXdiff_far, pidRot_far, pidRot_close, max_pidRot, min_pidRot, pidXdiff_far_MT;
    private double rotMax = 1.5; // maximum rotation of 90 degrees.

    private BigDecimal increment = new BigDecimal("0");

    private double oldxVel = 0;
    private double oldxPos = 0;


    // boolean representing the different phases, use to acitvate the correct phase at the desired time
    private boolean phase2X = false;
    double phase2X_time = 0;
    private boolean phase3X = false;
    private boolean phase2Y = false;
    double phase2Y_time = 0;
    private boolean phase3 = false;
    private double phase3_time = 0;


    private int i =1;
    private Vector3D oldAcceleration_smooth = new Vector3D();
    private Vector3D newAcceleration_smooth = new Vector3D();
    private Vector3D oldAcceleration_avg_absolute = new Vector3D();
    private Vector3D newAcceleration_avg_absolute = new Vector3D();

    private AverageQueue jerk_average_x;
    private AverageQueue acc_average_x;


    public LunarlanderLanderClosedLoop_advancedWind_good(double length, Vector3D centralPos,
                                                         Vector3D centralVel, Date date, boolean stochasticWind, double meanWindSpeed){
        super();
        super.landedAltitude = 0.01;

        this.name = "Rocket_temp: " + counter;
        this.radius = length;
        this.mass = dryMass + fuelMass;
        this.current_date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.stochasticWind = stochasticWind;
        this.meanWindSpeed = meanWindSpeed;

        pidYdiff_far = new PIDcontroller(-11.5, 0, -2350);
        pidYdiff_close = new PIDcontroller(-26.0, 0, -950);
        pidXdiff_far = new PIDcontroller(0.004, 0, 0.08);
        pidXdiff_far_MT = new PIDcontroller(-11.0, 0, -2350);
        pidRot_far = new PIDcontroller(1000, 0,5000);

        max_pidRot = new PIDcontroller(500, 0,8000);
        max_pidRot.setTarget_pos(rotMax);
        min_pidRot = new PIDcontroller(500, 0,8000);
        min_pidRot.setTarget_pos(-rotMax);

        jerk_average_x = new AverageQueue(100);
        acc_average_x = new AverageQueue(50);

        pidRot_close = new PIDcontroller(2500, 0,8500);
        if(stochasticWind){
            if(advancedWind) initializeAdvancedWind();
            else initializeSimpleWind();
        }
    }

    public LunarlanderLanderClosedLoop_advancedWind_good(double length, Vector3D centralPos,
                                                         Vector3D centralVel, Date date) {
        this(length, centralPos, centralVel, date, false, 0);
    }

    public LunarlanderLanderClosedLoop_advancedWind_good(double length, Vector3D centralPos,
                                                         Vector3D centralVel, Date date, boolean stochasticWind) {
        this(length, centralPos, centralVel, date, stochasticWind, -99);
    }

    /**
     * set acceleration method used by the ODE solver to solve for velocity and position respectively
     * @param objectsInSpace arraylist of all the objects that apply a force to
     * @param new_date
     */
    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date new_date){

        // keep track of the time spend, current date etc.
        increment = new BigDecimal(differenceInMiliSeconds(new_date)).divide(new BigDecimal(1000));
        dt = increment.doubleValue();
        this.current_date = new Date(new_date);

        // attain the right Ft and Fl based upon the controller
        if(dt!= 0) updateController();

        // only do gravity control when we are close to the surface, as we need to be extra precise there to land
        // within the required margins
        Ft = Math.min(Ft, maxFtPropulsion);
        if(phase2Y){
            Ft = Math.max(Ft, ((mass*g)/Math.cos(centralPos.getZ())));
            //Ft = Math.max(Ft, (mass*g));
        }else{
            Ft = Math.max(Ft, 0);
        }

        // make sure that Ft and Fl are within the maximum and minimum thrust abilitlies
        Ft = Math.min(Ft, maxFtPropulsion);
        Fl = Math.min(Fl, maxFlPropulsion);
        Fl = Math.max(Fl, -maxFlPropulsion);


        // recalculate the mass of the spacecraft a
        calculateMass();

        acceleration = new Vector3D(); // 3d acceleration where x and y are the positions and z is the angle
        // theta.
        double theta = centralPos.getZ();
        //page 2

        // attain and set the desired acceleration.
        double xacc = ((Fl * Math.cos(theta) - Ft * Math.sin(theta))/mass);
        double yacc = ((Fl * Math.sin(theta) + Ft * Math.cos(theta))/mass) - g;
        double tacc = Fl*4/J;

        acceleration.setX(xacc);
        acceleration.setY(yacc);
        acceleration.setZ(tacc);


        // aply stochastic wind if desired.

        if(stochasticWind) {
            if(advancedWind) applyAdvancedWindForce();
            else applySimpleWindForce(); // we also have a simple windforce generator.
        }


        // this is to smoothen out the acceleration provided by the rocket. It's main purpuse is for the graphical
        // representation of the acceleration, to get a good overview of the acceleration used.
        newAcceleration_smooth = newAcceleration_smooth.add(acceleration);
        newAcceleration_avg_absolute = newAcceleration_avg_absolute.add(acceleration.absolute());
        if(i%100==0) {
            newAcceleration_smooth = newAcceleration_smooth.scale(1/100D);
            if(dt!= 0) {
                setCentralJerk(oldAcceleration_smooth, newAcceleration_smooth, 100*dt);
                jerk_average_x.push(Math.abs(centralJerk.getX()));
            }
            oldAcceleration_smooth = newAcceleration_smooth;
            oldAcceleration_avg_absolute = newAcceleration_avg_absolute.scale(1/100D);
            acc_average_x.push(oldAcceleration_avg_absolute.getX());
            newAcceleration_smooth = new Vector3D();
            newAcceleration_avg_absolute = new Vector3D();
        }

        // update time and counter.
        totTime = totTime.add(increment);
        i++;
//        System.out.println("T: " + totTime + ", A: " + acceleration);
    }

    private void updateController() {
        // get the associated errors needed for the PID controller
        double yError = centralPos.getY();
        double xError = centralPos.getX();
        double xError_vel = centralVel.getX();
        double tError = centralPos.getZ();

        // For each phase we need to use the appropriate PID controller (different phases have different values for
        // P, and D.
        if(!phase2X && totTime.doubleValue() > 50.0){
            double curxVel = centralVel.getX();
            double curxPos = centralPos.getX();


            if(centralPos.getY() < getCutoffClose_x){
                phase2X = true;
                phase2X_time = totTime.doubleValue();
            }
            oldxVel = curxVel;
            oldxPos = curxPos;
        }

        // check if we have to start phase 2Y
        if(!phase2Y && (centralPos.getY() < cutoffClose_y) && (centralVel.getY() > cutoffClose_yVel)) {
            phase2Y = true;
            phase2Y_time = totTime.doubleValue();
        }
        // check if we have to start phase 3X
        if(!phase3X && (centralPos.getY() < 5)) {
            phase3X = true;
        }

        // Calculate the correct x impulse that is being pass to the inner loop controller of the angle
        double xImpulse;
        if(!phase2X){
            xImpulse = pidXdiff_far.calculateOutput(xError, xError_vel, dt);
            //xImpulse = pidXdiff_close.calculateOutput(xError, dt);
        }else {
            xImpulse = pidXdiff_far.calculateOutput(xError,xError_vel, dt);
        }


        double tImpulse;
        // check if the last phase needs to be started. To end up with a spacecraft orientated horizontally.
        if(!phase3 && ((centralPos.getY()-landedAltitude)/-centralVel.getY()) < cutoffFinal){
            phase3 = true;
            phase3_time = totTime.doubleValue();
        }

        if(phase3){
            tImpulse = -tError;
//            tImpulse = (0.2*xImpulse)-tError;
            Fl = pidRot_close.calculateOutput(tImpulse, dt);
        }else{
            tImpulse = xImpulse-tError;
            rotateController(tImpulse, pidRot_far);

        }

        // Get the desired Ft_y and Fl values.
        double Ft_y;
        if(!phase2Y){
            Ft_y = pidYdiff_far.calculateOutput(yError, dt);
        }else{
            Ft_y = pidYdiff_close.calculateOutput(yError, dt);
        }
        Ft_y = Ft_y/Math.cos(centralPos.getZ()) + ((mass*g)/Math.cos(centralPos.getZ()));


        // Last we need to do some controlling of the angele. This is because a large x displacement will result
        // in a spinning of the lunar lander, which is something we have to avoid. Therefore, we put a controller
        // the desired end points to counteract this.
        if(Math.abs(centralPos.getZ())>0.8) {
            double Ft_x =  Math.abs(centralPos.getZ()) * Math.abs(pidXdiff_far_MT.calculateOutput(xError, dt));
            Ft = Math.max(Ft_y, Ft_x);
            //System.out.println(totTime + ",  used");
        }else{
            Ft = Ft_y;
        }




    }

    /**
     * method to calulate the desired Fl force of the side thrusters.
     */
    private void rotateController(double tImpulse, PIDcontroller pidRot){
        double Fl_main = pidRot.calculateOutput(tImpulse, dt);
        double tpos = centralPos.getZ();
        double tvel = centralVel.getZ();

        if(tvel > 0) {
            double Fl_max = -max_pidRot.calculateOutput(tpos, tvel, dt);
            Fl = Math.min(Fl_main, Fl_max);
//                if(i%10 == 0)System.out.println("Fl max: " + Fl_max);
//                if(i%10 == 0 && Fl_max < Fl_main)System.out.println("A:");
        }else if(tvel < 0) {
            double Fl_min = -min_pidRot.calculateOutput(tpos, tvel, dt);
            Fl = Math.max(Fl_main, Fl_min);
//                if(i%10 == 0)System.out.println("Fl min: " + Fl_min);
//                if(i%10 == 0 && Fl_min > Fl_main)System.out.println("B:");
        }else{
            Fl = Fl_main;
        }
//            if(i%10 == 0)System.out.println("Fl: " + Fl + "\n");
    }


    /**
     * @return smoothend out acceleration
     */
    public Vector3D getAccelerationSmooth() {
        return oldAcceleration_smooth;
    }

    /**
     * @return the jerk of the spacecraft
     */
    public double getAverageJerkX(){
        return  jerk_average_x.average();
    }

    public double getAverageAccX(){
        return acc_average_x.average();
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
                "x_acc_sm: " + oldAcceleration_smooth.getX() + "\n" +
                "x_acc_ab: " + oldAcceleration_avg_absolute.getX() + "\n" +
                "x-jerk: " + this.getCentralJerk().getX() + "\n" +
                "t-pos: " + this.getCentralPos().getZ() + "\n" +
                "t-vel: " + this.getCentralVel().getZ() + "\n" +
                "mean wind: " + meanWindSpeed + "\n" +
                "cur wind: " + currentWindSpeed + "\n" +
                "P2Y_t: " + phase2Y_time + "\n" +
                "P2X_t: " + phase2X_time + "\n" +
                "P3_t: " + phase3_time + "\n\n");

    }
}
