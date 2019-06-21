package solarsystem.rocket.mainRocket2DRendezvous;

import org.junit.Test;
import physics.VerletVelocity;
import solarsystem.SolarSystem;
import solarsystem.Trajectory;
import utils.Date;
import utils.vector.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class MainRocket2DRendezvousTest {

    private String folderName = "advanced_wind_trialMaxRot_noPID/";
    private String pathName;
    boolean CHART = false;
    boolean PRINT = false;
    boolean CSV = true;
    final double PRINT_INTERVAL = 100;
    final int testCase = 1;
    physics.ODEsolver ODEsolver = new VerletVelocity();
    private static ArrayList<String[]> csvRows = new ArrayList<>();

    @Test
    public void name() throws IOException {
        /**
        SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
        Date date = new Date(2002, 9, 18, 12, 0, 0); //Date
        double titanMass = 1.3452*Math.pow(10,23); // Mass in kg
        double sphereOfInfluence = 122187e04*Math.pow(titanMass/5.6834e26,(2.0/5.0)); // Semimajor axis of Titan times ratio masses of titan and satur

        Vector3D initialPos = new Vector3D(0, 1, 0);//new Vector3D(-0.7071, 0.7071,0);
        initialPos = initialPos.scale(1.3*sphereOfInfluence);
        //initialPos = initialPos.add((new Vector3D(0,-1,0)).scale(solarSystem.getPlanets().getTitan().getRadius()*1e03));
        Vector3D initialVel = new Vector3D(0,0,0);//new Vector3D(0,-1,0).scale(5e02);
        solarSystem.getPlanets().getTitan().setCentralPos(new Vector3D(0,0,0));
        solarSystem.getPlanets().getTitan().setCentralVel(new Vector3D(0,0,0));
        Trajectory trajectory = new Trajectory(solarSystem.getPlanets().getTitan(), 0, 0);

        MainRocket2DRendezvous rocket = new MainRocket2DRendezvous(solarSystem, 100000000, initialPos,
                initialVel, date, trajectory);
        runODE(rocket, date);
        **/

    }

    private void runODE(MainRocket2DRendezvous rocket, Date date) {
        ArrayList<MainRocket2DRendezvous> obj = new ArrayList<>();
        obj.add(rocket);
        ODEsolver.initialize(obj, date);

        for (int i = 0; i < (2000 * (1d / 0.01)); i++) {
            ODEsolver.updateLocation(1, TimeUnit.SECONDS);
        }
    }
}