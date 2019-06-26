package solarsystem.rocket.mainRocket;

import manouvres.LambertSolver;
import physics.ODEsolvable;
import physics.PIDcontroller;
import solarsystem.CelestialObject;
import solarsystem.Planet;
import solarsystem.rocket.Falcon9Imaginary;
import solarsystem.rocket.SpaceCraft;
import utils.Date;
import utils.MathUtil;
import utils.vector.Vector;
import utils.vector.Vector3D;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 *
 */
public class InterPlanetaryRocketToTitan extends Falcon9Imaginary implements ODEsolvable {
    private Vector3D velSetPoint;
    private Vector3D startingPos;
    private Vector3D destinationPos;

    private Date arrivalDate;
    private double timeOfFlight; //seconds
    private BigDecimal time_increment;
    private int counter; //counter

    private double sphereOfInfluence_secondBody;
    private boolean impulseManouvre;
    private HashSet<Integer> impulseTimes;
    private boolean two_stagedMission;
    private boolean inSphereSaturn;

    private HashSet<Integer> impulseMoments = new HashSet<>();
    private int amount_impulseUpdates_alongTheWay;

    private Planet Sun;
    private Planet Saturn;

    private PIDcontroller x_control;
    private PIDcontroller y_control;
    private PIDcontroller z_control;

    private final double REFERENCE_MASS = 546300.0; // mass on which the P controller was tuned.

    private double netForce = 0;

    double P = 15000;
    double I = 0;



    public InterPlanetaryRocketToTitan(double mass, Planet fromPlanet, Planet toPlanet, Date current_date, Vector3D departurePos, Vector3D departureVel, Vector3D destinationPos, Date arrivalDate) {
//        this.mass = mass;
        super();
        this.radius = 2000;
        this.fromPlanet = fromPlanet;
        this.toPlanet = toPlanet;
        this.current_date = current_date;
        if(departurePos != null) {
            this.centralPos = departurePos;
        }else{
            setPositionOutsideOfInfluenceDepartPlanet();
        }
        this.centralVel = departureVel;
        this.destinationPos = destinationPos;
        this.arrivalDate = new Date(arrivalDate);
        this.timeOfFlight = (arrivalDate.getTimeInMillis() - current_date.getTimeInMillis())/1000D;

        if(!toPlanet.getCentralBody().getName().equals("Sun")){
            two_stagedMission = true;
            Saturn = toPlanet.getCentralBody();
            if(toPlanet.getCentralBody().getCentralBody().getName().equals("Sun")){
                Sun = toPlanet.getCentralBody().getCentralBody();
            }else{
                throw new IllegalArgumentException("The sun either needs to be the main central body, or the central body of the central body");
            }
        }else{
            Sun = toPlanet.getCentralBody();
        }
        calculateLambertTrajectoryPhase1();
//        centralVel = velSetPoint;

        amount_impulseUpdates_alongTheWay = 50;
    }

    public InterPlanetaryRocketToTitan(double mass, Planet fromPlanet,
                                       Planet toPlanet, Date date) {
        this.mass = mass;
        this.fromPlanet = fromPlanet;
        this.toPlanet = toPlanet;
        this.current_date = date;
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date new_date) {
        // Date description
        time_increment = new BigDecimal(differenceInMiliSeconds(new_date)).divide(new BigDecimal(1000));
        dt = time_increment.doubleValue(); //dt in seconds
        this.current_date = new Date(new_date);
        if(current_date.getTimeInMillis() > arrivalDate.getTimeInMillis()) {
            phaseFinished = true;
        }

        Ft = 0;
        // Get current gravity
        Vector3D GravitationalForces = gravitationalForces(this, objectsInSpace);

        if(dt == 0) {
            acceleration = GravitationalForces.scale(1D/mass);
            return;
        }


        if(inSphereSaturn && impulseMoments.contains(counter)){
            calculateLambertTrajectoryPhase2();
//            centralVel = velSetPoint;
        }

        Vector3D thrust_Force = new Vector3D();

        if(impulseManouvre) {
            thrust_Force = impulseManouvre(GravitationalForces);
            Ft = thrust_Force.length();
            calculateMass();
            netForce = netForce + Ft;
        }

        if(impulseManouvre && centralVel.substract(velSetPoint).length()<1.0){
            impulseManouvre = false;
            System.out.println("NF: " + netForce);
        }

        acceleration = GravitationalForces.add(thrust_Force).scale(1D/mass);
        totTime = totTime.add(time_increment);
        counter++;
    }

    @Override
    public Vector3D getAcceleration() {
        return acceleration;
    }

    @Override
    public Vector3D getCentralPos() {
        return centralPos;
    }

    @Override
    public Vector3D getCentralVel() {
        return centralVel;
    }

    @Override
    public void setCentralVel(Vector3D centralVel) {
        this.centralVel = centralVel;
    }

    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        double distanceToSaturn = Saturn.getCentralPos().substract(this.getCentralPos()).length();
        double tof_left = (arrivalDate.getTimeInMillis() - current_date.getTimeInMillis())/1000D;
        if(!inSphereSaturn && distanceToSaturn < Saturn.getSphereOfInfluence() * 0.8) {
            impulseMoments = alongPositionUpdates(tof_left, dt, amount_impulseUpdates_alongTheWay);
            counter = 0;
            impulseMoments.add(0);
            System.out.println("In sphere of SATURN: " + this.getCentralPos().substract(Saturn.getCentralPos()).length());
            System.out.println(tof_left);
            inSphereSaturn = true;
        }

