package Rocket;

import solarsystem.CelestialObjects;

public class HohmannTransfer {

    private CelestialObjects p1;
    private CelestialObjects p2;
    private CelestialObjects c1;

    private double time;
    private double a1;
    private double a2;
    private double a;
    private double u;
    private double totalEnergy;
    private double initialImpulse;
    private double finalImpulse;

    private final double G=6.67*Math.pow(10,-11);     //u=G(m1+m2)


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
    public void targetPlanetPositionToLaunch(Planets target){
        launchPlanetCompleteOrbit*time/target.period;
    }
    */

}
