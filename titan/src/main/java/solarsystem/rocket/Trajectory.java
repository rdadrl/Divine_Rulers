package solarsystem.rocket;

import solarsystem.CelestialObject;

public class Trajectory {
    private CelestialObject target;
    private double semiMajorAxis; // TODO: Check scale
    private double period; // TODO: Check scale

    public Trajectory(CelestialObject target, double semiMajorAxis, double period) {
        this.target = target; this.semiMajorAxis = semiMajorAxis; this.period = period;
    }

    public void setTarget(CelestialObject target) { this.target = target; }
    public CelestialObject getTarget() { return target; }

    public void setSemiMajorAxis(double semiMajorAxis) { this.semiMajorAxis = semiMajorAxis; }
    public double getSemiMajorAxis() { return semiMajorAxis; }

    public void setPeriod(double period) { this.period = period; }
    public double getPeriod() { return period; }
}
