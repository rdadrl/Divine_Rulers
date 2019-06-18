package solarsystem;

import solarsystem.CelestialObject;

public class Trajectory {
    protected double a; // semi major axis;
    protected double e; // eccentricity
    protected double i; // departureInclination
    protected double l; // longitude
    protected double w; // periphelon
    protected double o; // change of ascending node
    protected double w_a ; // argument of the periphelion
    protected double T; // Period
    protected CelestialObject targetBody;

    public Trajectory(CelestialObject targetBody, CelestialObject centralBody, double semiMajorAxis, double period,
                     double eccentricity, double departureInclination, double longitude, double periphelion) {
        this.targetBody = targetBody; this.a = semiMajorAxis; this.T = period;
    }

    public Trajectory(CelestialObject targetBody, double semiMajorAxis, double period){
        this.targetBody = targetBody; this.a = semiMajorAxis; this.T = period;
    }

    //public void setTarget(CelestialObject target) { this.target = target; }
    //public CelestialObject getTargetBody() { return target; }

    public void setSemiMajorAxis(double semiMajorAxis) { this.a = semiMajorAxis; }
    public double getSemiMajorAxis() { return this.a; }

    public void setPeriod(double period) { this.T = period; }
    public double getPeriod() { return this.T; }

    public CelestialObject getTargetBody() {
        return targetBody;
    }
}
