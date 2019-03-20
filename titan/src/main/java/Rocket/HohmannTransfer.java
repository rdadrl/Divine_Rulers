package Rocket;

import solarsystem.CelestialObjects;

public class HohmannTransfer {

    private CelestialObjects p1; // Planet 1
    private CelestialObjects p2; // Planet 2
    private CelestialObjects c1; // Central planet

    private double time; // Time in transfer
    private double a1; // semimajor axis of p1
    private double a2; // semimajor axis of p2
    private double a;
    private double u; // G(m1+m2)
    private double totalEnergy;
    private double initialImpulse;
    private double finalImpulse;

    private final double G=6.67*Math.pow(10,-11);


    public HohmannTransfer(CelestialObjects p1, CelestialObjects p2){
        this.p1=p1;
        this.p2=p2;
        this.c1=p1.getCentralBody();
        this.a1=p1.getSemiMajorAxisJ2000();
        this.a2=p2.getSemiMajorAxisJ2000();
    }


    public void setTime(){
        time=Math.PI*Math.sqrt(Math.pow(2*a,3)/8*u);
    }

    public void setA (){
        a=1/2.0*(a1+a2);
    }

    public void setU (CelestialObjects orbiting) {
        u=G*c1.getMass();
    }

    public void setTotalEnergy(){
        totalEnergy=-u/2*a;
    }

    public void setInitialImpulse(CelestialObjects p1){
        initialImpulse=Math.sqrt(2*(totalEnergy+u/a1))-Math.sqrt(u/a1);
    }
    public void setFinalImpulse(CelestialObjects p2) {
        finalImpulse=Math.sqrt(u/a2)-Math.sqrt(2*totalEnergy+u/a2);
    }
    /*

     * Computes the velocity of a particle in ellipticalOrbit
     * @param r The radius from the primary focus to the particle
     * @param a The semimajor axis of the ellipse
     * @param b The semiminor axis of the ellipse
     * @param rp The radius of the orbit at periapsis
     * @return

    public Vector getEllipticalVelocity(Vector r, double a, double b, rp) {
        // Vis Viva equation obtainins the speed of the satelite relative to the primary focus of its elliptical orbit
        double softV = sqrt( (2*u/ r.length()) - (u/ a)); // Velocity of the particle relative to the occupied focus of the ellipse
        double p = Math.square(b)/ a; // Semilatus Rectum
        double e = 1 - (rp/a);  // Eccentricity
        return new Vector()
    }
    */

    /*
    public void targetPlanetPositionToLaunch(Planets target){
        launchPlanetCompleteOrbit*time/target.period;
    }
    */

}
