package physics;

import solarsystem.CannonBall;
import solarsystem.CelestialObject;
import solarsystem.Planet;
import solarsystem.Projectile;
import utils.Date;
import utils.Vector3D;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * A class which uses a verlet velocity algorithm the locate the location of the
 * planets. This is an 2nd order ODE solver.
 * https://resources.saylor.org/wwwresources/archived/site/wp-content/uploads/2011/06/MA221-6.1.pdf
 *
 */
public class VerletVelocity {
    private ArrayList<? extends CelestialObject> bodies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Planet> planets;
    private Date currentDate;


    public VerletVelocity(ArrayList<? extends CelestialObject> bodies,
                          Date date) {
        this.currentDate = date;
        this.bodies = bodies;
        projectiles = new ArrayList<>();
        planets = new ArrayList<>();
        for (CelestialObject body: bodies) {
            body.initializeCartesianCoordinates(date);
            if (body instanceof Projectile) projectiles.add((Projectile) body);
            if (body instanceof Planet) planets.add((Planet) body);
            System.out.println(body);
        }
        updateForce();
    }

    private void updateForce(){
        //Don't let the cannonballs put force on each other.
        for (CelestialObject body: bodies) {
            body.setForces(planets);
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
        CannonBall.minDistanceAllCurrentDT = Double.MAX_VALUE;
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

        for (Projectile projectile : projectiles){
            projectile.checkColisions();
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
