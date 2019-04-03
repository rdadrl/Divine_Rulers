package rocket;

import solarsystem.Planet;

public class HohmannTransfer {

    private Planet p1; // Planet 1
    private Planet p2; // Planet 2
    private Planet c1; // Central planet

    private double time; // Time in transfer
    private double a1; // semimajor axis of p1
    private double a2; // semimajor axis of p2
    private double a; // semimajor axis of transfer orbit
    private double u; // G(m1+m2)
    private double totalEnergy;
    private double circularVelocity;
    private double perihelionVelocity;
    private double apoapsisVelocity;
    private double initialImpulse;
    private double finalImpulse;
    private double phaseAngle;
    private double synodicPeriod;
    //private double infiniteVelocity;

    private final double G=6.67*Math.pow(10,-11);



    public HohmannTransfer(Planet p1, Planet p2){
        this.p1=p1;
        this.p2=p2;
        this.c1=p1.getCentralBody();
        this.a1=p1.getSemiMajorAxisJ2000();
        this.a2=p2.getSemiMajorAxisJ2000();
        setA();
        setU();
        setTime();
        setTotalEnergy();
        setCircularVelocity();
        setPerihelionVelocity();
        setApoapsisVelocity();
        setInitialImpulse();
        setFinalImpulse();
        setPhaseAngle();
        setSynodicPeriod();
    }

    private void setA (){
        a=1/2.0*(a1+a2);
    }

    private void setU () {
        u=G*c1.getMass();
    }

    private void setTime(){
        time=Math.PI*Math.sqrt(Math.pow(2*a,3)/8*u);
    }

    private void setTotalEnergy(){
        totalEnergy=-u/2*a;
    }

    private void setCircularVelocity(){
        circularVelocity=Math.sqrt(u/a);
    }

    private void setPerihelionVelocity(){
        perihelionVelocity=Math.sqrt(2*(totalEnergy+u/a1));
    }

    private void setApoapsisVelocity(){
        apoapsisVelocity=Math.sqrt(2*totalEnergy+u/a2);
    }

    // necessary magnitude of the spacecraft velocity vector
    private void setInitialImpulse(){
        initialImpulse=perihelionVelocity-Math.sqrt(u/a1);
    }

    private void setFinalImpulse() {
        finalImpulse=Math.sqrt(u/a2)-apoapsisVelocity;
    }

    private void setPhaseAngle(){
        phaseAngle=Math.PI-Math.sqrt(u)*time/Math.pow(a2,3/2.0);
        phaseAngle=phaseAngle*180/Math.PI;
    }

    private void setSynodicPeriod(){
        synodicPeriod=2*Math.PI/(Math.sqrt(u/Math.pow(a1,3))-Math.sqrt(u/Math.pow(a2,3)));
    }

    public double getA1(){
        return a1;
    }

    public double getA2(){
        return a2;
    }

    public double getA(){
        return a;
    }

    public double getU(){
        return u;
    }

    public double getTime(){
        return time;
    }

    public double getTotalEnergy(){
        return totalEnergy;
    }

    public double getCircularVelocity(){
        return circularVelocity;
    }

    public double getApoapsisVelocity() {
        return apoapsisVelocity;
    }

    public double getPerihelionVelocity() {
        return perihelionVelocity;
    }

    public double getInitialImpulse(){
        return initialImpulse;
    }

    public double getFinalImpulse() {
        return finalImpulse;
    }

    public double getPhaseAngle() {
        return phaseAngle;
    }

    public double getSynodicPeriod() {
        return synodicPeriod;
    }

    /*
    public void waitingPeriod(){

    }

    public void setInfiniteVelocity(){
        //infiniteVelocity=Math.acos(-1,eccentricity);
    }



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

}
