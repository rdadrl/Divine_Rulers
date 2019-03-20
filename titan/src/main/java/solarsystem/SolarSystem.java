package solarsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.Date;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 *
 */
public class SolarSystem {
    private Planets planets;
    private Date currentDate;
    private ArrayList<? extends ObjectInSpace> allObjects;

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

    public ArrayList<? extends ObjectInSpace> getAllObjects() {
        return allObjects;
    }

    public void setAllObjects(ArrayList<? extends ObjectInSpace> allObjects) {
        this.allObjects = allObjects;
    }

    public void getStartingPositionsPlanets(Date date){
        currentDate = date;
        for(CelestialObjects planet: getPlanets().getAll()){
            planet.initializeCartesianCoordinates(date);
        }

    }



}
