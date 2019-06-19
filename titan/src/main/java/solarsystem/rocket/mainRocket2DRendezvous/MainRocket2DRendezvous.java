package solarsystem.rocket.mainRocket2DRendezvous;
import solarsystem.Planet;
import physics.ODEsolvable;
import solarsystem.CelestialObject;
import solarsystem.SolarSystem;
import solarsystem.rocket.SpaceCraft;
import solarsystem.Trajectory;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;
import java.util.Calendar;

import static utils.MathUtil.G;

public class MainRocket2DRendezvous extends SpaceCraft implements ODEsolvable {
    private int phase = 1;
    private SolarSystem solarSystem;
    private Date departureTime;
    private Vector3D departureVelocity;
    LanderRendezvous lander; // TODO: Turn into spacecraft

    @Override
    public void initializeCartesianCoordinates(Date date) { }

    public MainRocket2DRendezvous(SolarSystem solarSystem, double length, Vector3D centralPos,
                                  Vector3D centralVel, Date date, Trajectory trajectory) {
        super();
        this.solarSystem = solarSystem;
        this.trajectory = trajectory;
        this.name = "mainRocket2DRendezvous";
        this.radius = length;
        this.dryMass = 1000;//265000; // kg
        this.fuelMass = 1; //221500; // kg
        this.mass = this.dryMass + this.fuelMass;
        this.old_date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.thrusterImpulse = 0;//4000; // newtons per second TODO: Check value
        this.acceleration = new Vector3D();
        this.toPlanet = (Planet) trajectory.getTargetBody();
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date) {
        acceleration = new Vector3D();

        switch (this.phase) {
            case 1: super.acceleration = phase1(); break;
            case 2: super.acceleration = phase2(); break;
            case 3: super.acceleration = phase3(); break;
        }
        //Vector3D accTemp = acceleration;
        //int i = 1;
    }


    public Vector3D phase1() {
        //trajectory.getTargetBody().setCentralPos(new Vector3D(0,-1*trajectory.getTargetBody().getRadius()/2,0));

        if (isTangentialToTarget(this)) {
            // Bring the Rocket to it's elliptical orbit
            double muh = G* (trajectory.getTargetBody().getMass() + this.mass); // https://en.wikipedia.org/wiki/Orbital_mechanics
            this.trajectory.setSemiMajorAxis(((this.centralPos.substract(trajectory.getTargetBody().getCentralPos())).norm() + ((Planet)trajectory.getTargetBody()).getSphereOfInfluence())/2); // Astrobook page 61;-muh/(2*C); % Astrobook p. 62
            Vector3D priorVel = this.centralVel;
            this.centralVel = this.centralVel.unit().scale(Math.sqrt((-muh/trajectory.getSemiMajorAxis() + 2*muh/centralPos.substract(trajectory.getTargetBody().getCentralPos()).norm()))); // Astrobook page 61 (reformulated formula)
            if (this.centralVel.norm() < priorVel.norm())  {
                int i = 0;
            }
            this.trajectory.setPeriod(Math.round(2*Math.PI*Math.sqrt(Math.pow(this.trajectory.getSemiMajorAxis(), 3)/muh))); // Astrobook p.37 (reformed)
            this.departureTime = new Date(old_date);
            this.departureTime.add(Calendar.MILLISECOND, (int)(this.trajectory.getPeriod()*1000));
            this.departureVelocity = centralVel;

            // Construct a lander that is on it's transfer orbit
            double landerMass = 1000; // kg
            muh = G* (trajectory.getTargetBody().getMass() + landerMass); // https://en.wikipedia.org/wiki/Orbital_mechanics
            double semimajorAxis = ((getCentralPos().substract(this.trajectory.getTargetBody().getCentralPos())).norm() + ((Planet)this.trajectory.getTargetBody()).getSphereOfInfluence())/2; // Astrobook page 61
            Vector3D velocity = getCentralVel().unit().scale(Math.sqrt((-muh/semimajorAxis + 2*muh/(getCentralPos().substract(this.trajectory.getTargetBody().getCentralPos()).norm())))); // Astrobook page 61 (reformulated formula)
            double period = 2*Math.sqrt((Math.pow(semimajorAxis,3)/muh)*(Math.PI)); // probably in seconds Astrobook p.37 (reformed) // TODO: check whether this needs to be rounded
            //Trajectory trajectory = new Trajectory(this.trajectory.getTargetBody(), semimajorAxis, period);
            //lander = new LanderRendezvous(getCentralPos(), velocity, getDate(), trajectory);
            Date departureTime = new Date(old_date);
            departureTime.add(Calendar.MILLISECOND, (int)(this.trajectory.getPeriod() - period/2)*1000);
            //lander.setDepartureTime(departureTime);
            //solarSystem.addAnimatedObject(lander);

            phase = 2;
        }

        Vector3D d = centralPos;//.substract(new Vector3D(0,1,0).scale(trajectory.getTargetBody().getRadius()*1e03));
        System.out.println(d);
        Vector3D a = d.scale(-G* trajectory.getTargetBody().getMass()/Math.pow(d.norm(),3));
        System.out.println("Gravity: " + a);
        return a;
    }

    public Vector3D phase2() {
        /**if (old_date.after(departureTime)) {
            trajectory.setPeriod(0); trajectory.setSemiMajorAxis(0);
            setCentralVel(departureVelocity);
            //solarSystem.removeAnimatedObject(lander.getName());
        }**/
        Vector3D d = centralPos;//.substract(new Vector3D(0,1,0).scale(trajectory.getTargetBody().getRadius()*1e03));
        System.out.println(d);
        Vector3D a = d.scale(-G* trajectory.getTargetBody().getMass()/Math.pow(d.norm(),3));
        System.out.println("Gravity: " + a);
        return a;
    }

    public Vector3D phase3() {
        Vector3D d = centralPos.substract(trajectory.getTargetBody().getCentralPos());
        return d.scale(G*trajectory.getTargetBody().getMass()/Math.pow(d.norm(),3));
    }

}
