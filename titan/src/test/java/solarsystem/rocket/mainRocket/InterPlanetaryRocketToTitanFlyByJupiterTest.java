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

import static org.junit.Assert.*;

/**
 *
 *
 */
public class InterPlanetaryRocketToTitanFlyByJupiterTest {
    @Test
    public void day2() throws IOException {
        TitanTest2_alongwayUpdate(2);
    }

    @Test
    public void day7() throws IOException {
        TitanTest2_alongwayUpdate(7);
    }


    @Test // best performance for saturn 0.4
    public void day10() throws IOException {
        TitanTest2_alongwayUpdate(10);
    }

    @Test
    public void day13() throws IOException {
        TitanTest2_alongwayUpdate(13);
    }

    @Test
    public void day17() throws IOException {
        TitanTest2_alongwayUpdate(17);
    }

    @Test
    public void day20() throws IOException {
        TitanTest2_alongwayUpdate(20);
    }


    public void TitanTest2_alongwayUpdate(int day) throws IOException {

        Date departDate = new Date(2018, 0, 18);
        Date refDate = new Date(departDate);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date flybyDate = new Date(2020, 2, 12);
        Date arrivalDate = new Date(2026, 2, day);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        System.out.println("sphere Saturn: " + sol_reference.getPlanets().getSaturn().getSphereOfInfluence());

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;
        double tof_flyby = (flybyDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;
        long timestep_seconds = 30;
        Vector3D flybyPos = null;
        Vector3D flybyVel = null;

        sol_reference.initializeAnimationWithPlanets(refDate, null);
        for(double tof_cur = 0; tof_cur < tof; tof_cur = tof_cur + timestep_seconds){
            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
            if(flybyPos == null && tof_cur > tof_flyby){
                flybyPos = sol_reference.getPlanets().getJupiter().getCentralPos();
                flybyVel = sol_reference.getPlanets().getJupiter().getCentralVel();
            }
        }

        Planet tit_arr = sol_reference.getPlanets().getTitan();
        Planet tit_dep = sol_depart.getPlanets().getTitan();
        Planet earth_dep = sol_depart.getPlanets().getEarth();
        Planet jupiter_dep = sol_depart.getPlanets().getJupiter();
        //double mass, Planet fromPlanet, Planet toPlanet, Date current_date, Vector3D departurePos, Vector3D destinationPos, Date arrivalDate ;

        Vector3D arrivalPos = tit_arr.getCentralPos();
//        Vector3D arrivalPos = new Vector3D(1.3753660133497722E12, 2.772361116911658E11, -5.947866486590089E10);
//        Vector3D flybyPos = new Vector3D(1.5715795415164874E11, -7.625355088251364E11, -3.49797418664907E8);
        double sphereOfInfluenceJup = sol_reference.getPlanets().getJupiter().getSphereOfInfluence();
        flybyPos = flybyPos.add(flybyVel.unit().scale(sphereOfInfluenceJup * - 0.8));
        Vector3D startVel = earth_dep.getCentralVel();
        //Planet fromPlanet, Planet Jupiter, Planet toPlanet, Date current_date, Date jupiter_date, Date arrival_date, Vector3D departurePos, Vector3D flybyPos, Vector3D arrivalPos
        InterPlanetaryRocketToTitanFlyByJupiter rocket = new InterPlanetaryRocketToTitanFlyByJupiter(earth_dep, jupiter_dep, tit_dep, departDate, flybyDate, arrivalDate, null, flybyPos, arrivalPos);


        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(rocket);
        sol_depart.initializeAnimationWithPlanets(departDate, proj);

        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }
        Vector3D tit_arr_pos = sol_depart.getPlanets().planetByName("Titan").getCentralPos();
        Vector3D can_arr_pos = rocket.getCentralPos();
        Vector3D can_arr_vel = rocket.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println("Titan" + " pos: " + tit_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + tit_arr_pos.substract(can_arr_pos).length());
        System.out.println("Sphere of influence: " + tit_dep.getSphereOfInfluence());
        System.out.println("Diff: " + tit_arr_pos.substract(can_arr_pos));

        System.out.println();

        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Diff Sat: " + can_arr_pos.substract(sol_depart.getPlanets().getSaturn().getCentralPos()));
        System.out.println("Distance: " + can_arr_pos.substract(sol_depart.getPlanets().getSaturn().getCentralPos()).length());
        System.out.println("Sphere of influence: " + sol_depart.getPlanets().getSaturn().getSphereOfInfluence());



        System.out.println();

        System.out.println("fuel left: " + rocket.getFuelMass());

    }

}