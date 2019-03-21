package utils;

import solarsystem.CelestialObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class VerletVelocity {
    private ArrayList<? extends CelestialObject> bodies;
    private Date currentDate;
    private Vector3D[] oldForces;

    public VerletVelocity(ArrayList<? extends CelestialObject> bodies){
        this.bodies = bodies;
        currentDate = new Date(2000,0,1,12,0,0);
        for(CelestialObject ob: bodies){
            ob.setHEEvel(ob.getHEEvel().scale(MathUtil.AU/(60*60*24)));
            ob.setHEEpos(ob.getHEEpos().scale(MathUtil.AU));
        }
        oldForces = new Vector3D[bodies.size()];
        for (CelestialObject body: bodies) {
            body.setForces(bodies);
        }

    }

    public VerletVelocity(ArrayList<? extends CelestialObject> bodies,
                          Date date) {
        this.currentDate = date;
        this.bodies = bodies;
        System.out.println(bodies.size());

        initializeCartesianCoordinates(date);
        for(CelestialObject planet: bodies){
            printXYZ(planet.getName(), planet.getHEEpos(), date);
            printXYZ(planet.getName(), planet.getHEEvel(), date);
            System.out.println();
        }
        oldForces = new Vector3D[bodies.size()];
        //Initialize the beginning forces.
        for (CelestialObject body: bodies) {
            body.setForces(bodies);
        }

    }
    private void initializeCartesianCoordinates(Date date){
        for (CelestialObject body: bodies) {
            System.out.println(body);
            body.initializeCartesianCoordinates(date);
        }
        for (CelestialObject body: bodies){
            body.setHEEvel(body.getHEEvel().scale(MathUtil.AU/(60*60*24)));
            body.setHEEpos(body.getHEEpos().scale(MathUtil.AU));
        }
    }



    private void updateForce(){
        //int i = 0;
        for (CelestialObject body: bodies) {
            //oldForces[i] = body.getForces();
            body.setForces(bodies);
            //i++;
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
            Vector3D startVel = body.getHEEvel();
            Vector3D startPos = body.getHEEpos();

            Vector3D startForce = body.getForces();
            Vector3D startAcceleration = startForce.scale(1D / body.getMass());

            // step 1 v(t + 0.5dt) = v(t) + 0.5 a(t) * dt
            Vector3D velChange = startAcceleration.scale((dt) / 2.0);
            body.setHEEvel(startVel.add(velChange)); // set half vel
            Vector3D halfVel = body.getHEEvel();

            // step 2 x(t + dt) = x(t) + v(t + 0.5dt) * dt
            Vector3D posChange = halfVel.scale(dt);
            body.setHEEpos(startPos.add(posChange));
        }

        // step 3 update the forces halfway
        updateForce();

        for (CelestialObject body: bodies) {
            Vector3D halfVel = body.getHEEvel();

            Vector3D endForce = body.getForces();
            Vector3D endAcceleration = endForce.scale(1D / body.getMass());

            // step 4 v(t + dt) = v(t + 0.5dt) + 0.5 a(t + dt) * dt
            Vector3D velChange = endAcceleration.scale((dt) / 2.0);
            body.setHEEvel(halfVel.add(velChange));
        }


    }







    /**
     * update the location of all the bodies for a timestep in seconds. Trying the short version
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


    public void updateLocation(long time, TimeUnit unit){

        time = TimeUnit.SECONDS.convert(time, unit); //convert to seconds
        currentDate.add(Calendar.SECOND, (int)time);
        double dt = time;

        // Update positions and half-update velocities
        // set the change in position
        // v(t + dt) = x(t) + v(t) dt + 0.5 a(t) dt^2
        // a(t) = F(t) / m
        for (CelestialObject body: bodies) {
            Vector3D startVel = body.getHEEvel();
            Vector3D startPos = body.getHEEpos();

            Vector3D startForce = body.getForces();
            Vector3D startAcceleration = startForce.scale(1D / body.getMass());

            // step 1 x(t + dt) = x(t) + v(t) + 0.5 a(t) * dt * dt
            Vector3D velChange = startVel.scale(dt);
            Vector3D accChange = startAcceleration.scale((dt * dt) / 2.0);
            body.setHEEpos(startPos.add(velChange.add(accChange))); // set half vel
        }

        // step 3 update the forces halfway
        updateForce();

        int i = 0;
        for (CelestialObject body: bodies) {
            Vector3D startVel = body.getHEEvel();
            // step 3 v(t + dt) = v(t) + 0.5 (a(t) + a(t + dt)) * dt
            Vector3D oldAcceleration = oldForces[i].scale(1D / body.getMass());
            Vector3D newAcceleration = body.getForces().scale(1D / body.getMass());
            Vector3D accelerationChange = oldAcceleration.add(newAcceleration).scale(dt / 2.0);
            body.setHEEvel(startVel.add(accelerationChange));
            i++;
        }
    }
     */

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
