package solarsystem.rocket.launch;

import rocket.Rocket;
import solarsystem.CelestialObject;
import solarsystem.rocket.lunarLander.Lunarlander;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;

import static utils.MathUtil.G;

public class RocketLauncher extends Lunarlander { //TODO: should not extend Lunarlander

    private double timeCurrentPhase;
    private double timeAtCurrentPhase = 0;

    private boolean boost1_done;
    private boolean boost1_started;

    private double burnRate;
    private double gasRelativeSpeed;
    private double[] mass_propellant;
    private double[] mass_rocketParts;

    private final int GEO = 35786000;
    private final double M = 5.972*Math.pow(10,24); //TODO : change to getCentralBodyMass (Earth)
    private final double R = 6371000; //TODO: change to central body radii

    public RocketLauncher(Vector3D centralPos,
                          Vector3D centralVel, Date date){
        super();
        this.old_date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        Ft = 0;
        burnRate = 14900;
        gasRelativeSpeed = 2770;
        mass_propellant = new double[]{2690000};
        mass_rocketParts = new double[]{110000};
        g=9.81; //TODO: change to centralbody gravity
        totalInitMass();
        acceleration = new Vector3D();
    }

    public void boost(int n){
        timeCurrentPhase=mass_propellant[n-1]/burnRate;
        double x=centralPos.getX();
        double y=centralPos.getY();
        double z=centralPos.getZ();
        double d=Math.pow(Math.pow(x,2)+Math.pow(y,2)+Math.pow(R,2),3/2); //common term to both equation
        double x_doubledot=burnRate*gasRelativeSpeed*-Math.sin(z)/mass-G*M*x/d;
        double y_doubledot=burnRate*gasRelativeSpeed*Math.cos(z)/mass-G*M*y/d;
        acceleration.setX(x_doubledot);
        acceleration.setY(y_doubledot);
    }

    public void coast(int n){
        mass-=mass_rocketParts[n-1];
    }


    public void updateOrientation(){
        double z=Math.atan(centralVel.getY()/centralVel.getX())-Math.PI/2;
        centralPos.setZ(z);
    }

    public void totalInitMass(){
        mass=0;
        for(int i=0;i<mass_propellant.length;i++){
            mass+=mass_propellant[i];
        }
        for(int j = 0;j<mass_rocketParts.length;j++){
            mass+=mass_rocketParts[j];
        }
    }

    //TODO: Should the change in mass be common ?
    @Override
    public void calculateMass(){
        mass-=burnRate*dt;
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
            acceleration.setY(-g);
            acceleration.setZ(0);
            updateOrientation();
            }
        }
    }
