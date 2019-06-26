package physics;

import org.junit.Assert;
import org.junit.Test;
import solarsystem.SolarSystem;
import utils.Date;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 * Some test cases to check verlet velocity. Only one assertion is used as an extra check
 */
public class VerletVelocityTest {

    @Test
    public void VerletEarth() throws IOException {

        Date departDate = new Date(2000, 0, 1);
        SolarSystem sol_ODE = new SolarSystem();
        sol_ODE.setPositionsPlanetsAtDateKepler(departDate);

        SolarSystem sol_kepler = new SolarSystem();
        Date refDate = new Date(2001, 0, 1);
        sol_kepler.setPositionsPlanetsAtDateKepler(refDate);
        double tof = Math.abs((departDate.getTimeInMillis() - refDate.getTimeInMillis()) / 1000D);
        System.out.println(tof);


        long timestep_seconds = 30;
        sol_ODE.initializeAnimationWithPlanets(departDate, null);
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_ODE.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        System.out.println("ODE date: " + departDate);
        System.out.println("\t pos: " + sol_ODE.getPlanets().getEarth().getCentralPos());
        Assert.assertEquals(-2.718401807204254E10, sol_ODE.getPlanets().getEarth().getCentralPos().getX(), 1e-13);
        System.out.println("\t vel: " + sol_ODE.getPlanets().getEarth().getCentralVel());
        System.out.println("Ref date: " + refDate);
        System.out.println("\t pos: " + sol_kepler.getPlanets().getEarth().getCentralPos());
        System.out.println("\t vel: " + sol_kepler.getPlanets().getEarth().getCentralVel());

        System.out.println("diff pos: " + sol_ODE.getPlanets().getEarth().getCentralPos().substract( sol_kepler.getPlanets().getEarth().getCentralPos()));
        System.out.println("diff vel: " + sol_ODE.getPlanets().getEarth().getCentralVel().substract( sol_kepler.getPlanets().getEarth().getCentralVel()));
    }

    @Test
    public void VerletTitan() throws IOException {

        Date departDate = new Date(2018, 5, 18);
        SolarSystem sol_ODE = new SolarSystem();
        sol_ODE.setPositionsPlanetsAtDateKepler(departDate);

        SolarSystem sol_kepler = new SolarSystem();
        Date refDate = new Date(2025, 1, 25);
        sol_kepler.setPositionsPlanetsAtDateKepler(refDate);
        double tof = Math.abs((departDate.getTimeInMillis() - refDate.getTimeInMillis()) / 1000D);
        System.out.println(tof);


        long timestep_seconds = 30;
        sol_ODE.initializeAnimationWithPlanets(departDate, null);
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_ODE.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        System.out.println("ODE date: " + departDate);
        System.out.println("\t pos: " + sol_ODE.getPlanets().getTitan().getCentralPos());
        System.out.println("\t vel: " + sol_ODE.getPlanets().getTitan().getCentralVel());
        System.out.println("Ref date: " + refDate);
        System.out.println("\t pos: " + sol_kepler.getPlanets().getTitan().getCentralPos());
        System.out.println("\t vel: " + sol_kepler.getPlanets().getTitan().getCentralVel());

        System.out.println("diff pos: " + sol_ODE.getPlanets().getTitan().getCentralPos().substract( sol_kepler.getPlanets().getTitan().getCentralPos()));
        System.out.println("diff vel: " + sol_ODE.getPlanets().getTitan().getCentralVel().substract( sol_kepler.getPlanets().getTitan().getCentralVel()));
    }



    @Test
    public void VerletSaturn() throws IOException {

        Date departDate = new Date(2000, 0, 1);
        SolarSystem sol_ODE = new SolarSystem();
        sol_ODE.setPositionsPlanetsAtDateKepler(departDate);

        SolarSystem sol_kepler = new SolarSystem();
        Date refDate = new Date(2006, 0, 1);
        sol_kepler.setPositionsPlanetsAtDateKepler(refDate);
        double tof = Math.abs((departDate.getTimeInMillis() - refDate.getTimeInMillis()) / 1000D);
        System.out.println(tof);


        long timestep_seconds = 30;
        sol_ODE.initializeAnimationWithPlanets(departDate, null);
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_ODE.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        System.out.println("ODE date: " + departDate);
        System.out.println("\t pos: " + sol_ODE.getPlanets().getSaturn().getCentralPos());
        System.out.println("\t vel: " + sol_ODE.getPlanets().getSaturn().getCentralVel());
        System.out.println("Ref date: " + refDate);
        System.out.println("\t pos: " + sol_kepler.getPlanets().getSaturn().getCentralPos());
        System.out.println("\t vel: " + sol_kepler.getPlanets().getSaturn().getCentralVel());

        System.out.println("diff pos: " + sol_ODE.getPlanets().getSaturn().getCentralPos().substract( sol_kepler.getPlanets().getSaturn().getCentralPos()));
        System.out.println("diff vel: " + sol_ODE.getPlanets().getSaturn().getCentralVel().substract( sol_kepler.getPlanets().getSaturn().getCentralVel()));
    }

}