package SolarSystem;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 */
public class SolarSystem {
    private Planets planets;

    /**
     * Constructor of the solar system
     * @throws IOException
     */
    public SolarSystem() throws  IOException{
        createPlanets();
    }

    /**
     * creates the planets
     * @throws IOException
     */
    public void createPlanets() throws IOException {
        // Get JSON file
        InputStream fileStream = new FileInputStream("src/main/resources/planets.json");
        // Create planets from JSON file.
        planets = new ObjectMapper().readValue(fileStream, Planets.class);
    }

    /**
     * @return planets of the solarsystem
     */
    public Planets getPlanets() {
        return planets;
    }

}
