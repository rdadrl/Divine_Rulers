package solarsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.Date;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 */
public class SolarSystem {
    private Planets planets;
    private Date currentDate;

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
    private void createPlanets() throws IOException {
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

    public void getStartingPositionsPlanets(Date date){
        currentDate = date;
        for(CelestialObjects planet: getPlanets().getAll()){
            planet.initializeCartesianCoordinates(date);
        }

    }



}
