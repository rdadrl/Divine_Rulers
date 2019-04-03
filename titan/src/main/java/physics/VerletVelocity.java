package physics;

import solarsystem.CelestialObject;
import utils.Date;
import utils.Vector3D;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * A class which uses a verlet velocity algorithm the locate the location of the
 * planets.
 * https://resources.saylor.org/wwwresources/archived/site/wp-content/uploads/2011/06/MA221-6.1.pdf
 *
 */
public class VerletVelocity {
    private ArrayList<? extends CelestialObject> bodies;
    private Date currentDate;


    public VerletVelocity(ArrayList<? extends CelestialObject> bodies,
                          Date date) {
        this.currentDate = date;
        this.bodies = bodies;
        initializeCartesianCoordinates(date);
        //Initialize the beginning forces.
        for (CelestialObject body: bodies) {
            body.setForces(bodies);
        }

    }
    private void initializeCartesianCoordinates(Date date){
        for (CelestialObject body: bodies) {
            body.initializeCartesianCoordinates(date);
        }
    }

    private void updateForce(){
        for (CelestialObject body: bodies) {
            body.setForces(bodies);
        }
    }

    /**
     * update the location of all the bodies for a timestep in seconds. We
     * are using the short version of verlet velocity integration
     * https://resources.saylor.org/wwwresources/archived/site/wp-content/uploads/2011/06/MA221-6.1.pdf
     *
     * main formula is
     * a(t) = F(t) / m
     * x(t + dt) = x(t) + v(t) dt + 0.5 a(t) dt^2
     * v(t + dt) = 0.5 * (a(t) + a(t + dt)) * dt
     *
     * Calculate it as follow
     * 1. v(t + 0.5dt) = v(t) + 0.5 a(t) * dt
     * 2. x(t + dt) = x(t) + v(t + 0.5dt) * dt
     * 3. recalculate forces and get a(t + dt) using x(t + dt)
     * 4. v(t + dt) = v(t + 0.5dt) + 0.5 a(t + dt) * dt
     *
     * @param time timestep
     * @param unit unit of the timestep
     *
     */

    public void updateLocation(long time, TimeUnit unit){
        time = TimeUnit.SECONDS.convert(time, unit); //convert to seconds
        currentDate.add(Calendar.SECOND, (int)time);
        double dt = time;

        // Update positions and half-update velocities
        // set the change in position
        // v(t + dt) = x(t) + v(t) dt + 0.5 a(t) dt^2
        // a(t) = F(t) / m
        for (CelestialObject body: bodies) {
            Vector3D startVel = body.getCentralVel();
            Vector3D startPos = body.getCentralPos();

            Vector3D startForce = body.getForces();
            Vector3D startAcceleration = startForce.scale(1D / body.getMass());

            // step 1 v(t + 0.5dt) = v(t) + 0.5 a(t) * dt
            Vector3D velChange = startAcceleration.scale((dt) / 2.0);
            body.setCentralVel(startVel.add(velChange), currentDate); // set half vel
            Vector3D halfVel = body.getCentralVel();

            // step 2 x(t + dt) = x(t) + v(t + 0.5dt) * dt
            Vector3D posChange = halfVel.scale(dt);

            Vector3D Earthpos = bodies.get(3).getCentralPos();

            Vector3D centralPos = startPos.add(posChange).substract(bodies.get(3).getCentralPos());
            body.setCentralPos(startPos.add(posChange), currentDate);
        }

        // step 3 update the forces halfway
        updateForce();

        for (CelestialObject body: bodies) {
            Vector3D halfVel = body.getCentralVel();

            Vector3D endForce = body.getForces();
            Vector3D endAcceleration = endForce.scale(1D / body.getMass());

            // step 4 v(t + dt) = v(t + 0.5dt) + 0.5 a(t + dt) * dt
            Vector3D velChange = endAcceleration.scale((dt) / 2.0);
            body.setCentralVel(halfVel.add(velChange), currentDate);
        }
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void printXYZ(String name, Vector3D posCoord, Date date){
        System.out.print(name + ": ");
        System.out.print(date);
        System.out.print("\t\tX: " + posCoord.getX());
        System.out.print("\t\tY: " + posCoord.getY());
        System.out.print("\t\tZ: " + posCoord.getZ() + "\n");
    }
}
