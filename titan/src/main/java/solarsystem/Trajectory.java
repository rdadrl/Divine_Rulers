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
    protected double centralBodyMass; // Mass of central body
    protected double centralBodySphereOfInfluence;

    public Trajectory(CelestialObject centralBody, double semiMajorAxis, double period,
                     double eccentricity, double departureInclination, double longitude, double periphelion) {
        this.a = semiMajorAxis; this.T = period;
    }

    public Trajectory(double centralBodyMass, double centralBodySphereOfInfluence, double semiMajorAxis, double period) {
        this.centralBodyMass = centralBodyMass; this.centralBodySphereOfInfluence = centralBodySphereOfInfluence; this.a = semiMajorAxis; this.T = period;
    }

    //public void setTarget(CelestialObject target) { this.target = target; }
    //public CelestialObject getTarget() { return target; }

    public void setSemiMajorAxis(double semiMajorAxis) { this.a = semiMajorAxis; }
    public double getSemiMajorAxis() { return this.a; }

    public void setPeriod(double period) { this.T = period; }
    public double getPeriod() { return this.T; }

    public double getCentralBodySphereOfInfluence() { return centralBodySphereOfInfluence; }
    public double getCentralBodyMass() { return centralBodyMass; }

}
