package solarsystem.rocket;

import solarsystem.Trajectory;
import utils.vector.Vector3D;

import utils.Date;

import java.math.BigDecimal;

/**
 * A abstract spacecraft method. All spacecrafts should extend this class
 */
public abstract class SpaceCraft extends Projectile {
    protected BigDecimal totTime = new BigDecimal("0.0");
    protected double dryMass; //kg
    protected double fuelMass; // kg
    protected double fuelMass_t0; //kg
    protected double J; //moment of inertia kg m^2
    protected double maxFtPropulsion; //newton
    protected double maxFlPropulsion; //newton
    protected double thrusterImpulse; // ns/kg;
    protected double Ft;
    protected double Fl;
    protected double dt;
    protected Trajectory trajectory;

//    protected boolean phaseFinished;
    protected double g;




    public void calculateMass() {
        double totalThrust = Math.abs(Ft) + Math.abs(Fl);
        double fuelMassLoss = (totalThrust/thrusterImpulse) * dt;
        fuelMass = fuelMass - fuelMassLoss;
        if(fuelMass<0){
            System.out.println("Ran out of fuel!");
            System.exit(-1);
            fuelMass = 0;
        }
        mass = dryMass + fuelMass;
    }

    protected double differenceInSeconds(Date date) {

        return (date.getTimeInMillis() - this.current_date.getTimeInMillis())/1000D;
    }

    protected long differenceInMiliSeconds(Date date) {
        return (date.getTimeInMillis() - this.current_date.getTimeInMillis());
    }


    public Trajectory getTrajectory() { return trajectory; }
    public void setTrajectory(Trajectory trajectory) { this.trajectory = trajectory; }

    public boolean isTangentialToTarget(SpaceCraft spaceCraft) {

        Vector3D dUnit = spaceCraft.getCentralPos().unit();
        Vector3D vUnit = spaceCraft.getCentralVel().unit();
        return ((vUnit.dot(dUnit) < 0.05) && (vUnit.dot(dUnit) > -0.05));
    }

    public BigDecimal getTotTime() {
        return totTime;
    }

    public double getFuelMass() {
        return fuelMass;
    }

    public double getDryMass() {
        return dryMass;
    }

    public void setFuelMass_t0(double fuelMass_t0) {
        this.fuelMass_t0 = fuelMass_t0;
        this.fuelMass = fuelMass_t0;
        this.mass = fuelMass + dryMass;
    }

    public double getFuelMass_t0() {
        return fuelMass_t0;
    }

    public double getFuellUsed() {
        return fuelMass_t0 - fuelMass;
    }
}
