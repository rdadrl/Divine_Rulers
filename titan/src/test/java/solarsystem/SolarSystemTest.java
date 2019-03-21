package solarsystem;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;



/**
 *
 *
 */
public class SolarSystemTest {
    @Test
    public void solarSystemTest() throws IOException {
        SolarSystem solarSystem = new SolarSystem();
        assertEquals(solarSystem.getPlanets().getEarth().getMass(), 5.9723E24, 1e-5);
        assertEquals(solarSystem.getPlanets().getMercury().getCentralBody().getName(), "Sun");
        for(Planet planet: solarSystem.getPlanets().getAll()){
            System.out.println(planet.getName());
        }
    }

}