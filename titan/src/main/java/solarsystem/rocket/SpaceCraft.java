package solarsystem.rocket;

import java.math.BigDecimal;

/**
 *
 *
 */
public abstract class SpaceCraft extends Projectile {
    protected BigDecimal totTime = new BigDecimal("0.0");
    protected double dryMass; //kg
    protected double fuelMass; // kg
    protected double mass;
    protected double J; //moment of inertia kg m^2
    protected double maxFtPropulsion; //newton
    protected double maxFlPropulsion; //newton
    protected double thrusterImpulse; // ns/kg;
    protected double Ft;
    protected double Fl;
    protected double dt;


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
}
