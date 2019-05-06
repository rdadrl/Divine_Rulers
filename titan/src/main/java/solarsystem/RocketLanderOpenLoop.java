package solarsystem;

import utils.Date;
import utils.Vector3D;

import java.math.BigDecimal;

public class RocketLanderOpenLoop extends Projectile {
    private double dryMass = 5000; //kg
    private double fuelMass = 10000; // kg
    private double mass;
    private double J = 100000; //moment of inertia kg m^2
    private double maxFtPropulsion = 44000; //newton
    private double maxFlPropulsion = 500; //newton
    private double thrusterImpulse = 3000; // ns/kg;
    private double g = 1.352; // m / s^2

    private BigDecimal totTime = new BigDecimal("0.0");
    private BigDecimal increment = new BigDecimal("0.01");
    private double dt; // timestep in seconds
    private double Fl; // force thrusters
    private double Ft; // force latereral thrusters

    private double xLandingPad;

    public RocketLanderOpenLoop(double xLandingPad){
        this.xLandingPad=xLandingPad;
        Fl=0;
        Ft=0;
        acceleration = new Vector3D();
        //rotation to have x position equal 0
        double theta=0.785398; //45° in radians
        makeRotation(theta);
        acceleration.setX(-g);
        //moving towards x=0
        setXAccelerationPhase1(10);
        //rotation to have x velocity equal 0
        theta=-1.5708; //-90° in radians
        makeRotation(theta);
        setXAccelerationPhase2(theta);
        //rotation for vertical landing
        theta=0.785398; //40° in radians
        makeRotation(theta);
        verticalLanding();
    }

    public void makeRotation(double theta){
        Fl=maxFlPropulsion;
        double theta_doubledot=4*maxFlPropulsion/J;
        acceleration.setZ(theta_doubledot);
        double time_halfway = Math.sqrt(theta/theta_doubledot);
        double t0=date.getTimeInMillis();
        double tnow=date.getTimeInMillis();
        double tnowstored;
        while((tnow-t0) < time_halfway){
            tnowstored=tnow;
            tnow=date.getTimeInMillis();
            dt=tnow-tnowstored;
            calculateMass();
        }
        //when halftime reached
        theta_doubledot=4*-maxFlPropulsion/J;
        acceleration.setZ(theta_doubledot);
        t0=date.getTimeInMillis();
        tnow=date.getTimeInMillis();
        while((tnow-t0)< time_halfway){
            tnowstored=tnow;
            tnow=date.getTimeInMillis();
            dt=tnow-tnowstored;
            calculateMass();
        }
        //when other halftime reached
        theta_doubledot=0;
        acceleration.setZ(theta_doubledot);


        // speed formula : double theta_dot=theta_dot+theta_doubledot*t;
    }

    public void setXAccelerationPhase1(double t){
        double x_doubledot=-g;
        acceleration.setX(x_doubledot);
        double t0=date.getTimeInMillis();
        double tnow=date.getTimeInMillis();
        double tnowstored;
        while((tnow-t0)<t){
            Ft=2/Math.sqrt(2)*mass*g;
            tnowstored=tnow;
            tnow=tnow-tnowstored;
            dt=tnow-tnowstored;
            calculateMass();
        }
    }

    public void setXAccelerationPhase2(double theta){
        double xRocket=this.getCentralPos().getX();
        double x=xRocket-xLandingPad;
        double x_dot=this.getCentralVel().getX();
        double timeNeeded=2*x/x_dot;
        double t0=date.getTimeInMillis();
        double tnow=date.getTimeInMillis();
        double tnowstored;
        while ((tnow-t0)<timeNeeded){
            double x_doubledot=Math.pow(x_dot,2)/2*x;
            Ft=-mass*x_doubledot/Math.sin(theta);
            tnowstored=tnow;
            tnow=date.getTimeInMillis();
            dt=tnow-tnowstored;
            calculateMass();
        }
    }

    public void verticalLanding(){
        double y_dot=this.getCentralVel().getY();
        double yRocket=this.getCentralPos().getY();
        double tFreeFall=(-y_dot+Math.sqrt(Math.pow(y_dot,2)-4*g/2.0*(-1/4.0)*yRocket))/g;
        double y_doubledot=g;
        acceleration.setY(y_doubledot);
        double t0=date.getTimeInMillis();
        double tnow=date.getTimeInMillis();
        while ((tnow-t0)<tFreeFall){
            tnow=date.getTimeInMillis();
        }
        double timeNeeded=-y_dot/y_doubledot;
        y_doubledot=Math.pow(y_dot,2)/2*yRocket;
        acceleration.setY(y_doubledot);
        Ft=y_doubledot+g;
        t0=date.getTimeInMillis();
        tnow=date.getTimeInMillis();
        double tnowstored;
        while ((tnow-t0)<timeNeeded){
            tnowstored=tnow;
            tnow=date.getTimeInMillis();
            dt=tnow-tnowstored;
            calculateMass();
        }

    }

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

    private double differenceInSeconds(Date date) {
        return (date.getTimeInMillis() - this.date.getTimeInMillis())/1000D;
    }

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
}
