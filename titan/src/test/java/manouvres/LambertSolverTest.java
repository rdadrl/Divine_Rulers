package manouvres;

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
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class LambertSolverTest {
    double EPSILON = 1e-14;
    String toPlanet = "Titan";
    Date departDate;
    Date returnDate;


    public void TitanTest() throws IOException {
        Date departDate = new Date(2018, 5, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 25);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(arrivalDate);

        Planet titan_arr = sol_reference.getPlanets().getTitan();
        Planet titan_dep = sol_depart.getPlanets().getTitan();
        Planet earth_dep = sol_depart.getPlanets().getEarth();

        CannonBall cannonBall = new CannonBall(1000, 100, earth_dep, titan_dep, departDate, new Vector3D(0,0,0));
        cannonBall.initializeCartesianCoordinates(departDate);

        double mu = sol_depart.getPlanets().getSun().getMass() * MathUtil.G;
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

        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        long timestep_seconds = 30;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }
        Vector3D tit_arr_pos = sol_depart.getPlanets().getTitan().getCentralPos();
        Vector3D can_arr_pos = cannonBall.getCentralPos();
        Vector3D can_arr_vel = cannonBall.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println("Titan pos: " + tit_arr_pos);
        System.out.println("Titan pred pos: " + r2);
        System.out.println("Dist with pred " + can_arr_pos.substract(r2).length());
        System.out.println("Diff with pred " + can_arr_pos.substract(r2));
        System.out.println("Kepler vs sim: " + tit_arr_pos.substract(r2).length());

        System.out.println("Titan pos: " + tit_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + tit_arr_pos.substract(can_arr_pos).length());
        System.out.println("Diff: " + tit_arr_pos.substract(can_arr_pos));

        System.out.println("Titan pred: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + r2.substract(can_arr_pos).length());
        System.out.println("Diff: " + r2.substract(can_arr_pos));
        System.out.println("Sphere of influence: " + titan_arr.getSphereOfInfluence());
        System.out.println();
        System.out.println("Cannon ball vel: " + can_arr_vel);
        System.out.println("Cannon ball pre: " + vel[1]);
        System.out.println("Diff: " + vel[1].substract(can_arr_vel));
    }

    @Test
    public void Titan_1Update() throws IOException {
        TitanTest2_alongwayUpdate_sphereSaturn(1, toPlanet);
    }

    @Test
    public void Titan_10Update() throws IOException {
        TitanTest2_alongwayUpdate_sphereSaturn(10, toPlanet);
    }

    @Test
    public void Titan_100Update() throws IOException {
        TitanTest2_alongwayUpdate_sphereSaturn(100, toPlanet);
    }

    @Test
    public void Titan_1000Update() throws IOException {
        TitanTest2_alongwayUpdate_sphereSaturn(1000, toPlanet);
    }

    @Test
    public void Titan_2000Update() throws IOException {
        TitanTest2_alongwayUpdate_sphereSaturn(2000, toPlanet);
    }

    public void TitanTest2_alongwayUpdate(int updatesAlongTheWay, String toPlanetName) throws IOException {

        Date departDate = new Date(2018, 5, 18);
        Date refDate = new Date(2018, 5, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 17);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;

        sol_reference.initializeAnimationWithPlanets(refDate, null);
        long timestep_seconds = 30;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        Planet tit_arr = sol_reference.getPlanets().planetByName(toPlanetName);
        Planet tit_dep = sol_depart.getPlanets().planetByName(toPlanetName);
        Planet earth_dep = sol_depart.getPlanets().getEarth();

        CannonBall cannonBall = new CannonBall(1000, 100, earth_dep, tit_dep, departDate, new Vector3D(0,0,0));
        cannonBall.initializeCartesianCoordinates(departDate);

        double mu = sol_depart.getPlanets().getSun().getMass() * MathUtil.G;
        Vector3D r1 = cannonBall.getCentralPos();
        System.out.println(r1);
        Vector3D r2 = tit_arr.getCentralPos();
        System.out.println(r2);

        LambertSolver lambertSolver = new LambertSolver(mu, r1, r2, tof);

        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        System.out.println(vel[0]);
        System.out.println(vel[1]);

        cannonBall.setStartVelocityVector(vel[0]);

        System.out.println(cannonBall.getCentralVel());
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(cannonBall);

        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        HashSet<Integer> update_for_counter = alongPositionUpdates(tof, timestep_seconds, updatesAlongTheWay);
        int counter = 0;
        Vector3D changedVelAdd = new Vector3D();
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
            if(toPlanetName.equals("Titan")){
                Planet saturn = tit_dep.getCentralBody();
                double distanceToSaturn = saturn.getCentralPos().substract(cannonBall.getCentralPos()).length();
                if(distanceToSaturn < saturn.getSphereOfInfluence()){
                    System.out.println("In sphere of SATURN");

                }
            }
            if(update_for_counter.contains(counter)) {
                Vector3D r1_half = cannonBall.getCentralPos();
                LambertSolver lambertSolver_half = new LambertSolver(mu, r1_half, r2, tof_left);
                Vector3D[] vel_half = lambertSolver_half.getVelocityVectors().get(0);
                changedVelAdd = changedVelAdd.add(cannonBall.getCentralVel().substract(vel_half[0]).absolute());
                cannonBall.setCentralVel(vel_half[0]);
            }
            counter++;
        }
        Vector3D tit_arr_pos = sol_depart.getPlanets().planetByName(toPlanetName).getCentralPos();
        Vector3D can_arr_pos = cannonBall.getCentralPos();
        Vector3D can_arr_vel = cannonBall.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println(toPlanetName+ " pos: " + tit_arr_pos);
        System.out.println(toPlanetName+ " pred pos: " + r2);
        System.out.println("Kepler vs sim: " + tit_arr_pos.substract(r2).length());
        System.out.println("Diff with pred " + tit_arr_pos.substract(r2));


        System.out.println();

        System.out.println(toPlanetName + " pos: " + tit_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + tit_arr_pos.substract(can_arr_pos).length());
        System.out.println("Diff: " + tit_arr_pos.substract(can_arr_pos));

        System.out.println();

        System.out.println(toPlanetName + " pred: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Diff: " + r2.substract(can_arr_pos));
        System.out.println("Distance: " + r2.substract(can_arr_pos).length());
        System.out.println("Sphere of influence: " + tit_arr.getSphereOfInfluence());

        System.out.println();

        System.out.println("Cannon ball vel: " + can_arr_vel);
        System.out.println("Cannon ball pre: " + vel[1]);
        System.out.println("Cannon ball vel: " + can_arr_vel.length() + ", " + can_arr_vel.length() * timestep_seconds);
        System.out.println("Diff: " + vel[1].substract(can_arr_vel));

        System.out.println(changedVelAdd);
    }


    public void TitanTest2_alongwayUpdate_sphereSaturn(int updatesAlongTheWay, String toPlanetName) throws IOException {

        Date departDate = new Date(2018, 5, 18);
        Date refDate = new Date(2018, 5, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 21);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;

        sol_reference.initializeAnimationWithPlanets(refDate, null);
        long timestep_seconds = 30;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        Planet tit_arr = sol_reference.getPlanets().planetByName(toPlanetName);
        Planet tit_dep = sol_depart.getPlanets().planetByName(toPlanetName);
        Planet earth_dep = sol_depart.getPlanets().getEarth();

        CannonBall cannonBall = new CannonBall(1000, 100, earth_dep, tit_dep, departDate, new Vector3D(0,0,0));
        cannonBall.initializeCartesianCoordinates(departDate);

        double mu = sol_depart.getPlanets().getSun().getMass() * MathUtil.G;
        Vector3D r1 = cannonBall.getCentralPos();
        System.out.println(r1);
        Vector3D r2 = tit_arr.getCentralPos();
        System.out.println(r2);

        LambertSolver lambertSolver = new LambertSolver(mu, r1, r2, tof);

        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        System.out.println(vel[0]);
        System.out.println(vel[1]);

        cannonBall.setStartVelocityVector(vel[0]);

        System.out.println(cannonBall.getCentralVel());
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(cannonBall);

        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        HashSet<Integer> update_for_counter = alongPositionUpdates(tof, timestep_seconds, updatesAlongTheWay);
        int counter = 0;
        Vector3D changedVelAdd = new Vector3D();
        boolean inSphereSaturn = false;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
            if(toPlanetName.equals("Titan")){
                Planet saturn = tit_dep.getCentralBody();
                double distanceToSaturn = saturn.getCentralPos().substract(cannonBall.getCentralPos()).length();
                if(!inSphereSaturn && distanceToSaturn < saturn.getSphereOfInfluence()){
                    System.out.println("In sphere of SATURN");
                    System.out.println(tof_left);
                    inSphereSaturn = true;
                    System.out.println("Sat: " + saturn.getCentralPos());
                    System.out.println("Tit_n: " + sol_depart.getPlanets().getTitan().getCentralPos());
                    System.out.println("Tit_p: " + r2);
                    System.out.println("Canno: " + cannonBall.getCentralPos());
                    System.out.println();
                    System.out.println("r_Sat: " + saturn.getCentralPos().substract(saturn.getCentralPos()));
                    System.out.println("r_Tit_n: " + sol_depart.getPlanets().getTitan().getCentralPos().substract(saturn.getCentralPos()));
                    System.out.println("r_Tit_p: " + r2.substract(saturn.getCentralPos()));
                    System.out.println("r_Canno: " + cannonBall.getCentralPos().substract(saturn.getCentralPos()));

                    System.out.println();
                    Vector3D r1_saturnPer = cannonBall.getCentralPos().substract(saturn.getCentralPos());
                    Vector3D r2_saturnPer = r2.substract(saturn.getCentralPos());
                    double mu_saturn = saturn.getMass() * MathUtil.G;



                    LambertSolver lambertSolver_half = new LambertSolver(mu_saturn, r1_saturnPer, r2_saturnPer, tof_left);
                    Vector3D[] vel_half = lambertSolver_half.getVelocityVectors().get(0);
                    changedVelAdd = changedVelAdd.add(cannonBall.getCentralVel().substract(vel_half[0]));
                    cannonBall.setCentralVel(vel_half[0]);
                }
            }
//            if(update_for_counter.contains(counter)) {
//                Vector3D r1_half = cannonBall.getCentralPos();
//                LambertSolver lambertSolver_half = new LambertSolver(mu, r1_half, r2, tof_left);
//                Vector3D[] vel_half = lambertSolver_half.getVelocityVectors().get(0);
//                changedVelAdd = changedVelAdd.add(cannonBall.getCentralVel().substract(vel_half[0]).absolute());
//                cannonBall.setCentralVel(vel_half[0]);
//            }
            counter++;
        }
        Vector3D tit_arr_pos = sol_depart.getPlanets().planetByName(toPlanetName).getCentralPos();
        Vector3D can_arr_pos = cannonBall.getCentralPos();
        Vector3D can_arr_vel = cannonBall.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println(toPlanetName+ " pos: " + tit_arr_pos);
        System.out.println(toPlanetName+ " pred pos: " + r2);
        System.out.println("Kepler vs sim: " + tit_arr_pos.substract(r2).length());
        System.out.println("Diff with pred " + tit_arr_pos.substract(r2));


        System.out.println();

        System.out.println(toPlanetName + " pos: " + tit_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + tit_arr_pos.substract(can_arr_pos).length());
        System.out.println("Diff: " + tit_arr_pos.substract(can_arr_pos));

        System.out.println();

        System.out.println(toPlanetName + " pred: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Diff: " + r2.substract(can_arr_pos));
        System.out.println("Distance: " + r2.substract(can_arr_pos).length());
        System.out.println("Sphere of influence: " + tit_arr.getSphereOfInfluence());

        System.out.println();

        System.out.println("Cannon ball vel: " + can_arr_vel);
        System.out.println("Cannon ball pre: " + vel[1]);
        System.out.println("Cannon ball vel: " + can_arr_vel.length() + ", " + can_arr_vel.length() * timestep_seconds);
        System.out.println("Diff: " + vel[1].substract(can_arr_vel));

        System.out.println(changedVelAdd);
    }




    public void JupiterTest2() throws IOException {
        Date departDate = new Date(2018, 0, 18);
        Date refDate = new Date(2018, 0, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 22);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;

        sol_reference.initializeAnimationWithPlanets(refDate, null);
        long timestep_seconds = 30;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        Planet jup_arr = sol_reference.getPlanets().getJupiter();
        Planet jup_dep = sol_depart.getPlanets().getJupiter();
        Planet earth_dep = sol_depart.getPlanets().getEarth();

        CannonBall cannonBall = new CannonBall(1000, 100, earth_dep, jup_dep, departDate, new Vector3D(0,0,0));
        cannonBall.initializeCartesianCoordinates(departDate);

        double mu = sol_depart.getPlanets().getSun().getMass() * MathUtil.G;
        Vector3D r1 = cannonBall.getCentralPos();
        System.out.println(r1);
        Vector3D r2 = jup_arr.getCentralPos();
        System.out.println(r2);

        LambertSolver lambertSolver = new LambertSolver(mu, r1, r2, tof);

        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        System.out.println(vel[0]);
        System.out.println(vel[1]);

        cannonBall.setStartVelocityVector(vel[0]);

        System.out.println(cannonBall.getCentralVel());
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(cannonBall);

        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }
        Vector3D jup_arr_pos = sol_depart.getPlanets().getJupiter().getCentralPos();
        Vector3D can_arr_pos = cannonBall.getCentralPos();
        Vector3D can_arr_vel = cannonBall.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println("Jup pos: " + jup_arr_pos);
        System.out.println("Jup pred pos: " + r2);
        System.out.println("Kepler vs sim: " + jup_arr_pos.substract(r2).length());
        System.out.println("Diff with pred " + jup_arr_pos.substract(r2));


        System.out.println();

        System.out.println("Jup pos: " + jup_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + jup_arr_pos.substract(can_arr_pos).length());
        System.out.println("Diff: " + jup_arr_pos.substract(can_arr_pos));

        System.out.println();

        System.out.println("Jup pred: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + r2.substract(can_arr_pos).length());
        System.out.println("Diff: " + r2.substract(can_arr_pos));
        System.out.println("Sphere of influence: " + jup_arr.getSphereOfInfluence());

        System.out.println();

        System.out.println("Cannon ball vel: " + can_arr_vel);
        System.out.println("Cannon ball pre: " + vel[1]);
        System.out.println("Diff: " + vel[1].substract(can_arr_vel));
    }


    public void JupiterTest2_halfwayUpdate() throws IOException {
        Date departDate = new Date(2018, 0, 18);
        Date refDate = new Date(2018, 0, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 22);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;

        sol_reference.initializeAnimationWithPlanets(refDate, null);
        long timestep_seconds = 30;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        Planet jup_arr = sol_reference.getPlanets().getJupiter();
        Planet jup_dep = sol_depart.getPlanets().getJupiter();
        Planet earth_dep = sol_depart.getPlanets().getEarth();

        CannonBall cannonBall = new CannonBall(1000, 100, earth_dep, jup_dep, departDate, new Vector3D(0,0,0));
        cannonBall.initializeCartesianCoordinates(departDate);

        double mu = sol_depart.getPlanets().getSun().getMass() * MathUtil.G;
        Vector3D r1 = cannonBall.getCentralPos();
        System.out.println(r1);
        Vector3D r2 = jup_arr.getCentralPos();
        System.out.println(r2);

        LambertSolver lambertSolver = new LambertSolver(mu, r1, r2, tof);

        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        System.out.println(vel[0]);
        System.out.println(vel[1]);

        cannonBall.setStartVelocityVector(vel[0]);

        System.out.println(cannonBall.getCentralVel());
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(cannonBall);

        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        boolean updated = false;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
            if(!updated && tof_left < tof/2) {
                Vector3D r1_half = cannonBall.getCentralPos();
                LambertSolver lambertSolver_half = new LambertSolver(mu, r1_half, r2, tof_left);
                Vector3D[] vel_half = lambertSolver_half.getVelocityVectors().get(0);
                cannonBall.setCentralVel(vel_half[0]);
                updated = true;
            }
        }
        Vector3D jup_arr_pos = sol_depart.getPlanets().getJupiter().getCentralPos();
        Vector3D can_arr_pos = cannonBall.getCentralPos();
        Vector3D can_arr_vel = cannonBall.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println("Jup pos: " + jup_arr_pos);
        System.out.println("Jup pred pos: " + r2);
        System.out.println("Kepler vs sim: " + jup_arr_pos.substract(r2).length());
        System.out.println("Diff with pred " + jup_arr_pos.substract(r2));


        System.out.println();

        System.out.println("Jup pos: " + jup_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + jup_arr_pos.substract(can_arr_pos).length());
        System.out.println("Diff: " + jup_arr_pos.substract(can_arr_pos));

        System.out.println();

        System.out.println("Jup pred: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + r2.substract(can_arr_pos).length());
        System.out.println("Diff: " + r2.substract(can_arr_pos));
        System.out.println("Sphere of influence: " + jup_arr.getSphereOfInfluence());

        System.out.println();

        System.out.println("Cannon ball vel: " + can_arr_vel);
        System.out.println("Cannon ball pre: " + vel[1]);
        System.out.println("Diff: " + vel[1].substract(can_arr_vel));
    }

    public void SaturntTest_halfwayUpdate() throws IOException {
        Date departDate = new Date(2018, 0, 18);
        Date refDate = new Date(2018, 0, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 22);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;

        sol_reference.initializeAnimationWithPlanets(refDate, null);
        long timestep_seconds = 30;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        Planet sat_arr = sol_reference.getPlanets().getSaturn();
        Planet sat_dep = sol_depart.getPlanets().getSaturn();
        Planet earth_dep = sol_depart.getPlanets().getEarth();

        CannonBall cannonBall = new CannonBall(1000, 100, earth_dep, sat_dep, departDate, new Vector3D(0,0,0));
        cannonBall.initializeCartesianCoordinates(departDate);

        double mu = sol_depart.getPlanets().getSun().getMass() * MathUtil.G;
        Vector3D r1 = cannonBall.getCentralPos();
        System.out.println(r1);
        Vector3D r2 = sat_arr.getCentralPos();
        System.out.println(r2);

        LambertSolver lambertSolver = new LambertSolver(mu, r1, r2, tof);

        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        System.out.println(vel[0]);
        System.out.println(vel[1]);

        cannonBall.setStartVelocityVector(vel[0]);

        System.out.println(cannonBall.getCentralVel());
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(cannonBall);

        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        boolean updated = false;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
            if(!updated && tof_left < tof/2) {
                Vector3D r1_half = cannonBall.getCentralPos();
                LambertSolver lambertSolver_half = new LambertSolver(mu, r1_half, r2, tof_left);
                Vector3D[] vel_half = lambertSolver_half.getVelocityVectors().get(0);
                cannonBall.setCentralVel(vel_half[0]);
                updated = true;
            }
        }
        Vector3D sat_arr_pos = sol_depart.getPlanets().getSaturn().getCentralPos();
        Vector3D can_arr_pos = cannonBall.getCentralPos();
        Vector3D can_arr_vel = cannonBall.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println("Jup pos: " + sat_arr_pos);
        System.out.println("Jup pred pos: " + r2);
        System.out.println("Kepler vs sim: " + sat_arr_pos.substract(r2).length());
        System.out.println("Diff with pred " + sat_arr_pos.substract(r2));


        System.out.println();

        System.out.println("Jup pos: " + sat_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + sat_arr_pos.substract(can_arr_pos).length());
        System.out.println("Diff: " + sat_arr_pos.substract(can_arr_pos));

        System.out.println();

        System.out.println("Jup pred: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + r2.substract(can_arr_pos).length());
        System.out.println("Diff: " + r2.substract(can_arr_pos));
        System.out.println("Sphere of influence: " + sat_arr.getSphereOfInfluence());

        System.out.println();

        System.out.println("Cannon ball vel: " + can_arr_vel);
        System.out.println("Cannon ball pre: " + vel[1]);
        System.out.println("Diff: " + vel[1].substract(can_arr_vel));
    }

    public void JupiterTest() throws IOException {
        Date departDate = new Date(2018, 0, 18);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2025, 1, 22);
        SolarSystem sol_arrival = new SolarSystem();
        sol_arrival.setPositionsPlanetsAtDateKepler(arrivalDate);

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

        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        long timestep_seconds = 30;
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_depart.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        Vector3D jup_arr_pos = sol_depart.getPlanets().getJupiter().getCentralPos();
        Vector3D can_arr_pos = cannonBall.getCentralPos();
        Vector3D can_arr_vel = cannonBall.getCentralVel();
        System.out.println(arrivalDate + ", " + sol_depart.getCurrentDate());

        System.out.println("Jup pos: " + jup_arr_pos);
        System.out.println("Jup pred pos: " + r2);
        System.out.println("Dist with pred " + can_arr_pos.substract(r2).length());
        System.out.println("Diff with pred " + can_arr_pos.substract(r2));
        System.out.println("Kepler vs sim: " + jup_arr_pos.substract(r2).length());

        System.out.println();

        System.out.println("Jup pos: " + jup_arr_pos);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + jup_arr_pos.substract(can_arr_pos).length());
        System.out.println("Diff: " + jup_arr_pos.substract(can_arr_pos));

        System.out.println();

        System.out.println("Jup pred: " + r2);
        System.out.println("Cannon ball pos: " + can_arr_pos);
        System.out.println("Distance: " + r2.substract(can_arr_pos).length());
        System.out.println("Diff: " + r2.substract(can_arr_pos));
        System.out.println("Sphere of influence: " + jup_arr.getSphereOfInfluence());
        System.out.println();
        System.out.println("Cannon ball vel: " + can_arr_vel);
        System.out.println("Cannon ball pre: " + vel[1]);
        System.out.println("Diff: " + vel[1].substract(can_arr_vel));



    }


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


    public void debugKM_checkORBITPDF(){
        Vector3D r1 = new Vector3D(5000, 10000, 2100);
        Vector3D r2 = new Vector3D(-14600, 2500, 7000);
        double tof = 3600; // in seconds
        double k = 398600;
        LambertSolver lambertSolver = new LambertSolver(k, r1, r2, tof);
        Vector3D[] vel = lambertSolver.getVelocityVectors().get(0);
        System.out.println(vel[0]);
        System.out.println(vel[1]);
//        assertEquals(-10.043744763368588,   vel[0].getX(), EPSILON)  ;
//        assertEquals(-9.17025301865582,     vel[0].getY(), EPSILON)  ;
//        assertEquals(-3.973922674654367,    vel[0].getZ(), EPSILON)  ;
//
//        assertEquals(1.6168679513671442,    vel[1].getX(), EPSILON)  ;
//        assertEquals(31.92512637681035,     vel[1].getY(), EPSILON)  ;
//        assertEquals(13.8371359382399057,   vel[1].getZ(), EPSILON)  ;
    }


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


    public void findxyTest() {
        double ll = -0.250455837623667;
        double T = 1.776767070247781;
        double M =0;
        double numiter=35;
        double rtol=1e-8;

        ArrayList<double[]> xy = LambertSolver.findxy(ll, T, M, numiter, rtol);
    }


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


    public void compute_y_Test() {
        double ll = -0.250455837623667;
        double x_i = 0.1;
        assertEquals(0.9684519371998928, LambertSolver.compute_y(x_i, ll), EPSILON);

    }


    public void compute_psy_Test() {
        double ll = -0.250455837623667;
        double x = 0.1;
        double y = LambertSolver.compute_y(x, ll);
        assertEquals(1.7224834377536393, LambertSolver.compute_psi(x, y, ll), EPSILON);

    }

    public void tof_test() {
        double ll = -0.250455837623667;
        double M =0;
        double x_1 = 0.1;
        double y = LambertSolver.compute_y(x_1, ll);
        assertEquals(1.4026328787580127, LambertSolver.tof_equation(x_1, y, 0.0, ll, M), EPSILON);
        double x_2 = 0.8;
        assertEquals(0.724466964856131, LambertSolver.tof_equation(x_2, y, 0.0, ll, M), EPSILON);
    }

    private HashSet<Integer> alongPositionUpdates(double tof, long timestep_val, int updates) {
        HashSet<Integer> results = new HashSet<>();
        double no_timesteps = tof/timestep_val;
        for(int i = 1; i < updates; i++) {
            results.add((int) (no_timesteps/updates) * i);
        }
        return results;
    }


    public void alongPosTest() {
        HashSet<Integer> pos = alongPositionUpdates(30000, 30, 3);
        for(int i: pos){
            System.out.println(i);
        }

    }
}