package solarsystem;

import physics.PIDcontroller;
import utils.AverageQueue;
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
public class RocketLanderClosedLoop_advancedWind_good extends Rocket{
    private static int counter = 0;
    private boolean advancedWind = true;

    private PIDcontroller pidYdiff_far;
    private double getCutoffClose_x = 10000.0;
    private double cutoffClose_y = 10000.0;
    private double cutoffClose_yVel = -100.0;
//    private double cutoffClose_x = 5000.0;
    private double cutoffFinal = 8;
//    private double cutoffJerk = 0.01;
    private PIDcontroller pidYdiff_close, pidXdiff_close, pidXdiff_far, pidRot_far, pidRot_close, max_pidRot, min_pidRot, pidXdiff_far_MT;
    private double rotMax = 1.5; // maximum rotation of 90 degrees.

    private BigDecimal increment = new BigDecimal("0");

    private double oldxVel = 0;
    private double oldxPos = 0;

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


    public RocketLanderClosedLoop_advancedWind_good(double length, Vector3D centralPos,
                                                    Vector3D centralVel, Date date, boolean stochasticWind, double meanWindSpeed){
        super.landedAltitude = 0.01;

        this.name = "Rocket_temp: " + counter;
        this.radius = length;
        this.mass = dryMass + fuelMass;
        this.old_date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.stochasticWind = stochasticWind;
        this.meanWindSpeed = meanWindSpeed;
//        pidYdiff_far = new PIDcontroller(-12.5, 0, -2350);
//        pidYdiff_close = new PIDcontroller(-26.0, 0, -900);
//        pidXdiff_far = new PIDcontroller(0.002, 0, 0.06);
//        pidXdiff_close = new PIDcontroller(0.007, 0.0000015, 0.06);
//        pidRot_far = new PIDcontroller(1000, 0,5000);
//        pidRot_close = new PIDcontroller(2500, 0,8500);

        pidYdiff_far = new PIDcontroller(-11.5, 0, -2350);

        //pidYdiff_far = new PIDcontroller(-12.5, 0, -1792);
        pidYdiff_close = new PIDcontroller(-26.0, 0, -950);
        pidXdiff_far = new PIDcontroller(0.002, 0, 0.08);

        pidXdiff_far_MT = new PIDcontroller(-11.0, 0, -2350);

        //pidXdiff_close = new PIDcontroller(0.001, 0.00001, 0.);
        pidXdiff_close = new PIDcontroller(0.006,  0, 0.24);
        //pidXdiff_close = new PIDcontroller(0.002, 0.000005, 0.1);
        //pidXdiff_close = new PIDcontroller(0.0008, 0.00001, 0.2);





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

    public RocketLanderClosedLoop_advancedWind_good(double length, Vector3D centralPos,
                                                    Vector3D centralVel, Date date) {
        this(length, centralPos, centralVel, date, false, 0);
    }

    public RocketLanderClosedLoop_advancedWind_good(double length, Vector3D centralPos,
                                                    Vector3D centralVel, Date date, boolean stochasticWind) {
        this(length, centralPos, centralVel, date, stochasticWind, -99);
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date new_date){

        increment = new BigDecimal(differenceInMiliSeconds(new_date)).divide(new BigDecimal(1000));
        dt = increment.doubleValue();
        double time = dt;// get size of the time step
        this.old_date = new Date(new_date);
        if(dt!= 0) updateController();

        // Ft = Ft + ((mass*g)/Math.cos(centralPos.getZ()));

        // IF WE WANT TO USE PID TO TRACK WE NEED TO CHECK WITH THE POSITION AT TIME T-1;

        Ft = Math.min(Ft, maxFtPropulsion);
        if(phase2Y){
            Ft = Math.max(Ft, ((mass*g)/Math.cos(centralPos.getZ())));
            //Ft = Math.max(Ft, (mass*g));
        }else{
            Ft = Math.max(Ft, 0);
        }

        Ft = Math.min(Ft, maxFtPropulsion);
        Fl = Math.min(Fl, maxFlPropulsion);
        Fl = Math.max(Fl, -maxFlPropulsion);


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

        if(stochasticWind) {
            if(advancedWind) applyAdvancedWindForce();
            else applySimpleWindForce();
        }



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

        totTime = totTime.add(increment);
        i++;
//        System.out.println("T: " + totTime + ", A: " + acceleration);
    }

    private void updateController() {
        double yError = centralPos.getY();
        double xError = centralPos.getX();
        double xError_vel = centralVel.getX();
        double tError = centralPos.getZ();

//        if(Math.abs(centralVel.getX())<0.3){
//            double t = totTime.doubleValue();
//            double v = centralVel.getX();
//            double a = oldAcceleration_smooth.getX();
//            double j = centralJerk.getX();
//            if(totTime.doubleValue()%0.5 == 0){
//                double tt = totTime.doubleValue();
//            }
//            if(totTime.doubleValue()>300.0){
//                double tt = totTime.doubleValue();
//            }
//
//        }
        if(!phase2X && totTime.doubleValue() > 50.0){
            double curxVel = centralVel.getX();
            double curxPos = centralPos.getX();

                //if(curxVel * oldxVel < 0 || (Math.abs(curxVel) < 0.05 && Math.abs(oldxVel) < Math.abs(curxVel))) {
                //if(Math.abs(centralVel.getX())<0.5 && Math.abs(oldAcceleration_smooth.getX())<0.5 && Math.abs(centralJerk.getX())<0.5){
//            System.out.println(curxPos*oldxPos < 0);
//            if(((Math.abs(curxVel) < 0.1|| curxVel * oldxVel < 0) && Math.abs(oldAcceleration_smooth.getX())<0.02)){
                //if(totTime.doubleValue()>320.0){
//            if((jerk_average_x.average()<0.01)||curxPos*oldxPos<0.0 ){
            if(centralPos.getY() < getCutoffClose_x){
                phase2X = true;
                phase2X_time = totTime.doubleValue();
//                printStatus();
            }
            oldxVel = curxVel;
            oldxPos = curxPos;
        }
//        if(totTime.doubleValue() == 300.0) {
//            System.out.println(totTime.doubleValue());
//            System.out.println("CP: " + centralPos.getX() );
//            System.out.println("CV: " + centralVel.getX() );
//            System.out.println("CA: " + acceleration.getX() );
//            System.out.println("CJ: " + centralJerk.getX() );
//        }
        if(!phase2Y && (centralPos.getY() < cutoffClose_y) && (centralVel.getY() > cutoffClose_yVel)) {
            phase2Y = true;
            phase2Y_time = totTime.doubleValue();
        }

        if(!phase3X && (centralPos.getY() < 5)) {
            phase3X = true;
            //pidXdiff_close.reduceTotalError(0.5);
        }


        double xImpulse;
        if(!phase2X){
            xImpulse = pidXdiff_far.calculateOutput(xError, xError_vel, dt);
            //xImpulse = pidXdiff_close.calculateOutput(xError, dt);
        }else {
            xImpulse = pidXdiff_far.calculateOutput(xError,xError_vel, dt);
            //xImpulse = pidXdiff_close.calculateOutput(xError, dt);
        }

        double tImpulse;
        if(!phase3 && ((centralPos.getY()-landedAltitude)/-centralVel.getY()) < cutoffFinal){
            phase3 = true;
            phase3_time = totTime.doubleValue();
//            printStatus();
        }

        if(phase3){
            tImpulse = -tError;
//            tImpulse = (0.2*xImpulse)-tError;
            Fl = pidRot_close.calculateOutput(tImpulse, dt);
        }else{
            tImpulse = xImpulse-tError;
            rotateController(tImpulse, pidRot_far);

        }
        double Ft_y;
        if(!phase2Y){
            Ft_y = pidYdiff_far.calculateOutput(yError, dt);
        }else{
            Ft_y = pidYdiff_close.calculateOutput(yError, dt);
        }
        double a = Ft/Math.cos(centralPos.getZ());
        double b = ((mass*g)/Math.cos(centralPos.getZ()));
        Ft_y = Ft_y/Math.cos(centralPos.getZ()) + ((mass*g)/Math.cos(centralPos.getZ()));



        //if(i%100 == 0 ) System.out.println("Y: " + Ft_y + ", X: " + Ft_x);



        if(Math.abs(centralPos.getZ())>0.8) {
            double Ft_x =  Math.abs(centralPos.getZ()) * Math.abs(pidXdiff_far_MT.calculateOutput(xError, dt));
            Ft = Math.max(Ft_y, Ft_x);
            //System.out.println(totTime + ",  used");
        }else{
            Ft = Ft_y;
        }




    }

    private void rotateController(double tImpulse, PIDcontroller pidRot){
        double Fl_main = pidRot.calculateOutput(tImpulse, dt);
        double tpos = centralPos.getZ();
        double tvel = centralVel.getZ();
//            if(i%10 == 0){
//                System.out.println("t: " + totTime.doubleValue());
//                System.out.println("t_pos: " + centralPos.getZ());
//                System.out.println("t_vel: " + centralVel.getZ());
//                System.out.println("t_acc: " + acceleration.getZ());
//                System.out.println("Fl main: " + Fl_main);
//            }
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



    public Vector3D getAccelerationSmooth() {
        return oldAcceleration_smooth;
    }


    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        centralPos = newCentralPos;

        if(newCentralPos.getY() < landedAltitude) {
            landed = true;
        }
    }

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
