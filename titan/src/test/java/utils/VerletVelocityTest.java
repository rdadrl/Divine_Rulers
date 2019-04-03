package utils;

import org.junit.Test;
import physics.VerletVelocity;
import solarsystem.Planet;
import solarsystem.SolarSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
        ArrayList<Planet> planets = solarSystem.getPlanets().getAll();
        Planet plan = solarSystem.getPlanets().getEarth();
        VerletVelocity verletVelocity = new VerletVelocity(planets, date);
        System.out.println(plan.getName());
        for(int i = 0; i < 365*10; i++){
            {
                System.out.print(date);
                System.out.print("\t\t" + plan.getCentralPos());
                System.out.print("\t\t" + plan.getCentralVel() + "\n");
            }
            verletVelocity.updateLocation(1, TimeUnit.DAYS);
        }





    }

    @Test
    public void verletTest2() throws IOException {
        Date date = new Date(2000, 0, 1, 12, 0);
        SolarSystem solarSystem = new SolarSystem();
        Planet tit = solarSystem.getPlanets().getTitan();
        Planet sat = solarSystem.getPlanets().getSaturn();
        VerletVelocity verletVelocity = new VerletVelocity(solarSystem.getPlanets().getAll(), date);
        System.out.println(tit.getName());
        for(int i = 0; i < 365*7; i++){
            {
                System.out.println(verletVelocity.getCurrentDate());
                System.out.println("Tp: \t\t" + tit.getCentralPos());
                System.out.println("Sp: \t\t" + sat.getCentralPos());
                System.out.println("Tp: \t\t" + tit.getCentralVel());
                System.out.println("Sp: \t\t" + sat.getCentralVel());

            }
            verletVelocity.updateLocation(1, TimeUnit.DAYS);
        }
    }

    @Test
    public void verletTest2Date() throws IOException {
        SolarSystem solarSystem = new SolarSystem();
        Planet tit = solarSystem.getPlanets().getTitan();
        Planet sat = solarSystem.getPlanets().getSaturn();
        Date date = new Date(2000, 0, 1 , 12,0);

        VerletVelocity verletVelocity = new VerletVelocity(solarSystem.getPlanets().getAll(), date);

        for(int i = 0; i < 365*7; i++){
            {
                System.out.println(verletVelocity.getCurrentDate());
                System.out.println("Tp: \t\t" + tit.getCentralPos());
                System.out.println("Sp: \t\t" + sat.getCentralPos());
                System.out.println("Tp: \t\t" + tit.getCentralVel());
                System.out.println("Sp: \t\t" + sat.getCentralVel());

            }
            verletVelocity.updateLocation(1, TimeUnit.DAYS);
        }
    }


}