package physics;


import solarsystem.CelestialObject;
import solarsystem.rocket.Projectile;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/*
    rk4 method for approximating the position and velocity of the celestial bodies under newton gravitational force
 */
public class RungeKutta4 implements ODEsolver {
    private Date currentDate;
    private ArrayList<? extends ODEsolvable> bodies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<solarsystem.Planet> planets;
    private Vector3D[][] dfs;


    public RungeKutta4(){};

    public RungeKutta4(ArrayList<? extends ODEsolvable> bodies,
                       Date date) {
        this.currentDate = date;
        this.bodies = bodies;
        projectiles = new ArrayList<>();
        planets = new ArrayList<>();
        for (ODEsolvable body: bodies) {
            body.initializeCartesianCoordinates(date);
            if (body instanceof Projectile) projectiles.add((Projectile) body);
            if (body instanceof solarsystem.Planet) planets.add((solarsystem.Planet) body);
            System.out.println(body);
        }
        dfs = new Vector3D[bodies.size()][2];
    }

    @Override
    public void initialize(ArrayList<? extends ODEsolvable> bodies, Date date) {
        this.currentDate = date;
        this.bodies = bodies;
        projectiles = new ArrayList<>();
        planets = new ArrayList<>();
        for (ODEsolvable body: bodies) {
            body.initializeCartesianCoordinates(date);
            if (body instanceof Projectile) projectiles.add((Projectile) body);
            if (body instanceof solarsystem.Planet) planets.add((solarsystem.Planet) body);
            System.out.println(body);
        }
        dfs = new Vector3D[bodies.size()][2];
    }
    /*
        the derivative function specific to the problem of determining the position and velocity
     */
    public Vector3D[] deriv(Vector3D[] y, Vector3D acceleration, double dt) {
        Vector3D[] doty = new Vector3D[2];
        doty[0] = y[1].scale(dt);
        doty[1] = acceleration.scale(dt);
        return doty;
    }

    /*
        predict the future value based on the slope and coefficient
        @param y, the start value
        @param k, the increment
        @param coff, the coefficent or the intermediate time step
        @return the 3D vector array for position and velocity
     */
    public static Vector3D[] predict(Vector3D[] y, Vector3D[] k, double coff) {
        Vector3D[] p = new Vector3D[2];
        for(int i = 0; i < y.length; i++) {
            p[i] = y[i].add(k[i].scale(coff));
        }
        return p;
    }

    /*
     * Calculate the final coefficient based on rk4
     * @param p0 initial position
     * @param v0 initial velocity
     * @param a0 initial acceleration
     * @param dt delta t
     * @param self the planet on which calculation is based on
     * @param others the list of planets
     * @return Vector3D[]{ka, kv}
     */
    public Vector3D[] rk4(Vector3D p0, Vector3D v0, double dt, ODEsolvable self) {
        Vector3D[] y = new Vector3D[] {p0, v0};
        self.setAcceleration(planets, currentDate);
        Vector3D acceleration = self.getAcceleration();

        Vector3D[] k1 = deriv(y, acceleration, dt);

        Vector3D[] yp = predict(y, k1, 0.5);
        Vector3D[] k2 = deriv(yp, acceleration, dt);

        yp = predict(y, k2, 0.5);
        Vector3D[] k3 = deriv(yp, acceleration, dt);

        yp = predict(y, k3, 1);
        Vector3D[] k4 = deriv(yp, acceleration, dt);

        Vector3D[] coff = new Vector3D[2];
        for(int i = 0; i < k1.length; i++) {
            coff[i] = k1[i].add(k2[i].add(k3[i]).scale(2)).add(k4[i]).scale(1.0/6);
        }
        return coff;

    }

    /*
     * simulation with RK4
     * @param planets planets
     * @param kav the dotv and dots, calculated by the formula (k1 + 2k2 + 2k3 + k4)/6
     * @param dt delta t
     * @param n number of steps
     */
    public void rk4sim(ArrayList<? extends ODEsolvable> bodies, Vector3D[][] kav, double dt) {
        //the increment is precalculated before updating the whole celestial bodies
        for(int i = 0; i < bodies.size(); i++) {
            if(bodies.get(i)!=null) {
                ODEsolvable planet = bodies.get(i);
               // System.out.println(planet.toString());
                kav[i] = rk4(planet.getCentralPos(), planet.getCentralVel(), dt, planet);
            }
        }

        //updating the celestial body position and velocity at the next time step
        for(int i = 0; i < bodies.size(); i++) {
            if(bodies.get(i) != null) {
                ODEsolvable planet = bodies.get(i);
                planet.setCentralPos(planet.getCentralPos().add(kav[i][0]));
                planet.setCentralVel(planet.getCentralVel().add(kav[i][1]));
            }
        }
    }

    @Override
    public void updateLocation(long time, TimeUnit unit) {

        time = TimeUnit.MILLISECONDS.convert(time, unit); //convert to seconds
        currentDate.add(Calendar.MILLISECOND, (int)time);
        double dt = time /1000D;
        rk4sim(bodies, dfs, dt);
    }
}
