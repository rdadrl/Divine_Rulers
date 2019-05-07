package solarsystem;

import org.junit.Test;
import utils.Constant;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class PlanetTest {

    @Test
    public void getSphereOfInfluence() throws IOException {
        SolarSystem solarSystem = new SolarSystem();
        Planet earth = solarSystem.getPlanets().getEarth();
        assertEquals(9.245476058611175E8, earth.getSphereOfInfluence(), Constant.EPSILON);
    }
}