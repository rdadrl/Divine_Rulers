package solarsystem;

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
    double meanWindSpeed;
    double currentWindSpeed;
    double windAcc;
    double dt; // timestep in seconds
    double Fl; // force thrusters
    double Ft; // force latereral thrusters

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
        this.date = new Date(date);
        this.centralVel = newCentralVel;
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

    public boolean getLanded() {
        return landed;
    }

}
