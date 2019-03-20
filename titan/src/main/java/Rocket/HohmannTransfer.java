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
    private double circularVelocity;
    private double perihelionVelocity;
    private double initialImpulse;
    private double finalImpulse;
    private double phaseAngle;
    private double synodicPeriod;
    private double infiniteVelocity;

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

    public void setU () {
        u=G*c1.getMass();
    }

    public void setTotalEnergy(){
        totalEnergy=-u/2*a;
    }

    public void setCircularVelocity(){
        circularVelocity=u/a1;
    }
    public void setPerihelionVelocity(){
        perihelionVelocity=Math.sqrt(2*(totalEnergy+circularVelocity));
    }

    // necessary magnitude of the spacecraft velocity vector
    public void setInitialImpulse(){
        initialImpulse=perihelionVelocity-Math.sqrt(circularVelocity);
    }
    public void setFinalImpulse() {
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

    public void phaseAngle(){
        phaseAngle=Math.PI-Math.sqrt(u)*time/Math.pow(a2,3/2.0);
        phaseAngle=phaseAngle*180/Math.PI;
    }

    public void synodicPeriod(){
        synodicPeriod=2*Math.PI/(Math.sqrt(u/Math.pow(a1,3))-Math.sqrt(u/Math.pow(a2,3)));
    }

    public void waitingPeriod(){

    }

    public void setInfiniteVelocity(){
        //infiniteVelocity=Math.acos(-1,eccentricity);
    }

}
