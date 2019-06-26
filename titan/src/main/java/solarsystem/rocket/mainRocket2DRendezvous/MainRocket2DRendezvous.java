package solarsystem.rocket.mainRocket2DRendezvous;
import physics.ODEsolvable;
import solarsystem.CelestialObject;
import solarsystem.SolarSystem;
import solarsystem.rocket.SpaceCraft;
import solarsystem.Trajectory;
import utils.Date;
import utils.vector.Vector3D;

import javax.sound.midi.Soundbank;
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
                                  Vector3D centralVel, Date date, Trajectory trajectory, LanderRendezvous lander) {
        super();
        this.lander = lander;
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


            //Put the lander that is on it's transfer orbit
            double landerMass = 1e3; // kg
            muh = G* (trajectory.getCentralBodyMass() + landerMass); // https://en.wikipedia.org/wiki/Orbital_mechanics
            double semimajorAxis = (centralPos.norm() + 1200e3)/2;
            Vector3D velocity = getCentralVel().unit().scale(Math.sqrt(muh*(2/centralPos.norm() - 1/semimajorAxis)));
            double period = 2*Math.sqrt((Math.pow(semimajorAxis,3)/muh))*(Math.PI); // probably in seconds Astrobook p.37 (reformed) // TODO: check whether this needs to be rounded
            Trajectory trajectory = new Trajectory(this.trajectory.getCentralBodyMass(), this.trajectory.getCentralBodySphereOfInfluence(), semimajorAxis, period);
            lander.setTrajectory(trajectory);
            lander.setDepartureTimeInMillis(this.departureTime.getTimeInMillis()-(long)(1e3*period/2));
            lander.setCentralVel(velocity);
            phase = 2;

            System.out.println("Phase 2 is being called: " + current_date.getTimeInMillis());
        }
    }

    private void phase2() {
        if (current_date.after(departureTime)) {
            System.out.println("Rocket time to leave " + current_date.getTimeInMillis());
            trajectory.setPeriod(0); trajectory.setSemiMajorAxis(0);
            centralVel = departureVelocity.scale(1);
            lander.setTrajectory(trajectory);
            lander.setCentralPos(centralPos);
            lander.setCentralVel(centralVel);
            phase = 3;
        }
    }

    private void phase3() {

    }

}
