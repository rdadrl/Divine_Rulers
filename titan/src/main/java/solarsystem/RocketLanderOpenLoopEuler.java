package solarsystem;

import utils.Date;
import utils.Vector3D;

import java.math.BigDecimal;

public class RocketLanderOpenLoopEuler extends Projectile {

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
    private double Fl; // force thrusters
    private double Ft; // force latereral thrusters
    private double dt=0.1; // timestep in seconds


    private double xLandingPad;

    /*xLandingPad is x position of LandingPad
    The y coordinate is assumed to be 0 as the landing pad is on Titan ground
    xRocket is starting x position of the Rocket
     */
    public RocketLanderOpenLoopEuler(Vector3D centralPos, Vector3D centralVel, double xLandingPad ){
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.xLandingPad=xLandingPad;
        //We start with all thrusters shut down
        Fl=0;
        Ft=0;
        acceleration = new Vector3D();
        //rotation to have x position equal 0
        double theta;
        double xRocket=centralPos.getX();
        //Angle depends if landing pad is on the left or right of the rocket
        if(xRocket>xLandingPad){
            theta=0.785398; //45° in radians
        }
        else{
            theta=-0.785398;
        }
        makeRotation(theta);
        //moving towards x=0
        setXAccelerationPhase1(10);
        //rotation to have x velocity equal 0
        if(xRocket>xLandingPad){
            theta=-1.5708; //-90° in radians
        }
        else {
            theta=1.5708;
        }
        makeRotation(theta);
        setXAccelerationPhase2(theta);
        //rotation for vertical landing
        if(xRocket>xLandingPad){
            theta=0.785398; //40° in radians
        }
        else{
            theta=-0.785398;
        }
        makeRotation(theta);
        verticalLanding();
    }

    /*Rotation is in 2 phases, 1st phase to rotate the rocket in the desired angle
    and second phase to stop at that angle. We use opposite side thrusters to do so
     */
    public void makeRotation(double theta){
        System.out.println("Rotation Started");
        //side Thruster Updated
        Fl=maxFlPropulsion;
        //acceleration updated
        double theta_doubledot;
        //TODO:EMMA (myself) double check this
        if(theta>0){
            theta_doubledot=4*maxFlPropulsion/J;
        }
        else{
            theta_doubledot=-4*maxFlPropulsion/J;
        }
        acceleration.setZ(theta_doubledot);
        //time of needed to execute each phase
        double time_halfway = Math.sqrt(Math.abs(theta)/Math.abs(theta_doubledot));
        //Updating position, velocity and mass during first phase of rotation
        int y=0;
        for(double i=0;i<=time_halfway;i+=dt){
            update();
            calculateMass();
            if(y%10==0){
                System.out.println("X:"+centralPos.getX());
                System.out.println("Y:"+centralPos.getY());
                System.out.println("Z:"+centralPos.getZ());
                System.out.println("Vx:"+centralVel.getX());
                System.out.println("Vy:"+centralVel.getY());
                System.out.println("Vz:"+centralVel.getZ());
                System.out.println("Ax:"+acceleration.getX());
                System.out.println("Ay:"+acceleration.getY());
                System.out.println("Az:"+acceleration.getZ());
                System.out.println();
            }
            y+=1;
        }
        //when halftime reached
        //acceleration updated
        theta_doubledot=-theta_doubledot;
        acceleration.setZ(theta_doubledot);
        //Updating position, velocity and mass during second phase of rotation
        y=0;
        for(double i=0;i<=time_halfway;i+=dt){
            update();
            calculateMass();
            if(y%10==0){
                System.out.println("X:"+centralPos.getX());
                System.out.println("Y:"+centralPos.getY());
                System.out.println("Z:"+centralPos.getZ());
                System.out.println("Vx:"+centralVel.getX());
                System.out.println("Vy:"+centralVel.getY());
                System.out.println("Vz:"+centralVel.getZ());
                System.out.println("Ax:"+acceleration.getX());
                System.out.println("Ay:"+acceleration.getY());
                System.out.println("Az:"+acceleration.getZ());
                System.out.println();
            }
            y+=1;
        }
        //when final rotation time reached
        //Thrusters
        Fl=0;
        theta_doubledot=0;
        acceleration.setZ(theta_doubledot);
        System.out.println("Rotation Done");

        // speed formula : double theta_dot=theta_dot+theta_doubledot*t;
    }

