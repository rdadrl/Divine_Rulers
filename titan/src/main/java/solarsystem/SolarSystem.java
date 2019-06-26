package solarsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import physics.ODEsolver;
import physics.VerletVelocity;
import solarsystem.rocket.Projectile;
import solarsystem.rocket.cannonBall.CannonBall;
import utils.Date;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A CONTROLLER CLASS
 * A class representing all the different components of the whole solarsystem. This class has a reference to all the planets,
 * and allows for the rocket objects to be added. Furthermore, it is able to manage the simulation of the solar system through
 * the ODE solvers
 */
public class SolarSystem {
    private Planets planets;
    private Date currentDate;
    private ArrayList<CelestialObject> allAnimatedObjects;
    private ArrayList<Projectile> projectiles;
    private ODEsolver ODEsolver;

    /**
     * Constructor of the solar system
     * @throws IOException
     */
    public SolarSystem() throws  IOException{
        createPlanets();
        ODEsolver = new VerletVelocity();
    }

    /**
     * Constructor of the solar system
     * @throws IOException
     */
    public SolarSystem(ODEsolver odEsolver) throws  IOException{
        createPlanets();
        ODEsolver = odEsolver;
    }

    /**
     * @param ODEsolver ode solver wanted for the numerical simulator
     */
    public void setODEsolver(ODEsolver ODEsolver) {
        this.ODEsolver = ODEsolver;
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

    /**
     * @return the objects that will be animated by the numerical integrator
     */
    public ArrayList<? extends CelestialObject> getAllAnimatedObjects() {
        return allAnimatedObjects;
    }

    /**
     * @param allAnimatedObjects set the objects that will be animated by the numerical integrator
     */
    public void setAllAnimatedObjects(ArrayList<CelestialObject> allAnimatedObjects) {
        this.allAnimatedObjects = allAnimatedObjects;
    }

    /**
     * @param animatedObject add object to the animated objecst
     */
    public void addAnimatedObject(CelestialObject animatedObject) {

        allAnimatedObjects.add(animatedObject); }

    /**
     * @param name object to be removed from the animated objects
     */
    public void removeAnimatedObject(String name) {
        for (CelestialObject o : allAnimatedObjects) {
            if (o.getName().equals(name)) allAnimatedObjects.remove(o);
        }
    }

    /**
     * Get the starting positions of the planets based upon kepler approximation
     * @param date Date of the planet positions.
     */
    public void setPositionsPlanetsAtDateKepler(Date date){
        currentDate = date;
        for(Planet planet: getPlanets().getAll()){
            planet.initializeCartesianCoordinates(date);
        }
    }

    /**
     * Initialize an animation with all the planets present
     * @param date Date of initialization current_date
     * @param projectiles Additional projectiles such as CannonBalls or Rockets that need to be
     *                    part of the animation
     */
    public void initializeAnimationWithPlanets(Date date, ArrayList<Projectile> projectiles){
        allAnimatedObjects = new ArrayList<>(getPlanets().getAll());
        if(projectiles != null) allAnimatedObjects.addAll(projectiles);
        this.projectiles = projectiles;
        ODEsolver.initialize(this.allAnimatedObjects, date);
    }

    public void initializeAnimationWithoutPlanets(Date date, ArrayList<CelestialObject> objects) {
        allAnimatedObjects = new ArrayList<>(objects);
        this.projectiles = new ArrayList<>();
        for(CelestialObject c: objects) {
            if(c instanceof Projectile) {
                projectiles.add((Projectile) c);
            }
        }
        ODEsolver.initialize(this.allAnimatedObjects, date);
    }


    /**
     * @param dt the timestep amount
     * @param timeUnit the timestep unit
     */
    public void updateAnimation(long dt, TimeUnit timeUnit){
        if(ODEsolver == null){
            System.err.println("Cannot update animation without initializaton");
            System.exit(-1);
        }
        ODEsolver.updateLocation(dt, timeUnit);
    }

    /**
     * Will update the planets of the solarsystem based upon some standard timestep.
     * In addition as the projectile moves closer to the target planet, the timesteps will become
     * smaller
     *
     * @param standardDt standard time step
     * @param standardTimeUnit standard unit of the time step
     */
    public void updateAnimationRelativeTimeStep(long standardDt, TimeUnit standardTimeUnit){
        if(ODEsolver == null){
            System.err.println("Cannot update animation without initializaton");
            System.exit(-1);
        }
        if(CannonBall.minDistanceAllCurrentDT < 1.0E08) {
            ODEsolver.updateLocation(10, TimeUnit.SECONDS);
        }else if(CannonBall.minDistanceAllCurrentDT < 1.0E09){
            ODEsolver.updateLocation(100, TimeUnit.SECONDS);
        }else if(CannonBall.minDistanceAllCurrentDT < 1.0E10){
            ODEsolver.updateLocation(1000, TimeUnit.SECONDS);
        }else if(CannonBall.minDistanceAllCurrentDT < 1.0E11){
            ODEsolver.updateLocation(10000, TimeUnit.SECONDS);
        }else{
            ODEsolver.updateLocation(standardDt, standardTimeUnit);
        }

    }


    /**
     * @return current date of te solarsystem
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @return projectiles that are currently in the solarsystem.
     */
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
}
