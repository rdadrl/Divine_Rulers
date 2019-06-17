package physics;

import org.junit.Test;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import solarsystem.rocket.Projectile;
import solarsystem.rocket.cannonBall.CannonBall;
import utils.Date;
import utils.MathUtil;
import utils.vector.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class LambertSolverTest {
    double EPSILON = 1e-14;

    @Test
    public void TitanTest() throws IOException {
        Date departDate = new Date(2018, 5, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.getPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 25);
        SolarSystem sol_arrival = new SolarSystem();
        sol_arrival.getPositionsPlanetsAtDateKepler(arrivalDate);

        Planet titan_arr = sol_arrival.getPlanets().getTitan();
        Planet titan_dep = sol_depart.getPlanets().getTitan();
        Planet earth_dep = sol_depart.getPlanets().getEarth();

        CannonBall cannonBall = new CannonBall(1000, 100, earth_dep, titan_dep, departDate, new Vector3D(0,0,0));
        cannonBall.initializeCartesianCoordinates(departDate);

        double mu = sol_arrival.getPlanets().getSun().getMass() * MathUtil.G;
        Vector3D r1 = cannonBall.getCentralPos();
        System.out.println(r1);
        Vector3D r2 = titan_arr.getCentralPos();
        System.out.println(r2);
        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;
        LambertSolver lambertSolver = new LambertSolver(mu, r1, r2, tof);

        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        System.out.println(vel[0]);
        System.out.println(vel[1]);

        cannonBall.setStartVelocityVector(vel[0]);

        System.out.println(cannonBall.getCentralVel());
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(cannonBall);

        sol_arrival.initializeAnimation(departDate, proj);
        for(int i = 0; i < tof/30; i++){
            sol_arrival.updateAnimation(30, TimeUnit.SECONDS);
        }
        Vector3D tit_arr_pos = sol_depart.getPlanets().getTitan().getCentralPos();
        Vector3D can_arr_pos = (Vector3D) cannonBall.getCentralPos();
        System.out.println("Titan pos: " + tit_arr_pos);
        System.out.println("Titan pred pos: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + tit_arr_pos.substract(can_arr_pos).length());
        System.out.println("Kepler vs sim: " + tit_arr_pos.substract(r2).length());
        System.out.println("Dist with pred " + can_arr_pos.substract(r2).length());
        System.out.println("Sphere of influence: " + titan_arr.getSphereOfInfluence());
    }

    @Test
    public void JupiterTest() throws IOException {
        Date departDate = new Date(2018, 0, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.getPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 22);
        SolarSystem sol_arrival = new SolarSystem();
        sol_arrival.getPositionsPlanetsAtDateKepler(arrivalDate);

        Planet jup_arr = sol_arrival.getPlanets().getJupiter();
        Planet jup_dep = sol_depart.getPlanets().getJupiter();
        Planet earth_dep = sol_depart.getPlanets().getEarth();

        double mu = sol_arrival.getPlanets().getSun().getMass() * MathUtil.G;
        Vector3D r1 = earth_dep.getCentralPos();
        System.out.println(r1);
        Vector3D r2 = jup_arr.getCentralPos();
        System.out.println(r2);
        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;
        LambertSolver lambertSolver = new LambertSolver(mu, r1, r2, tof);

        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        System.out.println(vel[0]);
        System.out.println(vel[1]);

        CannonBall cannonBall = new CannonBall(1000, 100, earth_dep, jup_dep, departDate, vel[0]);
        cannonBall.initializeCartesianCoordinates(departDate);
        System.out.println(cannonBall.getCentralVel());
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(cannonBall);

        sol_arrival.initializeAnimation(departDate, proj);
        for(int i = 0; i < tof/60; i++){
            sol_arrival.updateAnimation(1, TimeUnit.MINUTES);
        }
        Vector3D jup_arr_pos = sol_depart.getPlanets().getJupiter().getCentralPos();
        Vector3D can_arr_pos = cannonBall.getCentralPos();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println("Jupiter pos: " + jup_arr_pos);
        System.out.println("Jupiter pred pos: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + jup_arr_pos.substract(can_arr_pos).length());
        System.out.println("Kepler vs sim: " + jup_arr_pos.substract(r2).length());
        System.out.println("Dist with pred " + can_arr_pos.substract(r2).length());
        System.out.println("Sphere of influence: " + jup_arr.getSphereOfInfluence());

    }

    @Test
    public void debugKM(){
        Vector3D r1 = new Vector3D(-243302263.06448805,  227288856.3934878,  98530963.0817577);
        Vector3D r2 = new Vector3D(143344432.8655673, 38540290.87886229, 16694114.729144625);
        double tof = 34836133.61114318; // in seconds
        double k = 132712442099.00002;
        LambertSolver lambertSolver = new LambertSolver(k, r1, r2, tof);
        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        assertEquals(-10.043744763368588,   vel[0].getX(), EPSILON)  ;
        assertEquals(-9.17025301865582,     vel[0].getY(), EPSILON)  ;
        assertEquals(-3.973922674654367,    vel[0].getZ(), EPSILON)  ;

        assertEquals(1.6168679513671442,    vel[1].getX(), EPSILON)  ;
        assertEquals(31.92512637681035,     vel[1].getY(), EPSILON)  ;
        assertEquals(13.8371359382399057,   vel[1].getZ(), EPSILON)  ;
    }

    @Test
    public void debugM(){
        Vector3D r1 = new Vector3D(-243302263.06448805,  227288856.3934878,  98530963.0817577);
        r1 = r1.scale(1000);
        Vector3D r2 = new Vector3D(143344432.8655673, 38540290.87886229, 16694114.729144625);
        r2 = r2.scale(1000);
        double tof = 34836133.61114318; // in seconds
        double k = 132712442099.00002;
        k = k * Math.pow(1000, 3);
        LambertSolver lambertSolver = new LambertSolver(k, r1, r2, tof);
        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
//        assertEquals(-10.043744763368588,   vel[0].getX(), EPSILON)  ;
//        assertEquals(-9.17025301865582,     vel[0].getY(), EPSILON)  ;
//        assertEquals(-3.973922674654367,    vel[0].getZ(), EPSILON)  ;
//
//        System.out.println(vel[0].getX());
//        System.out.println(vel[0].getY());
//        System.out.println(vel[0].getZ());
//        assertEquals(1.6168679513671442,    vel[1].getX(), EPSILON)  ;
//        assertEquals(31.92512637681035,     vel[1].getY(), EPSILON)  ;
//        assertEquals(13.8371359382399057,   vel[1].getZ(), EPSILON)  ;

        assertEquals(-10.043744763368577 * 1000,   vel[0].getX(), EPSILON)  ;
        assertEquals(-9.170253018655829 * 1000,     vel[0].getY(), EPSILON)  ;
        assertEquals(-3.9739226746543727 * 1000,    vel[0].getZ(), EPSILON)  ;

        assertEquals(1.6168679513671668 * 1000,    vel[1].getX(), EPSILON)  ;
        assertEquals(31.925126376810356 * 1000,     vel[1].getY(), EPSILON)  ;
        assertEquals(13.837135938239913 * 1000,   vel[1].getZ(), EPSILON)  ;

    }

    @Test
    public void findxyTest() {
        double ll = -0.250455837623667;
        double T = 1.776767070247781;
        double M =0;
        double numiter=35;
        double rtol=1e-8;

        ArrayList<double[]> xy = LambertSolver.findxy(ll, T, M, numiter, rtol);
    }

    @Test
    public void initialGuessTest() {
        double ll = -0.250455837623667;
        double T = 1.776767070247781;
        double M =0;
        assertEquals(-0.07468900748139251, LambertSolver.initial_guess(T, ll, M)[0], EPSILON)  ;
        assertEquals(10.760508602937438, LambertSolver.initial_guess(0.1, ll, M)[0], EPSILON)  ;
        assertEquals(-0.4293114233368308, LambertSolver.initial_guess(1, ll, M)[0], EPSILON)  ;

        assertEquals(-0.26559637479229886, LambertSolver.initial_guess(T, ll, 1)[0], EPSILON)  ;
        assertEquals(0.4646057877070317, LambertSolver.initial_guess(T, ll, 1)[1], EPSILON)  ;

    }

    @Test
    public void compute_y_Test() {
        double ll = -0.250455837623667;
        double x_i = 0.1;
        assertEquals(0.9684519371998928, LambertSolver.compute_y(x_i, ll), EPSILON);

    }

    @Test
    public void compute_psy_Test() {
        double ll = -0.250455837623667;
        double x = 0.1;
        double y = LambertSolver.compute_y(x, ll);
        assertEquals(1.7224834377536393, LambertSolver.compute_psi(x, y, ll), EPSILON);

    }

    @Test
    public void tof_test() {
        double ll = -0.250455837623667;
        double M =0;
        double x_1 = 0.1;
        double y = LambertSolver.compute_y(x_1, ll);
        assertEquals(1.4026328787580127, LambertSolver.tof_equation(x_1, y, 0.0, ll, M), EPSILON);
        double x_2 = 0.8;
        assertEquals(0.724466964856131, LambertSolver.tof_equation(x_2, y, 0.0, ll, M), EPSILON);
    }

}