    //first phase for the rocket getting closer to the x coordinate, the rocket accelerates
    public void setXAccelerationPhase1(double t){
        System.out.println("Phase 1 X displacement started");
        //updating acceleration
        double x_doubledot=-g;
        acceleration.setX(x_doubledot);
        //updating mass, main thruster, velocity and position during the first phase of move
        int y=0;
        for(double i=0;i<=t;i+=dt){
            Ft=2/Math.sqrt(2)*mass*g;
            update();
            calculateMass();
            if(y%10==0){
                System.out.println("X:"+centralPos.getX());
                System.out.println("Y:"+centralPos.getY());
                System.out.println("Z:"+centralPos.getZ());
                System.out.println("Vx:"+centralVel.getX());
                System.out.println("Vy:"+centralVel.getY());
                System.out.println("Vz:"+centralVel.getZ());
                System.out.println("Ax:"+acceleration.getX());
                System.out.println("Ay:"+acceleration.getY());
                System.out.println("Az:"+acceleration.getZ());
                System.out.println();
            }
            y+=1;
        }
        System.out.println("Phase 1 X displacement done");
        //Phase done we turn off engines
        Ft=0;
        acceleration.setX(0);
    }

    /*first phase for the rocket getting closer to the x coordinate, the rocket deccerlerates
    to arrive at x=0 with Vx=0
     */
    public void setXAccelerationPhase2(double theta){
        System.out.println("Phase 2 X displacement started");
        double xRocket=centralPos.getX();
        double x=xRocket-xLandingPad;
        double x_dot=centralVel.getX();
        double timeNeeded=2*x/Math.abs(x_dot);
        double x_doubledot=Math.pow(x_dot,2)/(2*x);
        acceleration.setX(x_doubledot);
        int y=0;
        for(double i=0;i<=timeNeeded;i+=dt){
            Ft=-mass*x_doubledot/Math.sin(theta);
            update();
            calculateMass();
            if(y%10==0){
                System.out.println("X:"+centralPos.getX());
                System.out.println("Y:"+centralPos.getY());
                System.out.println("Z:"+centralPos.getZ());
                System.out.println("Vx:"+centralVel.getX());
                System.out.println("Vy:"+centralVel.getY());
                System.out.println("Vz:"+centralVel.getZ());
                System.out.println("Ax:"+acceleration.getX());
                System.out.println("Ay:"+acceleration.getY());
                System.out.println("Az:"+acceleration.getZ());
                System.out.println();
            }
            y+=1;
        }
        System.out.println("Phase 2 X displacement done");
        //Shut Down engines after phase done
        Ft=0;
        acceleration.setX(0);
    }

    public void verticalLanding(){
        System.out.println("Free fall started");
        double y_dot = centralVel.getY();
        double yRocket= centralPos.getY();
        double tFreeFall=(-y_dot+Math.sqrt(Math.pow(y_dot,2)-4*g/2.0*(-1/4.0)*yRocket))/g;
        double y_doubledot=-g;
        acceleration.setY(y_doubledot);
        int y=0;
        for(double i=0;i<=tFreeFall;i+=dt){
            update();
            if(y%10==0){
                System.out.println("X:"+centralPos.getX());
                System.out.println("Y:"+centralPos.getY());
                System.out.println("Z:"+centralPos.getZ());
                System.out.println("Vx:"+centralVel.getX());
                System.out.println("Vy:"+centralVel.getY());
                System.out.println("Vz:"+centralVel.getZ());
                System.out.println("Ax:"+acceleration.getX());
                System.out.println("Ay:"+acceleration.getY());
                System.out.println("Az:"+acceleration.getZ());
                System.out.println();
            }
            y+=1;
        }
        System.out.println("Final stage started");
        y_dot=centralVel.getY();
        yRocket=centralPos.getY();
        y_doubledot=Math.pow(y_dot,2)/(2*yRocket);
        double timeNeeded=Math.abs(y_dot/y_doubledot);
        System.out.println("Time"+timeNeeded);
        System.out.println("Vy"+y_dot);
        System.out.println("YRocket"+yRocket);
        acceleration.setY(y_doubledot);
        Ft=y_doubledot+g;
        y=0;
        for(double i=0;i<=timeNeeded;i+=dt){
            update();
            calculateMass();
            if(y%10==0){
                System.out.println("X:"+centralPos.getX());
                System.out.println("Y:"+centralPos.getY());
                System.out.println("Z:"+centralPos.getZ());
                System.out.println("Vx:"+centralVel.getX());
                System.out.println("Vy:"+centralVel.getY());
                System.out.println("Vz:"+centralVel.getZ());
                System.out.println("Ax:"+acceleration.getX());
                System.out.println("Ay:"+acceleration.getY());
                System.out.println("Az:"+acceleration.getZ());
                System.out.println();
            }
            y+=1;
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

    //Euler to get Velocity and Position
    public void update() {
		centralPos = centralPos.add(centralVel.scale(dt));
	    centralVel = centralVel.add(acceleration.scale(dt));
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

    //TODO:ARDA help me for runnable thread launcher shit :D

    public static void main (String[] args){
        RocketLanderOpenLoopEuler rocket=new RocketLanderOpenLoopEuler(new Vector3D(600,1500,0),
                new Vector3D(0,0,0),0);
    }
}
