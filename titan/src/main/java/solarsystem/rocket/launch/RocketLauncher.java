package solarsystem.rocket.launch;

import solarsystem.CelestialObject;
import solarsystem.Planet;
import solarsystem.rocket.SpaceCraft;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;

import static utils.MathUtil.G;

public class RocketLauncher extends SpaceCraft {

    private double timeCurrentPhase;
    private double timeAtCurrentPhase = 0;

    private boolean boost1_done;
    private boolean boost1_started;

    private double[] burnRate;
    private double[] gasRelativeSpeed;
    private double[] mass_propellant;
    private double[] mass_rocketParts;
    private double[] maxFt;

    private final int GEO = 35786000;
    private final double M; // 5.972*Math.pow(10,24);
    private final double R; // 6371000;
    private final double g0; //9.81

    public RocketLauncher(Vector3D centralPos,
                          Vector3D centralVel, Date date, Planet fromPlanet){
        super();
        this.old_date = date;
        this.fromPlanet = fromPlanet;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        M=fromPlanet.getMass();
        R=fromPlanet.getRadius();
        Ft = 0;
        //Rocket Specs Based on Falcon 9 (Not the latest revised)
        //1st stage is Ft made by 9 Merlin 1C
        double firstPhaseFt=3780000;
        //2nd stage is Ft made by one Merlin 1C
        double secondPhaseFt=445000;
        maxFt= new double[]{firstPhaseFt,secondPhaseFt};
        g0= G*M/Math.pow(R,2);//9.81;
        double firstPhaseIsp=275;
        double secondPhaseIsp=348;
        gasRelativeSpeed = new double[]{firstPhaseIsp*g0,secondPhaseIsp*g0};
        computeBurnRates(2);
        //LOX 1.141Kg/L and RP-1 is 0.81Kg/L
        //First stage: LOX : 146000L=166586Kg, RP-1 : 94000L=76140Kg, Total : 242725Kg
        //Second stage: LOX : 27600L=31491.6Kg, RP-1 : 17400L=14094Kg, Total : 45585.6Kg
        double firstPhasePropellant=246486;
        double secondPhasePropellant=46281.6;
        mass_propellant = new double[]{firstPhasePropellant, secondPhasePropellant};
        double engineDryMass=630;
        mass_rocketParts = new double[]{9*engineDryMass+17125,engineDryMass+17125};
        totalInitMass();
        acceleration = new Vector3D();
    }

    public void boost(int n){
        timeCurrentPhase=mass_propellant[n-1]/burnRate[n-1];
        double x=centralPos.getX();
        double y=centralPos.getY();
        double z=centralPos.getZ();
        double d=Math.pow(Math.pow(x,2)+Math.pow(y,2)+Math.pow(R,2),3/2); //common term to both equation
        double x_doubledot=burnRate[n-1]*gasRelativeSpeed[n-1]*-Math.sin(z)/mass-G*M*x/d;
        double y_doubledot=burnRate[n-1]*gasRelativeSpeed[n-1]*Math.cos(z)/mass-G*M*y/d;
        acceleration.setX(x_doubledot);

        acceleration.setY(y_doubledot);
    }

    public void coast(int n){
        mass-=mass_rocketParts[n-1];
    }


    public void updateOrientation(){
        computeG();
        double z;
        double Vx=centralVel.getX();
        double Vy=centralVel.getY();
        if(Vx==0){
            z=0;
            System.out.println("Vx was 0:"+z);
        }
        else if(Vy==0){
            if(Vx>0){
                z=-1.5708; //-90°
            }
            else{
                z=1.5708;
            }
            System.out.println("Vy was 0:"+z);
        }
        else{
            z=Math.atan(centralVel.getY()/centralVel.getX())-Math.PI/2;
            System.out.println("Entire formula was used:"+z);

        }
        double v=Math.sqrt(Math.pow(Vx,2)+Math.pow(Vy,2));
        double z_dot=g*Math.sin(z)/v-v*Math.sin(z)/(R+centralPos.getY());
        acceleration.setZ(z_dot);
    }

    public void computeG(){
        g=g0*Math.pow(R/(R+centralPos.getY()),2);
    }

    public void computeBurnRates(int n){
        burnRate=new double[n];
        for(int i=0;i<maxFt.length;i++){
            burnRate[i]=maxFt[i]/gasRelativeSpeed[i];
        }
    }

    public void totalInitMass(){
        for(int i=0;i<mass_propellant.length;i++){
            mass+=mass_propellant[i];
        }
        for(int j = 0;j<mass_rocketParts.length;j++){
            mass+=mass_rocketParts[j];
        }
    }

    public void calculateMass(int n){
        mass-=burnRate[n-1]*dt;
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date) {
        dt = differenceInSeconds(date); // get size of the time step
        this.old_date = new Date(date);
        if(!boost1_done){
            if(!boost1_started){
                boost(1);
                boost1_started=true;
            }
            if (timeAtCurrentPhase < timeCurrentPhase) {
                calculateMass();
                updateOrientation();
                timeAtCurrentPhase += dt;
            } else {
                boost1_done = true;
                timeAtCurrentPhase = 0;
                Ft = 0;
            }}
        else{
            acceleration.setX(0);
            acceleration.setY(-g0);
            acceleration.setZ(0);
            updateOrientation();
            }
        }
    @Override
    public void initializeCartesianCoordinates(Date date) {

    }
}
