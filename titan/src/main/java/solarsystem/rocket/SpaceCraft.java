package solarsystem.rocket;

import solarsystem.CelestialObject;
import utils.vector.Vector3D;

import solarsystem.Planet;
import utils.Date;

import java.math.BigDecimal;

/**
 *
 *
 */
public abstract class SpaceCraft extends Projectile {
    protected BigDecimal totTime = new BigDecimal("0.0");
    protected double dryMass; //kg
    protected double fuelMass; // kg
    protected double J; //moment of inertia kg m^2
    protected double maxFtPropulsion; //newton
    protected double maxFlPropulsion; //newton
    protected double thrusterImpulse; // ns/kg;
    protected double Ft;
    protected double Fl;
    protected double dt;
    protected Trajectory trajectory;

    protected boolean phaseFinished;
    protected double g;


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

    protected double differenceInSeconds(Date date) {

        return (date.getTimeInMillis() - this.old_date.getTimeInMillis())/1000D;
    }


    public boolean phaseFinished(){
        return phaseFinished;
    }

    public Trajectory getTrajectory() { return trajectory; }
    public void setTrajectory(Trajectory trajectory) { this.trajectory = trajectory; }

    public boolean isTangentialToTarget(SpaceCraft spaceCraft) {

        Vector3D dUnit = (spaceCraft.getCentralPos().substract(spaceCraft.getTrajectory().getTarget().getCentralPos())).unit();
        Vector3D vUnit = spaceCraft.getCentralVel().unit();
        return ((vUnit.dot(dUnit) < 0.05) && (vUnit.dot(dUnit) > -0.05));
    }

    public BigDecimal getTotTime() {
        return totTime;
    }
}
