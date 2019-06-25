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
 *
 *
 */
public class InterPlanetaryRocketToEarthTest {

    @Test
    public void TitanTest2_alongwayUpdate() throws IOException {

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
//        double escapeVel_val = 11000;
//        Vector3D escapeVel = startVel.unit().scale(11000);
////        startVel = startVel.add(escapeVel);
//        double mass, Planet fromPlanet, Planet toPlanet, Date current_date, Vector3D departurePos, Vector3D departureVel, Vector3D destinationPos, Date arrivalDate
        InterPlanetaryRocketToEarth rocket = new InterPlanetaryRocketToEarth(1000, titArr, eartDep, departDate, null, startVel, arrivalPos, arrivalDate);

//
//        double mu = sol_depart.getPlanets().getSun().getMass() * MathUtil.G;
//        Vector3D r1 = rocket.getCentralPos();
//        System.out.println(r1);
//        Vector3D r2 = eart_ref.getCentralPos();
//        System.out.println(r2);
//
//        LambertSolver lambertSolver = new LambertSolver(mu, r1, r2, tof);
//
//        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
//        System.out.println(vel[0]);
//        System.out.println(vel[1]);
//
//        rocket.setStartVelocityVector(vel[0]);
//
//        System.out.println(rocket.getCentralVel());
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(rocket);
//
        sol_depart.initializeAnimationWithPlanets(departDate, proj);
//        HashSet<Integer> update_for_counter = alongPositionUpdates(tof, timestep_seconds, updatesAlongTheWay);
//        int counter = 0;
//        Vector3D changedVelAdd = new Vector3D();
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

    }

}