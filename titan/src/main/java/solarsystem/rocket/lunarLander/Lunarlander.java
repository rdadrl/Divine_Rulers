package solarsystem.rocket.lunarLander;

import solarsystem.rocket.Projectile;
import solarsystem.rocket.SpaceCraft;
import utils.Date;
import utils.vector.Vector3D;

import java.math.BigDecimal;

/**
 *
 *
 */
public abstract class Lunarlander extends SpaceCraft {
    protected double g = 1.352; // m / s^2
    protected double sidearea = 25.0; // m^2
    protected double airDensity = 5.0; // kg/m^2

    protected boolean stochasticWind;
    protected boolean advancedWind;
    protected double meanWindSpeed;
    protected double currentWindSpeed;
    protected double windAcc;


    protected final double Z0 = 0.0024;
    protected final double Z1 = 10;
    protected final double Z2 = 100;
    protected double A_conts = 0.22;

    protected Vector3D centralJerk = new Vector3D();

    protected boolean landed;
    protected double landedAltitude = 0;


    public Lunarlander() {
        super.dryMass = 5000; //kg
        super.fuelMass = 10000; // kg
        super.J =  100000; //moment of inertia kg m^2
        super.maxFtPropulsion = 44000; //newton
        super.maxFlPropulsion = 500; //newton
        super.thrusterImpulse = 3000; // ns/kg;
    }

    protected double differenceInSeconds(Date date) {

        return (date.getTimeInMillis() - this.old_date.getTimeInMillis())/1000D;
    }

    protected long differenceInMiliSeconds(Date date) {
        return (date.getTimeInMillis() - this.old_date.getTimeInMillis());
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

    protected void setCentralJerk(Vector3D oldAcc, Vector3D newACC, double dt){

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
            phaseFinished = true;
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

    protected void initializeSimpleWind() {
        // random wind speed form -10 to 10 meters per seconds;
        if(meanWindSpeed==-99) meanWindSpeed = (Math.random() * 20) - 10;
    }

    protected void initializeAdvancedWind() {
        if(meanWindSpeed==-99) meanWindSpeed = (Math.random() * 20) - 10;
        double V1 = meanWindSpeed;
        double V2 = V1 * (Math.log(Z2/Z0)/Math.log(Z1/Z0));
        //A_conts = Math.log(V2/V1)/Math.log(Z2/Z1);
    }


    protected void applySimpleWindForce() {
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

    protected void applyAdvancedWindForce() {
        double Z2 = centralPos.getY();
        double meanWindSpeedHeight;
        if(Z2 > 100.0) {
            meanWindSpeedHeight = meanWindSpeed * Math.pow((Z2/Z1), A_conts);
        }else{
            meanWindSpeedHeight = meanWindSpeed* (Math.log(Z2/Z0)/Math.log(Z1/Z0));
            if(meanWindSpeed > 0) {
                meanWindSpeedHeight = Math.max(0, meanWindSpeedHeight);
            }else  meanWindSpeedHeight = Math.min(0, meanWindSpeedHeight);
        }
        if(Double.isNaN(meanWindSpeedHeight)) meanWindSpeedHeight = 0;
        double windNoise = (Math.random() *0.1) +0.95;

        currentWindSpeed = meanWindSpeedHeight * windNoise;
        double windForce = sidearea * airDensity * currentWindSpeed;
        windAcc = windForce/mass;
        acceleration.setX(acceleration.getX() + windAcc);
    }

    protected double applySimpleWindAcc() {
        // random windnoise of -0.5 m to 0.5 m.
        double windNoise = (Math.random() *0.1) +0.95;
        currentWindSpeed = meanWindSpeed * windNoise;
        // force = area of impact * air density * windSpeed
        double windForce = sidearea * airDensity * currentWindSpeed;
        return windAcc = windForce/mass;
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