        this.centralPos = newCentralPos;
    }

    @Override
    public void initializeCartesianCoordinates(Date date) {}

    private void calculateLambertTrajectoryPhase1() {
        double tof_left = (arrivalDate.getTimeInMillis() - current_date.getTimeInMillis())/1000D;

        Vector3D r1_saturnPer = centralPos.substract(Sun.getCentralPos());
        Vector3D r2_saturnPer = destinationPos.substract(Sun.getCentralPos());
        double mu_centralPos = Sun.getMass() * MathUtil.G;

        LambertSolver lambertSolver_prog = new LambertSolver(mu_centralPos, r1_saturnPer, r2_saturnPer, tof_left, true);
        LambertSolver lambertSolver_ret = new LambertSolver(mu_centralPos, r1_saturnPer, r2_saturnPer, tof_left, false);
        Vector3D[] vel_prog = lambertSolver_prog.getVelocityVectors().get(0);
        Vector3D[] vel_ret = lambertSolver_ret.getVelocityVectors().get(0);

        Vector3D startvel_prog = vel_prog[0];
//        Vector3D startvel_ret = vel_ret[0];
        Vector3D curV0 = new Vector3D(centralVel);

//        System.out.println(destinationPos.substract(this.getCentralPos()).length());
//        System.out.println("cur vel: " + this.getCentralVel());
//        System.out.println("new vel: " + vel_prog[0]);
        velSetPoint = vel_prog[0];

        // add the escape velocity of earth to the current velocity in the right direction (assumption)

        Vector3D setP1 = new Vector3D(velSetPoint);
        Vector3D curV1 = new Vector3D(centralVel);
        Vector3D diff1 = setP1.substract(curV1);

        Vector3D escapeVel = diff1.unit().scale(11000);
        centralVel = centralVel.add(escapeVel);

        Vector3D setP = new Vector3D(velSetPoint);
        Vector3D curV = new Vector3D(centralVel);
        Vector3D diff = setP.substract(curV);

        impulseManouvre = true;
        initializePIDcontrolers();
    }

    private void calculateLambertTrajectoryPhase2() {
        double tof_left = (arrivalDate.getTimeInMillis() - current_date.getTimeInMillis())/1000D;

        Vector3D r1_saturnPer = centralPos.substract(Saturn.getCentralPos());
        Vector3D r2_saturnPer = destinationPos.substract(Saturn.getCentralPos());
        double mu_centralPos = Saturn.getMass() * MathUtil.G;

//        LambertSolver lambertSolver_prog = new LambertSolver(mu_centralPos, r1_saturnPer, r2_saturnPer, tof_left, true);
        LambertSolver lambertSolver_ret = new LambertSolver(mu_centralPos, r1_saturnPer, r2_saturnPer, tof_left, false);
        Vector3D[] vel_ret = lambertSolver_ret.getVelocityVectors().get(0);

//        System.out.println(destinationPos.substract(this.getCentralPos()).length());
//        System.out.println("cur vel: " + this.getCentralVel());
//        System.out.println("new vel: " + vel_ret[0]);
        velSetPoint = vel_ret[0];
        impulseManouvre = true;
        initializePIDcontrolers();
    }



    private void setPositionOutsideOfInfluenceDepartPlanet() {
        Vector3D departPos = fromPlanet.getCentralPos();
        // add the radius vector and put it outside the sphere of influence
        Vector3D addRadX = fromPlanet.getCentralPos().unit().scale((fromPlanet.getSphereOfInfluence()));
        centralPos = departPos.add(addRadX);
    }

    private HashSet<Integer> alongPositionUpdates(double tof, double timestep_val, int updates) {
        HashSet<Integer> results = new HashSet<>();
        double no_timesteps = tof/timestep_val;
        for(int i = 1; i < updates; i++) {
            results.add((int) (no_timesteps/updates) * i);
        }
        return results;
    }

    private void initializePIDcontrolers() {
        x_control = new PIDcontroller(P, I, 0);
        x_control.setTarget_pos(velSetPoint.getX());
        y_control = new PIDcontroller(P, I, 0);
        y_control.setTarget_pos(velSetPoint.getY());
        z_control = new PIDcontroller(P, I, 0);
        z_control.setTarget_pos(velSetPoint.getZ());
    }

    private Vector3D impulseManouvre(Vector3D gravitationalForces) {
        Vector3D setP = new Vector3D(velSetPoint);
        Vector3D curV = new Vector3D(centralVel);
        Vector3D diff = curV.substract(setP);

        double x_force = x_control.calculateOutput(centralVel.getX(), dt);
        double y_force = y_control.calculateOutput(centralVel.getY(), dt);
        double z_force = z_control.calculateOutput(centralVel.getZ(), dt);
        Vector3D net_thrustForce = new Vector3D(x_force, y_force, z_force).scale(-1.0).scale(mass/REFERENCE_MASS);
        Vector3D antiGravity = gravitationalForces.scale(-1.0);
        Vector3D desired_FT = antiGravity.add(net_thrustForce);
        Vector3D actual_FT = new Vector3D(desired_FT);
        if(actual_FT.length() > maxFtPropulsion) {
            actual_FT = actual_FT.unit().scale(maxFtPropulsion);
        }
        return actual_FT;
    }








}
