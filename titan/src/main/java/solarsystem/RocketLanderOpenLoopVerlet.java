package solarsystem;

import utils.Date;
import utils.Vector3D;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RocketLanderOpenLoopVerlet extends Rocket {


    private BigDecimal increment = new BigDecimal("0.01");

    private double timeCurrentPhase;
    private double timeAtCurrentPhase = 0;
    private double theta;
    private double requiredAcc;

    private boolean init_rotB;
    private boolean rot1_init;
    private boolean phase1_init;
    private boolean rot2_init;
    private boolean phase2_init;
    private boolean rot3_init;
    private boolean phase3_freefall_init;
    private boolean rot4_init;
    private boolean phase3_vertical_init;

    private boolean rot1_done;
    private boolean phase1_done;
    private boolean rot2_done;
    private boolean phase2_done;
    private boolean rot3_done;
    private boolean phase3_freefall_done;
    private boolean rot4_done;
    private boolean phase3_vertical_done;


    private double xLandingPad;

    public RocketLanderOpenLoopVerlet(Vector3D centralPos,
                                      Vector3D centralVel, Date date) {
        this.landedAltitude = 0;
        this.name = "OpenLoop Rocket_temp: ";
        this.xLandingPad = 0;
        this.mass = dryMass + fuelMass;
        this.old_date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        Fl = 0;
        Ft = 0;
        acceleration = new Vector3D();
        //rotation to have x position equal 0
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date) {
        dt = differenceInSeconds(date); // get size of the time step
        this.old_date = new Date(date);
        if (!rot1_done) {
            // initialize rotation
            if(!rot1_init) {
                System.out.println("Rotation started");
                theta=0.785398; //45° in radians
                initRotation(theta);
                updateAcceleration();
                rot1_init = true;
            }
            timeAtCurrentPhase += dt;
            // first half rotation
            if (timeAtCurrentPhase < timeCurrentPhase / 2d) {
                calculateMass();
                // second half rotation
            } else if (timeAtCurrentPhase < timeCurrentPhase) {
                if (!init_rotB) {
                    initRotationB();
                    updateAcceleration();
                    init_rotB = true;
                }
                calculateMass();
            } else {
                Fl = 0;
                updateAcceleration();
                rot1_done = true;
                System.out.println("rotation done");
                init_rotB = false;
                timeAtCurrentPhase = 0;
            }
        } else if (!phase1_done) {
            if (!phase1_init) {
                System.out.println("Phase 1 x Started");
                initAccelerationPhase1();
                updateAcceleration();
                double a=(1/2.0)*acceleration.getX();
                System.out.println("Acc Phase 1:"+a);
                double b=centralVel.getX();
                System.out.println("Vel:"+b);
                double c=centralPos.getX();
                System.out.println("Pos:"+c);
                double determinant = Math.pow(b,2) - 4*a*c;
                System.out.println("determinant:"+c);
                if(determinant > 0){
                    double r1=(-b+Math.sqrt(determinant))/(2*a);
                    double r2=(-b-Math.sqrt(determinant))/(2*a);
                    if(r1>0){
                        timeCurrentPhase=r1/2;
                    }
                    else{
                        timeCurrentPhase=r2/2;
                    }
                }else if (determinant == 0){
                    timeCurrentPhase=-b/(4*a);
                }
                System.out.println("Time Phase 1:"+timeCurrentPhase);
                phase1_init = true;
            }
            if (timeAtCurrentPhase < timeCurrentPhase) {
                calculateMass();
                timeAtCurrentPhase += dt;
            } else {
                phase1_done = true;
                timeAtCurrentPhase = 0;
                Ft = 0;
                updateAcceleration();
            }
        } else if (!rot2_done) {
            if (!rot2_init) {
                theta = -1.5708; //-90° in radians
                System.out.println("Rotation Started");
                initRotation(theta);
                updateAcceleration();
                rot2_init = true;
            }
            timeAtCurrentPhase += dt;
            // first half rotation
            if (timeAtCurrentPhase < timeCurrentPhase / 2d) {
                calculateMass();
                // second half rotation
            } else if (timeAtCurrentPhase < timeCurrentPhase) {
                if (!init_rotB) {
                    initRotationB();
                    updateAcceleration();
                    init_rotB = true;
                }
                calculateMass();
            } else {
                Fl = 0;
                updateAcceleration();
                rot2_done = true;
                System.out.println("rotation done");
                init_rotB = false;
                timeAtCurrentPhase = 0;
            }
        } else if (!phase2_done) {
            if (!phase2_init) {
                theta = centralPos.getZ();
                System.out.println("Phase 2 x Started");
                initAccelerationPhase2(theta);
                updateAcceleration();
                phase2_init = true;
            }
            timeAtCurrentPhase += dt;
            if (timeAtCurrentPhase < timeCurrentPhase) {
                calculateMass();
            } else {
                phase2_done = true;
                timeAtCurrentPhase = 0;
                Ft = 0;
                acceleration.setX(0);
            }
        } else if (!rot3_done) {
            if (!rot3_init) {
                theta = 0.785398; //45° in radians
                System.out.println("Rotation Started");
                initRotation(theta);
                updateAcceleration();
                rot3_init = true;
            }
            timeAtCurrentPhase += dt;
            // first half rotation
            if (timeAtCurrentPhase < timeCurrentPhase / 2d) {
                calculateMass();
                // second half rotation
            } else if (timeAtCurrentPhase < timeCurrentPhase) {
                if (!init_rotB) {
                    initRotationB();
                    updateAcceleration();
                    init_rotB = true;
                }
                calculateMass();
            } else {
                Fl = 0;
                theta=centralVel.getZ();
                updateAcceleration();
                rot3_done = true;
                System.out.println("rotation done");
                init_rotB = false;
                timeAtCurrentPhase = 0;
            }
        } else if (!phase3_freefall_done) {
            if (!phase3_freefall_init) {
                System.out.println("Free Fall Started");
                initFreeFall();
                updateAcceleration();
                phase3_freefall_init = true;
            }
            timeAtCurrentPhase += dt;
            if (timeAtCurrentPhase > timeCurrentPhase) {
                phase3_freefall_done = true;
                timeAtCurrentPhase = 0;
            }
        } else if (!rot4_done) {
            if (!rot4_init) {
                theta = -(centralPos.getZ()+0.05); //trying to correct as much as possible angle
                System.out.println("Rotation Started");
                initRotation(theta);
                updateAcceleration();
                rot4_init = true;
            }
            timeAtCurrentPhase += dt;
            // first half rotation
            if (timeAtCurrentPhase < timeCurrentPhase / 2d) {
                calculateMass();
                // second half rotation
            } else if (timeAtCurrentPhase < timeCurrentPhase) {
                if (!init_rotB) {
                    initRotationB();
                    updateAcceleration();
                    init_rotB = true;
                }
                calculateMass();
            } else {
                Fl = 0;
                updateAcceleration();
                rot4_done = true;
                System.out.println("rotation done");
                init_rotB = false;
                timeAtCurrentPhase = 0;
            }
        }
        else if (!phase3_vertical_done) {
            theta=centralPos.getZ();
            if (!phase3_vertical_init) {
                System.out.println("Landing Started");
                initVerticalLanding();
                updateAcceleration();
                phase3_vertical_init = true;
            }
            timeAtCurrentPhase += dt;

            if (timeAtCurrentPhase < timeCurrentPhase) {
                calculateMass();
                theta=centralPos.getZ();
                Ft=(requiredAcc+g)*mass/Math.cos(centralPos.getZ());
                if(centralVel.getY()>0){
                    Ft=0;
                }
                Fl=-Ft*Math.tan(centralPos.getZ());
                updateAcceleration();
            }
            /*else{
                phase3_vertical_done=true;
                timeLanding=timeCurrentPhase;
            }*/

            else {
                phase3_vertical_done = true;
                timeAtCurrentPhase = 0;
                Ft=0;
                Fl=0;
                updateAcceleration();
                landed=true;
            }
        }
        totTime = totTime.add(increment);
    }

    public void updateAcceleration(){
        double xacc = ((Fl * Math.cos(theta) - Ft * Math.sin(theta))/mass);
        double yacc = ((Fl * Math.sin(theta) + Ft * Math.cos(theta))/mass) - g;
        double tacc = Fl*4/J;


        acceleration.setX(xacc);
        acceleration.setY(yacc);
        acceleration.setZ(tacc);
    }

    public void initRotation(double theta) {
        if(theta>0){
            Fl = maxFlPropulsion;
        }
        else{
            Fl=-maxFlPropulsion;
        }
        double theta_doubledot = 4 * maxFlPropulsion / J;
        timeCurrentPhase = Math.sqrt(Math.abs(theta)/theta_doubledot) * 2;
        System.out.println("time Rot: " + timeCurrentPhase);
    }

    public void initRotationB() {
        Fl=-Fl;
    }

    public void initAccelerationPhase1() {
        Ft = 2 / Math.sqrt(2) * mass * g;
    }

    public void initAccelerationPhase2(double theta) {
        timeCurrentPhase=Math.abs(centralPos.getX()*2/(centralVel.getX()));
        double x_doubledot=-centralVel.getX()/timeCurrentPhase;
        Ft = -mass*x_doubledot/Math.sin(theta);
    }

    public void initFreeFall() {
        double y_dot = g;
        double yRocket = centralPos.getY()*3/4.0;
        timeCurrentPhase = (-y_dot + Math.sqrt(Math.pow(y_dot, 2) - (4 * g / 2.0) * (-1 / 4.0) * yRocket)) / g;
    }

    public void initVerticalLanding() {
        double yRocket = centralPos.getY();
        double y_dot = centralVel.getY();
        double y_doubledot = Math.pow(y_dot, 2) / (2 * yRocket)*1.005055;
        requiredAcc=y_doubledot;
        System.out.println("Perfect acc:"+y_doubledot);
        timeCurrentPhase=Math.abs(centralPos.getY()*2/(centralVel.getY()))*1.1;
        Ft=(y_doubledot+g)*mass/Math.cos(centralPos.getZ());
        Fl=-Ft*Math.tan(centralPos.getZ());
        System.out.println("Ft of vertical: "+Ft);
        System.out.println("Time Landing:"+timeCurrentPhase);
    }

}
