package solarsystem;

import utils.Date;
import utils.Vector3D;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RocketLanderOpenLoopCopy extends Rocket {


    private BigDecimal increment = new BigDecimal("0.01");

    private double timeCurrentPhase;
    private double timeAtCurrentPhase =0;
    private double theta;

    private boolean init_rotB;
    private boolean rot1_init;
    private boolean phase1_init;
    private boolean rot2_init;
    private boolean phase2_init;
    private boolean rot3_init;
    private boolean phase3_freefall_init;
    private boolean phase3_vertical_init;
    
    private boolean rot1_done;
    private boolean phase1_done;
    private boolean rot2_done;
    private boolean phase2_done;
    private boolean rot3_done;
    private boolean phase3_freefall_done;
    private boolean phase3_vertical_done;


    private double xLandingPad;

    public RocketLanderOpenLoopCopy(Vector3D centralPos,
                                    Vector3D centralVel, Date date){
        this.name = "OpenLoop Rocket_temp: ";
        this.xLandingPad=0;
        this.mass = dryMass + fuelMass;
        this.date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        Fl=0;
        Ft=0;
        acceleration = new Vector3D();
        //rotation to have x position equal 0
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date) {
        dt = differenceInSeconds(date); // get size of the time step
        this.date = new Date(date);
        if(!rot1_done) {
            // initialize rotation
            if(!rot1_init) {
                theta=0.785398; //45° in radians
                initRotation(theta);
                rot1_init = true;
            }
            timeAtCurrentPhase += dt;
            // first half rotation
            if(timeAtCurrentPhase < timeCurrentPhase/2d) {
                calculateMass();
            // second half rotation
            }else if(timeAtCurrentPhase < timeCurrentPhase) {
                if(!init_rotB) initRotationB();
                calculateMass();
            }else{
                Fl=0;
                double theta_doubledot=0;
                acceleration.setZ(theta_doubledot);
                rot1_done = true;
                init_rotB = false;
                timeAtCurrentPhase = 0;
            }
        } else if (!phase1_done) {
            if(!phase1_init) {
                initAccelerationPhase1();
                timeCurrentPhase = 10;
                phase1_init = true;
            }
            timeAtCurrentPhase += dt;
            if(timeAtCurrentPhase < timeCurrentPhase) {
                setXAccelerationPhase1();
            }else{
                phase1_done = true;
                timeAtCurrentPhase = 0;
            }
        } else if (!rot2_done) {
            if(!rot2_init) {
                theta=-1.5708; //45° in radians
                initRotation(theta);
                rot2_init = true;
            }
            timeAtCurrentPhase += dt;
            // first half rotation
            if(timeAtCurrentPhase < timeCurrentPhase/2d) {
                calculateMass();
                // second half rotation
            }else if(timeAtCurrentPhase < timeCurrentPhase) {
                if(!init_rotB) initRotationB();
                calculateMass();
            }else{
                Fl=0;
                double theta_doubledot=0;
                acceleration.setZ(theta_doubledot);
                rot2_done = true;
                init_rotB = false;
                timeAtCurrentPhase = 0;
            }
        } else if (!phase2_done) {
            if(!phase2_init) {
                initAccelerationPhase2();
                phase2_init = true;
            }
            timeAtCurrentPhase += dt;
            if(timeAtCurrentPhase < timeCurrentPhase) {
                setXAccelerationPhase2(theta);
            }else{
                phase2_done = true;
                timeAtCurrentPhase = 0;
            }
        } else if (!rot3_done) {
            if(!rot3_init) {
                theta=-1.5708; //45° in radians
                initRotation(theta);
                rot3_init = true;
            }
            timeAtCurrentPhase += dt;
            // first half rotation
            if(timeAtCurrentPhase < timeCurrentPhase/2d) {
                calculateMass();
                // second half rotation
            }else if(timeAtCurrentPhase < timeCurrentPhase) {
                if(!init_rotB) initRotationB();
                calculateMass();
            }else{
                Fl=0;
                double theta_doubledot=0;
                acceleration.setZ(theta_doubledot);
                rot3_done = true;
                init_rotB = false;
                timeCurrentPhase = 0;
            }
        } else if (!phase3_freefall_done) {
            if(!phase3_freefall_init) {
                initFreeFall();
                phase3_freefall_init = true;
            }
            timeAtCurrentPhase += dt;
            if(timeAtCurrentPhase > timeCurrentPhase) {
                phase3_freefall_done = true;
                timeCurrentPhase = 0;
            }
        }else if (!phase3_vertical_done) {
            if(!phase3_vertical_init) {
                initVerticalLanding();
                phase3_vertical_init = true;
            }
            timeCurrentPhase += dt;
            if(timeAtCurrentPhase < timeCurrentPhase) {
                calculateMass();
            }else{
                phase3_vertical_done = true;
                timeAtCurrentPhase = 0;
            }
        }
    }
    public void initRotation(double theta) {
        Fl=maxFlPropulsion;
        double theta_doubledot=4*maxFlPropulsion/J;
        acceleration.setZ(theta_doubledot);
        timeCurrentPhase = Math.sqrt(theta/theta_doubledot) * 2;
        System.out.println("time Rot: " +  timeCurrentPhase);
    }

    public void initRotationB() {
        double theta_doubledot=4*-maxFlPropulsion/J;
        acceleration.setZ(theta_doubledot);
    }

    public void initAccelerationPhase1() {
        double x_doubledot=-g;
        acceleration.setX(x_doubledot);
    }

    public void setXAccelerationPhase1(){
        Ft=2/Math.sqrt(2)*mass*g;
        calculateMass();
    }

    public void initAccelerationPhase2() {
        double xRocket=centralPos.getX();
        double x=xRocket-xLandingPad;
        double x_dot=centralVel.getX();
        timeCurrentPhase=2*x/x_dot;
        double x_doubledot=Math.pow(x_dot,2)/2*x;
        acceleration.setX(x_doubledot);
        System.out.println("time ph2: " +  timeCurrentPhase);
    }
    
    public void setXAccelerationPhase2(double theta){
        Ft=-mass*acceleration.getX()/Math.sin(theta);
        calculateMass();
    }

    public void initFreeFall() {
        double y_dot = centralVel.getY();
        double yRocket= centralPos.getY();
        timeCurrentPhase=(-y_dot+Math.sqrt(Math.pow(y_dot,2)-4*g/2.0*(-1/4.0)*yRocket))/g;
        double y_doubledot=g;
        acceleration.setY(y_doubledot);
    }

    public void initVerticalLanding() {
        double yRocket= centralPos.getY();
        double y_dot = centralVel.getY();
        double y_doubledot = acceleration.getY();
        timeCurrentPhase=-y_dot/y_doubledot;
        y_doubledot=Math.pow(y_dot,2)/2*yRocket;
        acceleration.setY(y_doubledot);
        Ft=y_doubledot+g;
    }

















}
