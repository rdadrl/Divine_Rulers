package solarsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import physics.VerletVelocity;
import utils.Date;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class SolarSystem {
    private Planets planets;
    private Date currentDate;
    private ArrayList<CelestialObject> allAnimatedObjects;
    private ArrayList<Projectile> projectiles;
    private VerletVelocity verletVelocity;

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

    public ArrayList<? extends CelestialObject> getAllAnimatedObjects() {
        return allAnimatedObjects;
    }

    public void setAllAnimatedObjects(ArrayList<CelestialObject> allAnimatedObjects) {
        this.allAnimatedObjects = allAnimatedObjects;
    }

    public void getStaticPositionsPlanets(Date date){
        currentDate = date;
        for(Planet planet: getPlanets().getAll()){
            planet.initializeCartesianCoordinates(date);
        }
    }

    /**
     * Initialize an animation with all the planets present
     * @param date Date of initialization date
     * @param projectiles Additional projectiles such as CannonBalls or Rockets that need to be
     *                    part of the animation
     */
    public void initializeAnimation(Date date, ArrayList<Projectile> projectiles){
        allAnimatedObjects = new ArrayList<>(getPlanets().getAll());
        allAnimatedObjects.addAll(projectiles);
        this.projectiles = projectiles;
        verletVelocity = new VerletVelocity(this.allAnimatedObjects, date);
    }

    public void updateAnimation(long dt, TimeUnit timeUnit){
        if(verletVelocity == null){
            System.err.println("Cannot update animation without initializaton");
            System.exit(-1);
        }
        verletVelocity.updateLocation(dt, timeUnit);
    }

    public void updateAnimationRelativeTimeStep(long standardDt, TimeUnit standardTimeUnit){
        if(verletVelocity == null){
            System.err.println("Cannot update animation without initializaton");
            System.exit(-1);
        }
        if(CannonBall.minDistanceAllCurrentDT < 1.0E08) {
            verletVelocity.updateLocation(10, TimeUnit.SECONDS);
        }else if(CannonBall.minDistanceAllCurrentDT < 1.0E09){
            verletVelocity.updateLocation(100, TimeUnit.SECONDS);
        }else if(CannonBall.minDistanceAllCurrentDT < 1.0E10){
            verletVelocity.updateLocation(1000, TimeUnit.SECONDS);
        }else if(CannonBall.minDistanceAllCurrentDT < 1.0E11){
            verletVelocity.updateLocation(10000, TimeUnit.SECONDS);
        }else{
            verletVelocity.updateLocation(standardDt, standardTimeUnit);
        }

    }

}
