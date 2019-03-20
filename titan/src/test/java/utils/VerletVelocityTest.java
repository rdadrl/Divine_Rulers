package utils;

import org.junit.Test;
import solarsystem.CannonBall;
import solarsystem.CelestialObjects;
import solarsystem.ObjectInSpace;
import solarsystem.SolarSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class VerletVelocityTest {

    @Test
    public void verletTest() throws IOException {
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        SolarSystem solarSystem = new SolarSystem();
        solarSystem.getStartingPositionsPlanets(date);
        ArrayList<CelestialObjects> planets = solarSystem.getPlanets().getAll();
        CelestialObjects plan = solarSystem.getPlanets().getEarth();
        VerletVelocity verletVelocity = new VerletVelocity(planets, date);
        System.out.println(plan.getName());
        for(int i = 0; i < 365*10; i++){
            {
                System.out.print(date);
                System.out.print("\t\t" + plan.getHEEpos());
                System.out.print("\t\t" + plan.getHEEvel() + "\n");
            }
            verletVelocity.updateLocation(1, TimeUnit.DAYS);
        }





    }

    @Test
    public void verletTest2() throws IOException {
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        SolarSystem solarSystem = new SolarSystem();
        solarSystem.getStartingPositionsPlanets(date);
        ArrayList<CelestialObjects> planets = new ArrayList<>();
        planets.add(solarSystem.getPlanets().getSun());
        planets.add(solarSystem.getPlanets().getEarth());

        CelestialObjects plan = solarSystem.getPlanets().getEarth();
        VerletVelocity verletVelocity = new VerletVelocity(planets, date);
        System.out.println(plan.getName());
        for(int i = 0; i < 365*10; i++){
            {
                System.out.print(date);
                System.out.print("\t\t" + plan.getHEEpos());
                System.out.print("\t\t" + plan.getHEEvel() + "\n");
            }
            verletVelocity.updateLocation(1, TimeUnit.DAYS);
        }





    }


}