package solarsystem;

import physics.VerletVelocity;
import utils.Date;
import utils.Vector3D;

import java.math.BigDecimal;

/**
 *
 *
 */
public abstract class Rocket extends Projectile{
    BigDecimal totTime = new BigDecimal("0.0");
    double dryMass = 5000; //kg
    double fuelMass = 10000; // kg
    double mass;
    double J = 100000; //moment of inertia kg m^2
    double maxFtPropulsion = 44000; //newton
    double maxFlPropulsion = 500; //newton
    double thrusterImpulse = 3000; // ns/kg;
    double g = 1.352; // m / s^2
    double sidearea = 25.0; // m^2
    double airDensity = 5.0; // kg/m^2

    boolean stochasticWind;
    boolean advancedWind;
    double meanWindSpeed;
    double currentWindSpeed;
    double windAcc;
    double dt; // timestep in seconds
    double Fl; // force thrusters
    double Ft; // force latereral thrusters

    final double Z0 = 0.5;
    final double Z1 = 10;
    final double Z2 = 100;
    double A_conts;

    Vector3D centralJerk = new Vector3D();

    boolean landed;
    double landedAltitude = 0;


    double differenceInSeconds(Date date) {
        return (date.getTimeInMillis() - this.date.getTimeInMillis())/1000D;
    }

    public double getFuelMass() {
        return fuelMass;
    }
    public double getMainThrusterForceAsPercentage() {
        return Ft/maxFtPropulsion;
    }

    @Override
    public void checkColisions() {
    }

    @Override
    public void setCentralVel(Vector3D newCentralVel) {
        this.centralVel = newCentralVel;
    }

    void setCentralJerk(Vector3D oldAcc, Vector3D newACC, double dt){

        centralJerk = (newACC.substract(oldAcc).scale(1/dt));
//        System.out.println("TotTime: " + totTime);
//        System.out.println("OCV: " + oldAcc.getX());
//        System.out.println("NCV: " + newACC.getX());
//        System.out.println("SUBTR: " + newACC.substract(oldAcc).getX());
//        System.out.println("CJ: " + centralJerk.getX() + "\n");
    }

    public Vector3D getCentralJerk() {
        return centralJerk;
    }

    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        centralPos = newCentralPos;
        if(newCentralPos.getY() < landedAltitude) {
            printStatus();
            landed = true;
            System.out.println("Landed");
            //System.exit(-1);
        }
    }

    @Override
    public void initializeCartesianCoordinates(Date date){}


    public void calculateMass() {
        double totalThrust = Math.abs(Ft) + Math.abs(Fl);
        double fuelMassLoss = (totalThrust/thrusterImpulse) * dt;
        fuelMass = fuelMass - fuelMassLoss;
        if(fuelMass<0){
            System.out.println("Ran out of fuel!");
            fuelMass = 0;
        }
        mass = dryMass + fuelMass;
    }

    void initializeSimpleWind() {
        // random wind speed form -10 to 10 meters per seconds;
        if(meanWindSpeed==-99) meanWindSpeed = (Math.random() * 20) - 10;
    }

    void initializeAdvancedWind() {
        if(meanWindSpeed==-99) meanWindSpeed = (Math.random() * 20) - 10;
        double V1 = meanWindSpeed;
        double V2 = V1* (Math.log(Z2/Z0)/Math.log(Z1/Z0));
        A_conts = Math.log(V2/V1)/Math.log(Z2/Z1);
    }


    void applySimpleWindForce() {
        // random windnoise of -0.5 m to 0.5 m.
        double windNoise = (Math.random() *0.1) +0.95;
        currentWindSpeed = meanWindSpeed * windNoise;
        // force = area of impact * air density * windSpeed
        double windForce = sidearea * airDensity * currentWindSpeed;
        windAcc = windForce/mass;
        //System.out.println("Acc: " + acceleration.getX());
        //System.out.println("Wind_acc: " + windAcc);
        //System.out.println();
        acceleration.setX(acceleration.getX() + windAcc);
    }

    void applyAdvancedWindForce() {
        double Z2 = centralPos.getY();
        double meanWindSpeedHeight;
        if(Z2 > 100.0) {
            meanWindSpeedHeight = meanWindSpeed * Math.pow((Z2/Z1), A_conts);
        }else{
            meanWindSpeedHeight = meanWindSpeed* (Math.log(Z2/Z0)/Math.log(Z1/Z0));
        }
        if(Double.isNaN(meanWindSpeedHeight)) meanWindSpeedHeight = 0;
        double windNoise = (Math.random() *0.1) +0.95;

        currentWindSpeed = meanWindSpeedHeight * windNoise;
        double windForce = sidearea * airDensity * currentWindSpeed;
        windAcc = windForce/mass;
        acceleration.setX(acceleration.getX() + windAcc);
    }


    public boolean getLanded() {
        return landed;
    }

    public boolean fuelLeft() {return fuelMass>0;}

    public double getTotalTime(){
        return totTime.doubleValue();
    }


    public double getSideThrusterForce() {
        return Fl;
    }

    public double getMainThrusterForce() {
        return Ft;
    }

    public double getCurrentWindSpeed() {return currentWindSpeed;
    }

    public double getMeanWindSpeed() {
        return meanWindSpeed;
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
                "t-acc: " + this.acceleration.getZ() + "\n" +
                "mean wind: " + meanWindSpeed + "\n\n");
    }

}
