package solarsystem.rocket.mainRocket2DRendezvous;
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
    private Date departureTime;
    private Vector3D departureVelocity;
    LanderRendezvous lander; // TODO: Turn into spacecraft
    private long endPhase2;

    @Override
    public void initializeCartesianCoordinates(Date date) { }

    public MainRocket2DRendezvous(double length, Vector3D centralPos,
                                  Vector3D centralVel, Date date, Trajectory trajectory) {
        super();
        this.trajectory = trajectory;
        this.name = "mainRocket2DRendezvous";
        this.radius = length;
        this.dryMass = 1000;//265000; // kg
        this.fuelMass = 1; //221500; // kg
        this.mass = this.dryMass + this.fuelMass;
        this.current_date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.thrusterImpulse = 0;//4000; // newtons per second TODO: Check value
        this.acceleration = new Vector3D();
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date) {
        acceleration = new Vector3D();

        switch (this.phase) {
            case 1: phase1(); break;
            case 2: phase2(); break;
            case 3: phase3(); break;
        }
        Vector3D d = centralPos;
        super.acceleration = d.scale(-G* trajectory.getCentralBodyMass()/Math.pow(d.norm(),3));
    }


    public void phase1() {
        //trajectory.getTargetBody().setCentralPos(new Vector3D(0,-1*trajectory.getTargetBody().getRadius()/2,0));

        if (isTangentialToTarget(this)) {
            // Bring the Rocket to it's elliptical orbit
            double muh = G* (trajectory.getCentralBodyMass() + this.mass); // https://en.wikipedia.org/wiki/Orbital_mechanics
            this.trajectory.setSemiMajorAxis(this.centralPos.norm() + 0.5*(trajectory.getCentralBodySphereOfInfluence())); // Astrobook page 61;-muh/(2*C); % Astrobook p. 62
            Vector3D priorVel = this.centralVel;
            this.centralVel = this.centralVel.unit().scale(Math.sqrt((-muh/trajectory.getSemiMajorAxis() + 2*muh/centralPos.norm()))); // Astrobook page 61 (reformulated formula)
            if (this.centralVel.norm() < priorVel.norm())  {
                int i = 0;

            }
            this.trajectory.setPeriod(Math.round(2*Math.PI*Math.sqrt(Math.pow(this.trajectory.getSemiMajorAxis(), 3)/muh))); // Astrobook p.37 (reformed)
            this.departureTime = new Date(current_date);
            this.departureTime.add(Calendar.MILLISECOND, (int)(this.trajectory.getPeriod()*1000));
            this.endPhase2 = departureTime.getTimeInMillis();
            this.departureVelocity = priorVel;


            //Construct a lander that is on it's transfer orbit
            //double landerMass = 1000; // kg
            //muh = G* (trajectory.getCentralBodyMass() + landerMass); // https://en.wikipedia.org/wiki/Orbital_mechanics
            //double semimajorAxis = (getCentralPos().norm() + (trajectory.getCentralBodySphereOfInfluence())/2); // Astrobook page 61
            //Vector3D velocity = getCentralVel().unit().scale(Math.sqrt((-muh/semimajorAxis + 2*muh/(getCentralPos().norm())))); // Astrobook page 61 (reformulated formula)
            //double period = 2*Math.sqrt((Math.pow(semimajorAxis,3)/muh)*(Math.PI)); // probably in seconds Astrobook p.37 (reformed) // TODO: check whether this needs to be rounded
            //Trajectory trajectory = new Trajectory(this.trajectory.getTargetBody(), semimajorAxis, period);
            //lander = new LanderRendezvous(getCentralPos(), velocity, getDate(), trajectory);
            //Date departureTime = new Date(current_date);
            //departureTime.add(Calendar.MILLISECOND, (int) (this.trajectory.getPeriod() - period/2)*1000);

            phase = 2;

            System.out.println("Phase 2 is being called: " + current_date.getTimeInMillis());
        }
    }

    public void phase2() {
        if (current_date.getTimeInMillis() > endPhase2) {
            trajectory.setPeriod(0); trajectory.setSemiMajorAxis(0);
            setCentralVel(departureVelocity.scale(1.1));
            phase = 3;
            System.out.println("Phase 3 is being called: " + current_date.getTimeInMillis());
//            solarSystem.removeAnimatedObject(lander.getName());
        }
    }

    public void phase3() {

    }

}
