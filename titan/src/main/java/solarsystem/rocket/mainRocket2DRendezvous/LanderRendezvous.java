package solarsystem.rocket.mainRocket2DRendezvous;

import physics.ODEsolvable;
import solarsystem.CelestialObject;
import solarsystem.Planet;
import solarsystem.rocket.SpaceCraft;
import solarsystem.Trajectory;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;

import static utils.MathUtil.G;

public class LanderRendezvous  extends SpaceCraft implements ODEsolvable {
    int phase = 1;
    Vector3D departureVelocity;
    Date departureTime;

    @Override
    public void initializeCartesianCoordinates(Date date) { }

    public LanderRendezvous( Vector3D centralPos,
                                  Vector3D centralVel, Date date, Trajectory trajectory) {
        super();
        this.trajectory = trajectory;
        this.name = "LanderRendezvous";
        this.radius = 10000;
        this.dryMass = 265000; // kg
        this.fuelMass = 221500; // kg
        this.mass = this.dryMass + this.fuelMass;
        this.current_date = date;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.thrusterImpulse = 4000; // newtons per second TODO: Check value
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

    private void phase1() {
        if (getCentralPos().norm() < 250000 && isTangentialToTarget(this)) { // 200000 is the expected altitude at perigee
            centralVel = new Vector3D(0,0,0);
            phase = 2;
        }
    }

    private void phase2() {
        if (current_date.after(departureTime)) {
            centralVel = departureVelocity;
            phase = 3;
        }
    }

    private void phase3() {

    }

    public void setDepartureTime(Date date) { this.departureTime = departureTime; }
}
