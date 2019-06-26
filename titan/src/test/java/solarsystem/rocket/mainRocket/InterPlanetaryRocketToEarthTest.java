package solarsystem.rocket.mainRocket;

import org.junit.Test;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import solarsystem.rocket.Projectile;
import utils.Date;
import utils.vector.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A class to test the results of an interplanetary mission from earth to Titan.
 */
public class InterPlanetaryRocketToEarthTest {

    @Test
    public void toEarthWeGo() throws IOException {
        // Date based upon the NASA trajectory search.
        Date departDate = new Date(2028, 9, 12);
        Date refDate = new Date(departDate);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2034, 4, 22);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;

        long timestep_seconds = 30;
        sol_reference.initializeAnimationWithPlanets(refDate, null);
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        Planet eart_ref = sol_reference.getPlanets().getEarth();
        Planet eartDep = sol_depart.getPlanets().getEarth();
        Planet titArr = sol_depart.getPlanets().getTitan();
        //double mass, Planet fromPlanet, Planet toPlanet, Date current_date, Vector3D departurePos, Vector3D destinationPos, Date arrivalDate ;

        Vector3D arrivalPos = eart_ref.getCentralPos();
        Vector3D startVel = titArr.getCentralVel();
        InterPlanetaryRocketToEarth rocket = new InterPlanetaryRocketToEarth(1000, titArr, eartDep, departDate, null, startVel, arrivalPos, arrivalDate);
        rocket.stageTwo();
        rocket.setFuelMass_t0(93053.31074034973);

        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(rocket);
//
        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }
        Vector3D eart_arr_pos = sol_depart.getPlanets().planetByName("Earth").getCentralPos();
        Vector3D can_arr_pos = rocket.getCentralPos();
        Vector3D can_arr_vel = rocket.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println("Earth"+ " pos: " + eart_arr_pos);
        System.out.println("Earth"+ " pred pos: " + arrivalPos);
        System.out.println("Kepler vs sim: " + eart_arr_pos.substract(arrivalPos).length());
        System.out.println("Diff with pred " + eart_arr_pos.substract(arrivalPos));


        System.out.println();

        System.out.println("Earth" + " pos: " + eart_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + eart_arr_pos.substract(can_arr_pos).length());
        System.out.println("Diff: " + eart_arr_pos.substract(can_arr_pos));

        System.out.println();

        System.out.println("Earth" + " pred: " + arrivalPos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Diff: " + arrivalPos.substract(can_arr_pos));
        System.out.println("Distance: " + arrivalPos.substract(can_arr_pos).length());
        System.out.println("Sphere of influence: " + eartDep.getSphereOfInfluence());

        System.out.println();

        System.out.println("fuel left: " + rocket.getFuelMass());
        System.out.println("fuel used: " + rocket.getFuellUsed());

    }

}